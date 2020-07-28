# gflags external project
# target:
#  - gflags_ep
# defines:
#  - GFLAGS_HOME
#  - GFLAGS_STATIC_LIB
#  - GFLAGS_NOTHREADS_STATIC_LIB (Not provided now)
#  - GFLAGS_INCLUDE_DIR

set(GFLAGS_VERSION "2.2.2")
set(DOWNLOAD_URL "https://github.com/gflags/gflags/archive/v${GFLAGS_VERSION}.tar.gz")
set(GFLAGS_URL_MD5 1a865b93bacfa963201af3f75b7bd64c)

if(CMAKE_BUILD_TYPE)
    string(TOUPPER ${CMAKE_BUILD_TYPE} UPPERCASE_BUILD_TYPE)
endif()

ExternalProject_Add(gflags_ep
        PREFIX external/gflags
        URL ${DOWNLOAD_URL}
        URL_MD5 ${GFLAGS_URL_MD5}
        BUILD_IN_SOURCE 1
        CONFIGURE_COMMAND bash -c "mkdir bld && cd bld && ${CMAKE_COMMAND} .."
        BUILD_COMMAND bash -c "cd bld && make"
        INSTALL_COMMAND "")

set(GFLAGS_CURRENT_DIR ${CMAKE_CURRENT_BINARY_DIR}/external/gflags/src/gflags_ep)

set(GFLAGS_HOME ${GFLAGS_CURRENT_DIR}/bld)
set(GFLAGS_INCLUDE_DIR ${GFLAGS_HOME}/include)
set(GFLAGS_STATIC_LIB ${GFLAGS_HOME}/lib${LIB_SUFFIX}/${CMAKE_STATIC_LIBRARY_PREFIX}gflags${CMAKE_STATIC_LIBRARY_SUFFIX})
set(GFLAGS_NOTHREADS_STATIC_LIB ${GFLAGS_HOME}/lib${LIB_SUFFIX}/${CMAKE_STATIC_LIBRARY_PREFIX}gflags_nothreads${CMAKE_STATIC_LIBRARY_SUFFIX})
