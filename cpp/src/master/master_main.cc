#include "master.h"

#include <iostream>

using namespace dousi;

/**
 * The main entrance of master process.
 *
 * Usage:
 *   master_main --host=127.0.0.1 --port=9999
 */
int main(int argc, char *argv[]) {

  std::cout << "Master Main..." << std::endl;
  master::Master master("", 0);
  master.loop();

  return 0;
}
