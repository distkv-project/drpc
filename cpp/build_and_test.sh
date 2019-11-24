
# This script is used to build the cpp client and test them.

# TODO(qwang): Use SCRIPT_DIR here.

set +x

mkdir -p build && pushd build
cmake ..
make -j 8

# run all tests
./demo_test

popd
