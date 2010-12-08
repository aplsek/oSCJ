#!/bin/bash

set -e
set -x

# FIJI HOME
FIJI=../../../../

#CLEANUP
echo "Cleaning class files..."
find build/ -name "*.class" | xargs rm -f
rm -rf *.jar
rm -rf hello
rm -rf build/
mkdir build/

find . -name *.java > list
echo "Compiling with javac..."
javac -classpath "$FIJI/runtimej/build:$FIJI/lib/fijicore.jar:$FIJI/lib/fivmcommon.jar:$FIJI/lib/fijirt.jar:$FIJI/lib/fivmr.jar" -d ./build @list
rm -rf list

# JAR
echo "Creating jar package"
cd build/ && find . -name "*.class" | xargs jar cf ../hello.jar
cd ..



FIJI_FLAGS="--g-def-max-mem=1200k --g-def-trigger=850k --more-opt --max-threads 5 --uniprocessor"
$FIJI/bin/fivmc -o hello hello.jar $FIJI_FLAGS 

sudo ./hello | tee hello.cap

echo "All done."


