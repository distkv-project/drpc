#ifndef _DOUSI_ENDPOINT_H_
#define _DOUSI_ENDPOINT_H_

#include <string>
#include <utility>

namespace dousi {

/**
* A endpoint that can represents for a networking end point in Dousi RPC.
 *
 * It is consists of 2 parts: host and port.
 * `host` indicates the location of a node and `port` indicate the networking
 * port that we can lookup a concrete networking service by it.
*/
class Endpoint {
public:
  Endpoint() = delete;

  Endpoint(std::string host, int16_t port) : host_(std::move(host)), port_(port) {}

  virtual ~Endpoint() = default;

  std::string GetHost() const {return host_;}

  int16_t GetPort() const {return port_;}

  std::string ToString() const {
    return host_ + ":" + std::to_string(port_);
  }

private:
  // The host of this endpoint locates on.
  std::string host_;
  // The port of this endpoint.
  int16_t port_;
};

} // namespace dousi

#endif
