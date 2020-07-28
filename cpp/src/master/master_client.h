#ifndef _DOUSI_MASTER_MASTER_CLIENT_H_
#define _DOUSI_MASTER_MASTER_CLIENT_H_

#include "endpoint.h"
#include "common/logging.h"

#include <iostream>

namespace dousi {
namespace master {

class MasterClient {
public:

  MasterClient(boost::asio::io_context &io_context, Endpoint endpoint)
    : io_context_(io_context), socket_(io_context) {
    DoConnect(endpoint.Resolve(io_context_));
  }

  void Write(const std::string &str) {
    // write header.
    DoWriteHeader(static_cast<uint32_t>(str.size()));
    // write body.
    DoWriteBody(str);
  }

  void DoWriteHeader(uint32_t body_size) {
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

  void DoWriteBody(const std::string &str) {
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

private:
  void DoConnect(const asio_tcp::resolver::results_type &endpoints) {
    boost::asio::async_connect(socket_, endpoints,
        [](boost::system::error_code error_code, const asio_tcp::endpoint &) {
      if (error_code) {
        DOUSI_LOG(INFO) << "Failed to connect to server with error code:" << error_code;
        return ;
      }
      DOUSI_LOG(DEBUG) << "Succeeded to connect message to server.";
    });
  }

private:
  boost::asio::io_context &io_context_;
  asio_tcp::socket socket_;
};

} // namespace master
} // namespace dousi

#endif
