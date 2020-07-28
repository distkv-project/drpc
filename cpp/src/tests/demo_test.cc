#include "gmock/gmock.h"
#include "gtest/gtest.h"

#include <iostream>
#include <boost/asio.hpp>

using namespace std;

class DemoTest : public ::testing::Test {
 public:
  void SetUp() {
    std::cout << "SetUp..." << std::endl;
  }

};

TEST_F(DemoTest, f) {
  for (size_t i = 0; i < 5; ++i) {
    ASSERT_EQ(i, i);
  }
}


int main(int argc, char **argv) {
  boost::asio::io_context io;
  std::cout << "Before Hello, world!" << std::endl;
  boost::asio::steady_timer t(io, boost::asio::chrono::seconds(10));
  t.wait();
  std::cout << "Hello, world!" << std::endl;
  return 0;
  ::testing::InitGoogleTest(&argc, argv);
  return RUN_ALL_TESTS();
}
