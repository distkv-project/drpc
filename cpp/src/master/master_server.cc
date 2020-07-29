#include "master_server.h"

namespace dousi {
namespace master {

void MasterServer::DoAccept() {
  acceptor_.async_accept([this](boost::system::error_code error_code, asio_tcp::socket socket) {
    if (!error_code) {
      DOUSI_LOG(INFO) << "Succeed to accepted a connection.";
      auto client_session = std::make_shared<MasterClientSession>(std::move(socket));
      sessions_.push_back(client_session);
      client_session->Start();
    }
    DoAccept();
  });
}

} // namespace master
} // namespace dousi
