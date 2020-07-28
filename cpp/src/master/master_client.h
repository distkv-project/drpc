#ifndef _DOUSI_MASTER_MASTER_CLIENT_H_
#define _DOUSI_MASTER_MASTER_CLIENT_H_

#include "endpoint.h"

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
    // Copy str by value here.
    boost::asio::post(io_context_, [this, str]() {
      boost::asio::async_write(
          socket_, boost::asio::buffer(str.data(), str.size()),
          [this](boost::system::error_code error_code, size_t /*length*/) {
            if (error_code) {
              socket_.close();
              std::cout << "Failed to write message to server with error code:" << error_code << std::endl;
            } else {
              std::cout << "Succeeded to write message to server." << std::endl;
            }
          });
    });
  }

private:
  void DoConnect(const asio_tcp::resolver::results_type &endpoints) {
    boost::asio::async_connect(socket_, endpoints,
        [](boost::system::error_code error_code, const asio_tcp::endpoint &) {
      if (error_code) {
        std::cout << "Failed to connect to server with error code:" << error_code << std::endl;
        return ;
      }
      std::cout << "Succeeded to connect message to server." << std::endl;
    });
  }

private:
  boost::asio::io_context &io_context_;
  asio_tcp::socket socket_;

};

} // namespace master
} // namespace dousi

#endif
