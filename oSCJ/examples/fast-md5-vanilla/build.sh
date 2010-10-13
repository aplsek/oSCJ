#!/bin/bash

set -e
set -x

# FIJI HOME
FIJI="../../../../"

FIJI_FLAGS="--g-def-max-mem=1M -G cmr --pollcheck-mode portable --g-def-trigger=700k --more-opt
 --max-threads 5 -v 1"
 
 
rm -rf build/
mkdir build/
rm -rf md5.jar
rm -rf md5*
 
 
 
 
find src/ -name *.java > list
echo "Compiling with javac..."
#javac src/heap/Main.java -classpath "$FIJI/runtimej/build:$FIJI/lib/fijicore.jar:$FIJI/lib/fivmcommon.jar:$FIJI/lib/fijirt.jar:$FIJI/lib/fivmr.jar" -d ./build
javac -classpath "$FIJI/runtimej/build:$FIJI/lib/fijicore.jar:$FIJI/lib/fivmcommon.jar:$FIJI/lib/fijirt.jar:$FIJI/lib/fivmr.jar" -d ./build @list
rm -rf list

# JAR
echo "Creating jar package"
cd build/ && find . -name "*.class" | xargs jar cf ../md5.jar
cd ..




# COMPILE FIJI
echo "Compiling md5"

$FIJI/bin/fivmc -o md5-gc md5.jar --main com.twmacinta.util.MD5InputStream $FIJI_FLAGS

echo "running MICRO VANILLA"
./md5-gc | tee md5-gc.1000.700.cap
