#ifndef _DOUSI_MASTER_H_
#define _DOUSI_MASTER_H_

#include "endpoint.h"

#include <unordered_map>
#include <string>
#include <iostream>

namespace dousi {
namespace master {

/**
 * The master is a standalone component that provides the service discovery for
 * the Dousi servers and clients.
 *
 * A Dousi server provided by user should be registered into master, so that the
 * client which wants to use this service can find the service location from master.
 */
class Master {
public:
  Master() = delete;

  Master(std::string listening_host, int16_t listening_port)
    : listening_endpoint_(std::move(listening_host), listening_port) {}

  void loop() {
    std::cout << "Master is now listening on " << listening_endpoint_.ToString() << std::endl;
  }

private:
  // The endpoint master is listening on.
  Endpoint listening_endpoint_;

  // The map that maps service name to its endpoint.
  std::unordered_map<std::string, Endpoint> endpoints_;
};

} // namespace master
} // namespace dousi

#endif
