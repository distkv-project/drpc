
#include "master/client/master_client.h"

#include <string>
#include <thread>

#include <msgpack.hpp>

int main(int argc, char *argv[]) {
  boost::asio::io_context io_context;
  dousi::master::MasterClient client(io_context, dousi::Endpoint("127.0.0.1", 9999));

  const std::string service_name = "adder";
  const std::string service_addr = "10.8.8.12:12202";

  client.RegisterService(service_name, service_addr);

  std::thread t([&io_context](){ io_context.run(); });
  t.join();

  return 0;
}
