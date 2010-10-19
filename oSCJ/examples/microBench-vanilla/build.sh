#!/bin/bash

set -e
set -x

# FIJI HOME
FIJI="../../../../"

FIJI_FLAGS="--g-def-max-mem=1000k -G cmr --pollcheck-mode portable --g-def-trigger=700k --more-opt
 --max-threads 5 --uniprocessor"
 
 
rm -rf build/
mkdir build/
rm -rf micro.jar
rm -rf micro*
 
 
 
 
find . -name *.java > list
echo "Compiling with javac..."
javac -classpath "$FIJI/runtimej/build:$FIJI/lib/fijicore.jar:$FIJI/lib/fivmcommon.jar:$FIJI/lib/fijirt.jar:$FIJI/lib/fivmr.jar" -d ./build @list
rm -rf list

# JAR
echo "Creating jar package"
cd build/ && find . -name "*.class" | xargs jar cf ../micro.jar
cd ..




# COMPILE FIJI
echo "Compiling micro"

$FIJI/bin/fivmc -o micro micro.jar --main micro.Main $FIJI_FLAGS

echo "running MICRO VANILLA"
sudo ./micro  | tee micro-gc.1000.700.cap
