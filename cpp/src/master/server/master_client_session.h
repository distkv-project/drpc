#ifndef _DOUSI_MASTER_CLIENT_SESSION_H_
#define _DOUSI_MASTER_CLIENT_SESSION_H_

#include "common/endpoint.h"
#include "common/logging.h"

#include "master/constants.h"
#include "master_server.h"

#include <msgpack.hpp>

#include <utility>
#include <memory>
#include <iostream>

namespace dousi {
namespace master {

class MasterClientSession : public std::enable_shared_from_this<MasterClientSession> {
public:
  MasterClientSession(MasterServer &master_server, asio_tcp::socket socket)
    : master_server_(master_server), socket_(std::move(socket)) {}

  virtual ~MasterClientSession() {
    socket_.close();
  }

  void Start() {
    boost::asio::post([this]() {DoReadType();});
  }

private:

  void DoReadType();

  void DoReadHeader(uint8_t type);

  void DoReadBody(uint8_t type, uint32_t body_size);

private:
  // The reference to the corresponding master server.
  MasterServer &master_server_;
  // The socket handle for the client that connected to this master server.
  asio_tcp::socket socket_;
};

}
} // namespace dousi

#endif
