#!/bin/bash

set -e
set -x

JAVA="/opt/jrts-2.2"

# CLEAN-UP
rm -rf build
rm -rf minicdx.jar
mkdir build

# COMPILE & JAR
find ./src -name *.java > list
$JAVA/bin/javac -cp $JAVA/jre/lib/rt2.jar -d build/ @list	
cd build/ && find . -name "*.class" | xargs jar cf ../minicdx.jar && cd ..
rm -rf list

	
	
# RUN:	
$JAVA/bin/java -cp minicdx.jar Launcher | tee miniCDx-com.cap



