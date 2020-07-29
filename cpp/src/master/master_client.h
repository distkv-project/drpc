#ifndef _DOUSI_MASTER_MASTER_CLIENT_H_
#define _DOUSI_MASTER_MASTER_CLIENT_H_

#include "common/endpoint.h"
#include "common/logging.h"

#include <msgpack.hpp>

#include <iostream>

namespace dousi {
namespace master {

class MasterClient {
public:

  MasterClient(boost::asio::io_context &io_context, Endpoint master_server_endpoint)
    : io_context_(io_context), socket_(io_context) {
    DoConnect(master_server_endpoint.Resolve(io_context_));
  }

  /**
   * Register a Dousi RPC service to the master server.
   *
   * @param service_name The service name to be registered.
   * @param service_address The address of this service.
   */
  void RegisterService(const std::string &service_name,
                       const std::string &service_address);

private:
  void DoWriteHeader(uint32_t body_size);

  void DoWriteBody(const std::string &str);

  void DoConnect(const asio_tcp::resolver::results_type &endpoints);

private:
  boost::asio::io_context &io_context_;
  asio_tcp::socket socket_;
};

} // namespace master
} // namespace dousi

#endif
