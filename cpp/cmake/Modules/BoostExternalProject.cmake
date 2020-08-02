# boost external project
# target:
#  - boost_ep
# defines:
#  - BOOST_HOME
#  - BOOST_STATIC_LIB
#  - BOOST_SHARED_LIB
#  - BOOST_INCLUDE_DIR

set(BOOST_VERSION "1.73.0")
set(BOOST_URL_MD5 4036cd27ef7548b8d29c30ea10956196)

# This is used for speeding the boost downloading up in China.
if ($ENV{DOUSI_BUILD_PROXY})
    message("DOUSI_BUILD_PROXY is enabled, so we will download boost from Aliyun.")
    set(DOWNLOAD_URL "https://wq-boost.oss-cn-beijing.aliyuncs.com/boost_1_73_0.tar.gz")
else()
    message("DOUSI_BUILD_PROXY is disabled, so we will download boost from dl.rog .")
    set(DOWNLOAD_URL "https://dl.bintray.com/boostorg/release/1.73.0/source/boost_1_73_0.tar.gz")
endif()


if(CMAKE_BUILD_TYPE)
    string(TOUPPER ${CMAKE_BUILD_TYPE} UPPERCASE_BUILD_TYPE)
endif()

ExternalProject_Add(boost_ep
        PREFIX external/boost
        URL ${DOWNLOAD_URL}
        URL_MD5 ${BOOST_URL_MD5}
        BUILD_IN_SOURCE 1
        BUILD_BYPRODUCTS ${BOOST_STATIC_LIB}
        CONFIGURE_COMMAND bash -c "./bootstrap.sh"
        BUILD_COMMAND bash -c "./b2 --with-system --with-thread --with-date_time --with-regex --with-serialization stage"
        INSTALL_COMMAND "")

set(BOOST_CURRENT_DIR ${CMAKE_CURRENT_BINARY_DIR}/external/boost/src/boost_ep)

set(BOOST_HOME ${BOOST_CURRENT_DIR})
set(BOOST_INCLUDE_DIR ${BOOST_HOME})
set(BOOST_STATIC_LIB_DIR ${BOOST_HOME}/stage/lib)
