CMAKE_MINIMUM_REQUIRED(VERSION 3.11)
PROJECT(dousi_cpp)

set(CMAKE_CXX_STANDARD 14)
set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} "${CMAKE_SOURCE_DIR}/cmake/Modules")

set(DOUSI_CPP_CLIENT_SRC src/dousi_client.cc)

INCLUDE(ExternalProject)
INCLUDE(GtestExternalProject)
INCLUDE(BoostExternalProject)
INCLUDE(GflagsExternalProject)
INCLUDE(MsgpackExternalProject)
INCLUDE_DIRECTORIES(src)
INCLUDE_DIRECTORIES(SYSTEM ${GTEST_INCLUDE_DIR})
INCLUDE_DIRECTORIES(SYSTEM ${BOOST_INCLUDE_DIR})
INCLUDE_DIRECTORIES(SYSTEM ${GFLAGS_INCLUDE_DIR})
INCLUDE_DIRECTORIES(SYSTEM ${MSGPACK_INCLUDE_DIR})
LINK_DIRECTORIES(${BOOST_STATIC_LIB_DIR})

# master_server_static_library
ADD_LIBRARY(master_server src/master/master_server.cc src/master/master_client_session.cc)
ADD_DEPENDENCIES(master_server boost_ep gflags_ep msgpack_ep)
TARGET_LINK_LIBRARIES(master_server pthread)

# master_client_static_library
ADD_LIBRARY(master_client src/master/master_client.cc)
ADD_DEPENDENCIES(master_client boost_ep msgpack_ep)
TARGET_LINK_LIBRARIES(master_client pthread)


# master_server_main binary
ADD_EXECUTABLE(master_server_main src/master/master_server_main.cc)
ADD_DEPENDENCIES(master_server_main master_server boost_ep gflags_ep msgpack_ep)
TARGET_LINK_LIBRARIES(master_server_main master_server ${GFLAGS_STATIC_LIB} pthread)

# master client binary
ADD_EXECUTABLE(master_client_main src/master/master_client_main.cc)
ADD_DEPENDENCIES(master_client_main master_client boost_ep msgpack_ep)
TARGET_LINK_LIBRARIES(master_client_main master_client ${GFLAGS_STATIC_LIB} pthread)

# master_test
ADD_EXECUTABLE(master_test src/master/master_test.cc)
ADD_DEPENDENCIES(master_test googletest_ep master_server master_client)
TARGET_LINK_LIBRARIES(master_test master_server master_client
        ${GTEST_STATIC_LIB} ${GTEST_MAIN_STATIC_LIB} ${GMOCK_MAIN_STATIC_LIB} pthread)
