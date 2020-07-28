cmake_minimum_required(VERSION 3.11)
project(dousi_cpp_client)

set(CMAKE_CXX_STANDARD 14)
set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} "${CMAKE_SOURCE_DIR}/cmake/Modules")

set(DOUSI_CPP_CLIENT_SRC src/dousi_client.cc)

INCLUDE(ExternalProject)
INCLUDE(GtestExternalProject)
INCLUDE(BoostExternalProject)
INCLUDE_DIRECTORIES(SYSTEM ${GTEST_INCLUDE_DIR})
INCLUDE_DIRECTORIES(SYSTEM ${BOOST_INCLUDE_DIR})
LINK_DIRECTORIES(${BOOST_STATIC_LIB_DIR})

# demo_test
add_executable(demo_test src/tests/demo_test.cc)
add_dependencies(demo_test googletest_ep boost_ep)
target_link_libraries(demo_test ${GTEST_STATIC_LIB} ${GTEST_MAIN_STATIC_LIB} ${GMOCK_MAIN_STATIC_LIB} pthread)
