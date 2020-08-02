#include "master/server/master_server.h"

#include <iostream>
#include <gflags/gflags.h>

using namespace dousi;

// TODO(qwang): These flags should be defined in a seprated header file.
DEFINE_string(host,  "127.0.0.1",  "The host that master server listens on.");
DEFINE_int32(port,  9999,  "The port that master server listens on.");

/**
 * The main entrance of master process.
 *
 * Usage:
 *   master_main --host=127.0.0.1 --port=9999
 */
int main(int argc, char *argv[]) {
  // TODO(qwang): This should be refactored as RAII.
  gflags::ParseCommandLineFlags(&argc, &argv, true);
  const std::string host = FLAGS_host;
  const auto port = static_cast<uint16_t>(FLAGS_port);
  gflags::ShutDownCommandLineFlags();

  boost::asio::io_context io_context;
  master::MasterServer master_server(io_context, host, port);
  io_context.run();

  return 0;
}
