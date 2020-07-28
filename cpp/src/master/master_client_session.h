#ifndef _DOUSI_MASTER_CLIENT_SESSION_H_
#define _DOUSI_MASTER_CLIENT_SESSION_H_

#include <utility>
#include <memory>

#include "endpoint.h"

namespace dousi {
namespace master {

class MasterClientSession : public std::enable_shared_from_this<MasterClientSession> {
public:
  MasterClientSession(asio_tcp::socket socket) : socket_(std::move(socket)) {}

private:
  // The socket handle for the client that connected to this master server.
  asio_tcp::socket socket_;
};

}
} // namespace dousi

#endif
