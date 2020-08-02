#include "master/client/master_client.h"
#include "master/constants.h"

namespace dousi {
namespace master {

/** The protocol from MasterClient to MasterServer is:
 *
 *    +--------------------------------------+
 *    |  type  |   header  |      body       |
 *    +--------------------------------------+
 *     1 byte   4 bytes         N bytes
 *
 *  The first 1 byte is a flag to indicate the type of this message, and it
 *  contains 2types now:
 *   - 0x00 indicates `RegisterService` type.
 *   - 0x01 indicates `FetchService` type.
 *
 *  The following 4 bytes is the header of this message, it is an unsigned int
 *  in 4 bytes, indicating the length of this message's body in bytes.
 *
 *  And the following N bytes are the body part. Body is a serialized byte array
 *  with a tuple in msgpack. Its 1st element indicates the service name and the
 *  2nd element indicates the service address.
 */
void MasterClient::RegisterService(const std::string &service_name,
    const std::string &service_address) {
  msgpack::type::tuple<std::string, std::string> message(service_name, service_address);
  // Any class that implements `write(const char *, size_t)` can be a buffer.
  std::stringstream buffer;
  msgpack::pack(buffer, message);
  buffer.seekg(0);
  auto str_to_be_sent = buffer.str();
  const auto buffer_size = str_to_be_sent.size();
  // TODO(qwang): It's better to pack the type-header-body as a byte array.
  // Now it's safe to invoke these 3 functions directly, because the io_service is
  // in a single thread so that these writings should be invoked one by one instead
  // of parallel.
  DoWriteType(ProtocolConstants::MESSAGE_TYPE_REGISTER_SERVICE,  nullptr);
  DoWriteHeader(static_cast<uint32_t>(buffer_size), nullptr);
  DoWriteBody(str_to_be_sent);
}

void MasterClient::DoWriteType(uint8_t type, const std::function<void()> &done_callback) {
  boost::asio::post(io_context_, [type, done_callback, this]() {
    boost::asio::async_write(socket_,
        boost::asio::buffer(reinterpret_cast<const char *>(&type), 1),
        [this, type, done_callback](boost::system::error_code error_code, size_t) {
      if (error_code) {
        socket_.close();
        DOUSI_LOG(INFO) << "Failed to write type to server with error code:" << error_code;
      } else {
        DOUSI_LOG(DEBUG) << "Succeeded to write type to server, type=" << type;
        if (done_callback != nullptr) {
          done_callback();
        }
      }
    });
  });
}

void MasterClient::DoWriteHeader(uint32_t body_size, const std::function<void()> &done_callback) {
  boost::asio::post(io_context_, [this, done_callback, body_size]() {
    char header[sizeof(body_size)];
    memcpy(header, &body_size, sizeof(body_size));
    boost::asio::async_write(socket_, boost::asio::buffer(header, sizeof(body_size)),
        [this, done_callback, body_size](boost::system::error_code error_code, size_t) {
      if (error_code) {
        socket_.close();
        DOUSI_LOG(INFO) << "Failed to write header to server with error code:" << error_code;
      } else {
        DOUSI_LOG(DEBUG) << "Succeeded to write header to server, header=" << body_size;
        if (done_callback != nullptr) {
          done_callback();
        }
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

void MasterClient::FetchService(const std::string &service_name,
    const std::function<void(bool ok, const std::string &address)> &callback) {

}

} // namespace master
} // namespace dousi
