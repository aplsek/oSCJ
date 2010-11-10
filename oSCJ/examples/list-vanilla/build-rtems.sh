#!/bin/bash

set -e
set -x

# FIJI HOME
FIJI="../../../../"

FIJI_FLAGS="--g-def-max-mem=1M -G cmr --pollcheck-mode portable --g-def-trigger=700k --more-opt
 --max-threads 5 --uniprocessor"
RTEMSFLAGS="--target sparc-rtems4.9"
  
 
rm -rf build/
mkdir build/
rm -rf list*
 
 
 
 
find src/ -name *.java > jlist
echo "Compiling with javac..."
#javac src/heap/Main.java -classpath "$FIJI/runtimej/build:$FIJI/lib/fijicore.jar:$FIJI/lib/fivmcommon.jar:$FIJI/lib/fijirt.jar:$FIJI/lib/fivmr.jar" -d ./build
javac -classpath "$FIJI/runtimej/build:$FIJI/lib/fijicore.jar:$FIJI/lib/fivmcommon.jar:$FIJI/lib/fijirt.jar:$FIJI/lib/fivmr.jar" -d ./build @jlist
rm -rf jlist

# JAR

cd build/ && find . -name "*.class" | xargs jar cf ../list.jar
cd ..




# COMPILE FIJI
$FIJI/bin/fivmc -o list-rtems-gc list.jar --main list.MyMain $FIJI_FLAGS $RTEMSFLAGS

sudo ./list-rtems-gc | tee rtems-list-CMR.1000.700.cap
