#ifndef _DOUSI_MASTER_H_
#define _DOUSI_MASTER_H_

#include "endpoint.h"

#include <unordered_map>
#include <string>

namespace dousi {
namespace master {

class Master {
public:

  void loop() {}

private:
  // The map that maps service name to its endpoint.
  std::unordered_map<std::string, Endpoint> endpoints_;
};

} // namespace master
} // namespace dousi

#endif
