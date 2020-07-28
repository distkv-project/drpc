#ifndef _DOUSI_MASTER_H_
#define _DOUSI_MASTER_H_

#include "endpoint.h"

#include <unordered_map>
#include <string>

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
    : listening_host_(std::move(listening_host)),
    listening_port_(listening_port) {}

  void loop() {}

private:
  std::string listening_host_;
  int16_t listening_port_;

  // The map that maps service name to its endpoint.
  std::unordered_map<std::string, Endpoint> endpoints_;
};

} // namespace master
} // namespace dousi

#endif
