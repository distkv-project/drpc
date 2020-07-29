#ifndef _DOUSI_MASTER_CLIENT_SESSION_H_
#define _DOUSI_MASTER_CLIENT_SESSION_H_

#include "common/endpoint.h"
#include "common/logging.h"

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

    // Note this heap uint32_t object is necessary because we should pass this into the async
    // callback. If it's a local variable, it may be deconstructed immediately.
    std::shared_ptr<uint32_t> body_size_ptr = std::make_shared<uint32_t>();
    constexpr size_t HEADER_SIZE = sizeof(*body_size_ptr);
    // Read header first.
    // We should make sure the endian here. Otherwise the value will be incorrect.
    boost::asio::async_read(socket_, boost::asio::buffer(reinterpret_cast<char *>(body_size_ptr.get()), HEADER_SIZE),
        [this, body_size_ptr, self](boost::system::error_code error_code, size_t length) {
      // ASSERT(length == HEADER_SIZE);
      if (error_code) {
        DOUSI_LOG(INFO) << "Failed to receive header with error code: " << error_code;
        return;
      }
      DoReadBody(*body_size_ptr);
    });
  }

  void DoReadBody(uint32_t body_size) {
    auto self(shared_from_this());
    std::shared_ptr<char> buffer_ptr(new char[body_size], std::default_delete<char[]>());

    boost::asio::async_read(socket_, boost::asio::buffer(buffer_ptr.get(), body_size),
        [buffer_ptr, this, self](boost::system::error_code error_code, size_t length) {
      if (error_code) {
        DOUSI_LOG(INFO) << "Failed to receive body with error code: " << error_code;
        return;
      }
      // Decode immediately in this thread. This is not allowed in production.
      std::string message(buffer_ptr.get(), length);
      msgpack::object_handle object_handle = msgpack::unpack(message.data(), message.size());

      msgpack::object deserialized = object_handle.get();
      msgpack::type::tuple<std::string, std::string> service_name_and_addr = deserialized.as<
          msgpack::type::tuple<std::string, std::string>>();
      DOUSI_LOG(DEBUG) << "Succeeded to receive body : "
        << "service_name='" << service_name_and_addr.get<0>()
        << "', service_addr=" <<  service_name_and_addr.get<1>();
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
