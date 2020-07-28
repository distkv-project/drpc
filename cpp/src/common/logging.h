#ifndef _DOUSI_COMMON_LOGGING_H_
#define _DOUSI_COMMON_LOGGING_H_

#include <iostream>
#include <boost/asio.hpp>

namespace dousi {

enum LOG_LEVEL {
  DEBUG,
  INFO,
  WARNING,
  FATAL
};

class DousiLoggerInstance {
public:

  explicit DousiLoggerInstance(LOG_LEVEL log_level): log_level_(log_level) { }

  ~DousiLoggerInstance() { std::cout << std::endl; }

  DousiLoggerInstance &operator<<(const std::string &str) {
    std::cout << str;
    return *this;
  }

  DousiLoggerInstance &operator<<(boost::system::error_code error_code) {
    std::cout << error_code;
    return *this;
  }

  template<typename BasicType>
  DousiLoggerInstance &operator<<(BasicType value) {
    std::cout << value;
    return *this;
  }

private:
  LOG_LEVEL log_level_;
};

inline DousiLoggerInstance GetLoggerInstanceWithLevel(LOG_LEVEL log_level) {
  return DousiLoggerInstance(log_level);
}

#define DOUSI_LOG(LOG_LEVEL) GetLoggerInstanceWithLevel(LOG_LEVEL)


} // namespace dousi

#endif
