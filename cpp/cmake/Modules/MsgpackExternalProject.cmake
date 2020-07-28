# msgpack external project
# target:
#  - msgpack_ep
# defines:
#  - MSGPACK_INCLUDE_DIR

set(MSGPACK_VERSION "3.3.0")
set(DOWNLOAD_URL "https://github.com/msgpack/msgpack-c/archive/cpp-${MSGPACK_VERSION}.tar.gz")
set(MSGPACK_URL_MD5 bb8b3173b4bf864d96dac1532ecf781c)

if(CMAKE_BUILD_TYPE)
    string(TOUPPER ${CMAKE_BUILD_TYPE} UPPERCASE_BUILD_TYPE)
endif()

ExternalProject_Add(msgpack_ep
        PREFIX external/msgpack
        URL ${DOWNLOAD_URL}
        URL_MD5 ${MSGPACK_URL_MD5})

set(MSGPACK_INCLUDE_DIR ${CMAKE_CURRENT_BINARY_DIR}/external/msgpack/src/msgpack_ep/include)
