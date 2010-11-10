#!/bin/bash

set -e
set -x

# FIJI HOME
FIJI="../../../../"

rm -rf build/
mkdir build/
rm -rf micro.jar

 
 
 
 
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

FIJI_FLAGS="--g-def-max-mem=1600k -G cmr --pollcheck-mode portable --g-def-trigger=1200k --more-opt --max-threads 5 --uniprocessor"
$FIJI/bin/fivmc -o micro.1600.1200 micro.jar --main micro.Main $FIJI_FLAGS

echo "running MICRO VANILLA"
sudo ./micro.1600.1200  | tee micro-gc.1600.1200.cap
