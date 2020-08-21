#ifndef _DOUSI_COMMON_LOGGING_H_
#define _DOUSI_COMMON_LOGGING_H_

#include <iostream>
#include <boost/asio.hpp>

namespace drpc {

enum LOG_LEVEL {
  DEBUG,
  INFO,
  WARNING,
  FATAL
};

class DrpcLoggerInstance {
public:

  explicit DrpcLoggerInstance(LOG_LEVEL log_level): log_level_(log_level) { }

  ~DrpcLoggerInstance() { std::cout << std::endl; }

  DrpcLoggerInstance &operator<<(const std::string &str) {
    std::cout << str;
    return *this;
  }

  DrpcLoggerInstance &operator<<(boost::system::error_code error_code) {
    std::cout << error_code;
    return *this;
  }

  template<typename BasicType>
  DrpcLoggerInstance &operator<<(BasicType value) {
    std::cout << value;
    return *this;
  }

private:
  LOG_LEVEL log_level_;
};

inline DrpcLoggerInstance GetLoggerInstanceWithLevel(LOG_LEVEL log_level) {
  return DrpcLoggerInstance(log_level);
}

#define DOUSI_LOG(LOG_LEVEL) GetLoggerInstanceWithLevel(LOG_LEVEL)


} // namespace drpc

#endif
