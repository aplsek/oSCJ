#!/bin/bash

set -e
set -x

JAVA="/opt/jrts-2.2/"


#SCJFLAGS="--scj --scj-scope-backing 1500k --g-def-immortal-mem 2330k --g-scope-checks no --pollcheck-mode none"   #700 scope, 500 imm



# CLEAN-UP
rm -rf build
mkdir build

# COMPILE & JAR
find ./src -name *.java > list

$JAVA/bin/javac -classpath /opt/SUNWrtjv/jre/lib/rt2.jar -d build/ @list
cd build/ && find . -name "*.class" | xargs jar cf ../minicdx.jar && cd ..
rm -rf list
	
# RUN:	 
$JAVA/bin/java -cp minicdx.jar Launcher | tee miniCDx-com-scj.cap



