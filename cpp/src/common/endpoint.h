#ifndef _DOUSI_ENDPOINT_H_
#define _DOUSI_ENDPOINT_H_

#include <string>
#include <utility>

#include <boost/asio.hpp>

namespace dousi {

/**
* A endpoint that can represents for a networking end point in Dousi RPC.
 *
 * It is consists of 2 parts: host and port.
 * `host` indicates the location of a node and `port` indicate the networking
 * port that we can lookup a concrete networking service by it.
*/

using asio_tcp = boost::asio::ip::tcp;

class Endpoint {
public:
  Endpoint() = delete;

  Endpoint(std::string host, uint16_t port) : host_(std::move(host)), port_(port) {}

  Endpoint(const Endpoint &other) noexcept : host_(other.host_), port_(other.port_) {}

  Endpoint(Endpoint &&other) noexcept : host_(std::move(other.host_)), port_(other.port_) {}

  virtual ~Endpoint() = default;

  asio_tcp::resolver::results_type Resolve(boost::asio::io_context &io_context) const {
    asio_tcp::resolver resolver(io_context);
    return resolver.resolve(GetHost(), std::to_string(GetPort()));
  }

  asio_tcp::endpoint GetTcpEndpoint() const {
    return {asio_tcp::v4(), static_cast<unsigned short >(port_)};
  }

  std::string GetHost() const {return host_;}

  uint16_t GetPort() const {return port_;}

  std::string ToString() const {
    return host_ + ":" + std::to_string(port_);
  }

private:
  // The host of this endpoint locates on.
  std::string host_;
  // The port of this endpoint.
  uint16_t port_;
};

} // namespace dousi

#endif
