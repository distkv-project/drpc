#ifndef _DOUSI_MASTER_CLIENT_SESSION_H_
#define _DOUSI_MASTER_CLIENT_SESSION_H_

#include "endpoint.h"

#include <msgpack.hpp>

#include <utility>
#include <memory>
#include <iostream>

namespace dousi {
namespace master {

class MasterClientSession : public std::enable_shared_from_this<MasterClientSession> {
public:
  explicit MasterClientSession(asio_tcp::socket socket) : socket_(std::move(socket)) {}


  void Start() {
    DoReadHeader();
  }

private:

  void DoReadHeader() {
    // Note `self` is necessary here to make sure it will be not released in the async callback.
    auto self(shared_from_this());
    int32_t body_size;
    constexpr size_t HEADER_SIZE = sizeof(body_size);
    // Read header first.
    // We should make sure the endian here. Otherwise the value will be incorrect.
    boost::asio::async_read(socket_, boost::asio::buffer(&body_size, HEADER_SIZE),
        [this, self](boost::system::error_code error_code, size_t length) {
      // ASSERT(length == HEADER_SIZE);
      if (error_code) {
        std::cout << "Failed to receive header with error code: " << error_code << std::endl;
        return;
      }
      DoReadBody(length);
    });
  }

  void DoReadBody(int32_t body_size) {
    auto self(shared_from_this());
    constexpr size_t BUFFER_SIZE = 1024;
    char buffer[BUFFER_SIZE];
    boost::asio::async_read(socket_, boost::asio::buffer(buffer, body_size),
        [&buffer, this, self](boost::system::error_code error_code, size_t length) {
      if (error_code) {
        std::cout << "Failed to receive body with error code: " << error_code << std::endl;
        return;
      }
      // Decode immediately in this thread. This is not allowed in production.
      std::string message(buffer, length);
      msgpack::object_handle object_handle = msgpack::unpack(message.data(), message.size());
      msgpack::object deserialized = object_handle.get();
      msgpack::type::tuple<std::string, std::string> service_name_and_addr = deserialized.as<
          msgpack::type::tuple<std::string, std::string>>();
      std::cout << "Succeeded to receive body : "
        << "service_name=" << service_name_and_addr.get<0>()
        << ", service_addr=" <<  service_name_and_addr.get<1>()
        << std::endl;
      DoReadHeader();
    });
  }

private:
  // The socket handle for the client that connected to this master server.
  asio_tcp::socket socket_;
};

}
} // namespace dousi

#endif
