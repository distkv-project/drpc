#include "gmock/gmock.h"
#include "gtest/gtest.h"

#include "master/server/master_server.h"
#include "master/client/master_client.h"
#include "common/endpoint.h"
#include "common/logging.h"

#include <boost/asio.hpp>

#include <thread>
#include <iostream>
#include <chrono>


using namespace dousi;
using namespace dousi::master;


/**
 * This BasicTest tests that the basic functionality between master server
 * and master client works fine.
 */
TEST(MasterTest, BasicTest) {
  boost::asio::io_context io_context(16);
  MasterServer master_server(io_context, "0.0.0.0", 9999);
  MasterClient master_client1(io_context, dousi::Endpoint("127.0.0.1", 9999));
  MasterClient master_client2(io_context, dousi::Endpoint("127.0.0.1", 9999));
  MasterClient master_client3(io_context, dousi::Endpoint("127.0.0.1", 9999));

  // Run io_context in a separated thread to make sure we can do other assertions later.
  std::thread t([&io_context]() { io_context.run(); });
  master_client1.RegisterService("adder1", "127.0.0.1:10001");
  master_client2.RegisterService("adder2", "127.0.0.1:10002");
  master_client3.RegisterService("adder3", "127.0.0.1:10003");
  master_client1.RegisterService("adder4", "127.0.0.1:10004");
  master_client1.RegisterService("adder5", "127.0.0.1:10005");
  master_client2.RegisterService("adder3", "0.0.0.0:0000");

  // TODO(qwang): This should be a `Waitutil()`.
  std::this_thread::sleep_for(std::chrono::seconds(2));
  auto endpoints = master_server.GetAllEndpoints();

  ASSERT_EQ(5, endpoints.size());
  ASSERT_EQ("127.0.0.1:10001", endpoints["adder1"]);
  ASSERT_EQ("127.0.0.1:10002", endpoints["adder2"]);
  ASSERT_EQ("0.0.0.0:0000", endpoints["adder3"]);
  ASSERT_EQ("127.0.0.1:10004", endpoints["adder4"]);
  ASSERT_EQ("127.0.0.1:10005", endpoints["adder5"]);

  io_context.stop();
  t.detach();
}

int main(int argc, char **argv) {
  ::testing::InitGoogleTest(&argc, argv);
  return RUN_ALL_TESTS();
}
