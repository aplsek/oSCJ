#!/bin/bash

set -e
set -x


FIJI_HOME="../../../../"
SCJFLAGS="--scj --scj-scope-backing 310k --g-def-immortal-mem 330k --g-scope-checks no --pollcheck-mode none"   #700 scope, 500 imm
FIJIFLAGS="--max-threads 5 --more-opt"  # -v 1

# CLEAN-UP
rm -rf build
mkdir build
rm -rf md5*

# COMPILE & JAR
find ./src -name *.java > list
javac -cp $FIJI_HOME/lib/scj.jar -d build/ @list	
cd build/ && find . -name "*.class" | xargs jar cf ../md5.jar && cd ..
rm -rf list

# 

# COMPILE FIJI
$FIJI_HOME/bin/fivmc -o md5 $FIJIFLAGS $SCJFLAGS md5.jar
	
	
# RUN:	
./md5 > md5-scj.cap



