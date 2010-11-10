#!/bin/bash

set -e
set -x

JAVA="/opt/jrts-2.2/"

#CLEANUP
echo "Cleaning class files..."
find build/ -name "*.class" | xargs rm -f
rm -rf cdj.jar
rm -rf cdj_x86
rm -rf build/
mkdir build/

find . -name *.java > list
$JAVA/bin/javac -classpath $JAVA/jre/lib/rt2.jar -d ./build @list
rm -rf list

# JAR
echo "Creating jar package"
cd build/ && find . -name "*.class" | xargs jar cf ../cdj.jar
cd ..


$JAVA/bin/java -cp cdj.jar:$JAVA/jre/lib/rt2.jar heap.Main

