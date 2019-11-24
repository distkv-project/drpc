# handy external project
# target:
#  - handy_ep
# defines:
#  - HANDY_HOME
#  - HANDY_STATIC_LIB
#  - HANDY_SHARED_LIB
#  - HANDY_INCLUDE_DIR

set(HANDY_VERSION "0.1.1")
set(DOWNLOAD_URL "https://github.com/jovany-wang/handy/archive/${HANDY_VERSION}.tar.gz")
set(URL_MD5 5d78cd32168a0a38a805f17b558f8ac5)

set(HANDY_HOME ${CMAKE_CURRENT_BINARY_DIR}/external/handy-install)
set(HANDY_INCLUDE_DIR ${HANDY_HOME}/include)
set(HANDY_STATIC_LIB ${HANDY_HOME}/lib${LIB_SUFFIX}/${CMAKE_STATIC_LIBRARY_PREFIX}handy${CMAKE_STATIC_LIBRARY_SUFFIX})

if(CMAKE_BUILD_TYPE)
    string(TOUPPER ${CMAKE_BUILD_TYPE} UPPERCASE_BUILD_TYPE)
endif()

ExternalProject_Add(handy_ep
        PREFIX external/handy
        URL ${DOWNLOAD_URL}
        URL_MD5 ${HANDY_URL_MD5}
        BUILD_IN_SOURCE 1
        BUILD_BYPRODUCTS ${HANDY_STATIC_LIB}
        CONFIGURE_COMMAND ""
        BUILD_COMMAND bash -c "make"
        INSTALL_COMMAND "")

set(HANDY_CURRENT_DIR ${CMAKE_CURRENT_BINARY_DIR}/external/handy/src/handy_ep)


# Install include files
ExternalProject_Add_Step(handy_ep handy_ep_install_include
        COMMAND bash -c "mkdir -p ${HANDY_INCLUDE_DIR} && cp -rf ${HANDY_CURRENT_DIR}/handy/ ${HANDY_INCLUDE_DIR}/handy"
        DEPENDEES build)

# Install lib file
ExternalProject_Add_Step(handy_ep handy_ep_install_lib
        COMMAND bash -c "mkdir -p ${HANDY_HOME}/lib${LIB_SUFFIX}/ && cp ${HANDY_CURRENT_DIR}/libhandy.a ${HANDY_STATIC_LIB}"
        DEPENDEES build)
