cmake_minimum_required(VERSION 3.11)
project(drpc_cpp_client)

set(CMAKE_CXX_STANDARD 14)
set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} "${CMAKE_SOURCE_DIR}/cmake/Modules")

set(DRPC_CPP_CLIENT_SRC src/drpc_client.cc)

include(ExternalProject)
include(GtestExternalProject)
include(HandyExternalProject)

include_directories(SYSTEM ${HANDY_INCLUDE_DIR})
include_directories(SYSTEM ${GTEST_INCLUDE_DIR})

add_library(drpc_client ${DRPC_CPP_CLIENT_SRC})
add_library(drpc_client_shared SHARED ${DRPC_CPP_CLIENT_SRC})

target_link_libraries(drpc_client ${HANDY_STATIC_LIB})

# demo_test
add_executable(demo_test src/tests/demo_test.cc)
add_dependencies(demo_test googletest_ep handy_ep)
target_link_libraries(demo_test drpc_client ${GTEST_STATIC_LIB} ${GTEST_MAIN_STATIC_LIB} ${GMOCK_MAIN_STATIC_LIB} pthread)
