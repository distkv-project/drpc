#include "gmock/gmock.h"
#include "gtest/gtest.h"

#include "handy/handy.h"

#include <iostream>
using namespace std;
using namespace handy;

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

TEST_F(DemoTest, test_handy) {
EventBase base;
Signal::signal(SIGINT, [&]{ base.exit(); });
TcpServerPtr svr = TcpServer::startServer(&base, "", 2099);
exitif(svr == NULL, "start tcp server failed");
svr->onConnRead([](const TcpConnPtr& con) {
con->send(con->getInput());
});
base.loop();
}

int main(int argc, char **argv) {
  ::testing::InitGoogleTest(&argc, argv);
  return RUN_ALL_TESTS();
}
