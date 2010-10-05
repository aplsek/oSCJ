#!/bin/bash

set -e
set -x


FIJI_HOME="../../../../"
SCJFLAGS="--scj --scj-scope-backing 7m --g-def-immortal-mem 7m"   #700 scope, 500 imm
FIJIFLAGS="--max-threads 5 --more-opt"  # -v 1


# CLEAN-UP
rm -rf build
mkdir build

# COMPILE & JAR
find . -name *.java > list
javac -cp $FIJI_HOME/lib/scj.jar -d build/ @list	
cd build/ && find . -name "*.class" | xargs jar cf ../micro.jar && cd ..
rm -rf list

# 

# COMPILE FIJI
$FIJI_HOME/bin/fivmc -o micro $FIJIFLAGS $SCJFLAGS --scj-safelet micro.MicroBench micro.jar
	
	
# RUN:	
./micro > output.cap
