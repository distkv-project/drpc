#include "master/server/master_server.h"
#include "master/server/master_client_session.h"


namespace dousi {
namespace master {

void MasterServer::DoAccept() {
  acceptor_.async_accept([this](boost::system::error_code error_code, asio_tcp::socket socket) {
    if (!error_code) {
      DOUSI_LOG(INFO) << "Succeed to accepted a connection.";
      auto client_session = std::make_shared<MasterClientSession>(*this, std::move(socket));
      sessions_.push_back(client_session);
      client_session->Start();
    }
    DoAccept();
  });
}

void MasterServer::AddServiceRegistration(const std::string &service_name, const std::string &service_address) {
  endpoints_[service_name] = service_address;
}

} // namespace master
} // namespace dousi
