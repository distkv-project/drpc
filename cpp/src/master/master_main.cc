#include "master.h"

#include <iostream>

using namespace dousi;

// The main entrance of master process.
int main(int argc, char *argv[]) {

  std::cout << "Master Main..." << std::endl;
  master::Master master;
  master.loop();

  return 0;
}
