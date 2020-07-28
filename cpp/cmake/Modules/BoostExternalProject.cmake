# boost external project
# target:
#  - boost_ep
# defines:
#  - BOOST_HOME
#  - BOOST_STATIC_LIB
#  - BOOST_SHARED_LIB
#  - BOOST_INCLUDE_DIR

set(BOOST_VERSION "1.73.0")
set(DOWNLOAD_URL "https://dl.bintray.com/boostorg/release/1.73.0/source/boost_1_73_0.tar.gz")
set(URL_MD5 9995e192e68528793755692917f9eb6422f3052a53c5e13ba278a228af6c7acf)

set(BOOST_HOME ${CMAKE_CURRENT_BINARY_DIR}/external/boost-install)
set(BOOST_INCLUDE_DIR ${BOOST_HOME}/include)
set(BOOST_STATIC_LIB ${BOOST_HOME}/lib${LIB_SUFFIX}/${CMAKE_STATIC_LIBRARY_PREFIX}boost${CMAKE_STATIC_LIBRARY_SUFFIX})

if(CMAKE_BUILD_TYPE)
    string(TOUPPER ${CMAKE_BUILD_TYPE} UPPERCASE_BUILD_TYPE)
endif()

ExternalProject_Add(boost_ep
        PREFIX external/boost
        URL ${DOWNLOAD_URL}
        URL_MD5 ${BOOST_URL_MD5}
        BUILD_IN_SOURCE 1
        BUILD_BYPRODUCTS ${BOOST_STATIC_LIB}
        CONFIGURE_COMMAND ""
        BUILD_COMMAND bash -c "make"
        INSTALL_COMMAND "")

set(BOOST_CURRENT_DIR ${CMAKE_CURRENT_BINARY_DIR}/external/boost/src/boost_ep)


## Install include files
#ExternalProject_Add_Step(boost_ep boost_ep_install_include
#        COMMAND bash -c "mkdir -p ${HANDY_INCLUDE_DIR} && cp -rf ${HANDY_CURRENT_DIR}/handy/ ${HANDY_INCLUDE_DIR}/handy"
#        DEPENDEES build)
#
## Install lib file
#ExternalProject_Add_Step(handy_ep handy_ep_install_lib
#        COMMAND bash -c "mkdir -p ${HANDY_HOME}/lib${LIB_SUFFIX}/ && cp ${HANDY_CURRENT_DIR}/libhandy.a ${HANDY_STATIC_LIB}"
#        DEPENDEES build)
