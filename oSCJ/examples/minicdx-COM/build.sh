#!/bin/bash

set -e
set -x

JAVA="/opt/jrts-2.2"

FIJI_HOME="../../../../"
#SCJFLAGS="--scj --scj-scope-backing 1500k --g-def-immortal-mem 2330k --g-scope-checks no --pollcheck-mode none"   #700 scope, 500 imm
#FIJIFLAGS="--max-threads 5 --more-opt"  # -v 1


# CLEAN-UP
rm -rf build
mkdir build

# COMPILE & JAR
find ./src -name *.java > list
javac -cp $JAVA/jre/lib/rt2.jar -d build/ @list	
cd build/ && find . -name "*.class" | xargs jar cf ../minicdx.jar && cd ..
rm -rf list

	
	
# RUN:	
$JAVA/bin/java -jar minicdx.jar -m Launcher | tee miniCDx-com.cap



