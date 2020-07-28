
#include "master_client.h"

#include <string>
#include <thread>

#include <msgpack.hpp>

int main(int argc, char *argv[]) {

  const std::string service_name = "adder";
  const std::string service_addr = "10.8.8.12:12201";
  msgpack::type::tuple<std::string, std::string> message(service_name, service_addr);

  // Any class that implements `write(const char *, size_t)` can be a buffer.
  std::stringstream buffer;
  msgpack::pack(buffer, message);
  // send the buffer ...
  buffer.seekg(0);


  boost::asio::io_context io_context;
  dousi::master::MasterClient client(io_context, dousi::Endpoint("127.0.0.1", 9999));
  client.Write(buffer.str());

  std::thread t([&io_context](){ io_context.run(); });
  t.join();

  return 0;
}
