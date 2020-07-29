#include "master_client.h"

namespace dousi {
namespace master {

void MasterClient::RegisterService(const std::string &service_name,
    const std::string &service_address) {
  /** The protocol from MasterClient to MasterServer is:
   *
   *    +-----------------------------+
   *    |   header  |      body       |
   *    +-----------------------------+
   *      4 bytes         N bytes
   *
   *  The first 4 bytes is the header of this message, it is an unsigned int
   *  in 4 bytes, indicating the length of this message's body in bytes.
   *
   *  Body is a serialized byte array with a tuple in msgpack. Its 1st element
   *  indicates the service name and the 2nd element indicates the service
   *  address.
   */
  msgpack::type::tuple<std::string, std::string> message(service_name, service_address);
  // Any class that implements `write(const char *, size_t)` can be a buffer.
  std::stringstream buffer;
  msgpack::pack(buffer, message);
  buffer.seekg(0);
  auto str_to_be_sent = buffer.str();
  DoWriteHeader(static_cast<uint32_t>(str_to_be_sent.size()));
  DoWriteBody(str_to_be_sent);
}

void MasterClient::DoWriteHeader(uint32_t body_size) {
  boost::asio::post(io_context_, [this, body_size]() {
    char header[sizeof(body_size)];
    memcpy(header, &body_size, sizeof(body_size));

    boost::asio::async_write(socket_, boost::asio::buffer(header, sizeof(body_size)),
        [this, body_size](boost::system::error_code error_code, size_t) {
      if (error_code) {
        socket_.close();
        DOUSI_LOG(INFO) << "Failed to write header to server with error code:" << error_code;
      } else {
        DOUSI_LOG(DEBUG) << "Succeeded to write header to server, header=" << body_size;
      }
    });
  });
}

void MasterClient::DoWriteBody(const std::string &str) {
  // Copy str by value here.
  boost::asio::post(io_context_, [this, str]() {
    boost::asio::async_write(
        socket_, boost::asio::buffer(str.data(), str.size()),
        [this](boost::system::error_code error_code, size_t /*length*/) {
          if (error_code) {
            socket_.close();
            DOUSI_LOG(INFO) << "Failed to write message to server with error code:" << error_code;
          } else {
            DOUSI_LOG(DEBUG) << "Succeeded to write message to server.";
          }
        });
  });
}

void MasterClient::DoConnect(const asio_tcp::resolver::results_type &endpoints) {
  boost::asio::async_connect(socket_, endpoints,
      [](boost::system::error_code error_code, const asio_tcp::endpoint &) {
    if (error_code) {
      DOUSI_LOG(INFO) << "Failed to connect to server with error code:" << error_code;
      return ;
    }
    DOUSI_LOG(DEBUG) << "Succeeded to connect message to server.";
  });
}

} // namespace master
} // namespace dousi
