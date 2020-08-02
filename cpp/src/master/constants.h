#ifndef _DOUSI_MASTER_PROTOCOL_DEF_H_
#define _DOUSI_MASTER_PROTOCOL_DEF_H_

namespace dousi {
namespace master {

class ProtocolConstants {
public:
  static constexpr uint8_t MESSAGE_TYPE_REGISTER_SERVICE = 0x00;
  static constexpr uint8_t MESSAGE_TYPE_FETCH_SERVICE = 0x01;

};

} // namespace master
} // namespace dousi

#endif
