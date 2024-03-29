#!/bin/bash

set -e
set -x


FIJI_HOME="../../../../"
SCJFLAGS="--scj --scj-scope-backing 1m --g-def-immortal-mem 1m"   #700 scope, 500 imm
FIJIFLAGS="--max-threads 5"  # -v 1

# CLEAN-UP
rm -rf build
mkdir build

# COMPILE & JAR
find ./src -name *.java > list
javac -cp $FIJI_HOME/lib/scj.jar -d build/ @list	
cd build/ && find . -name "*.class" | xargs jar cf ../ukExample.jar && cd ..
rm -rf list

# 

# COMPILE FIJI
$FIJI_HOME/bin/fivmc -o ukExample $FIJIFLAGS $SCJFLAGS ukExample.jar
	
	
# RUN:	
./ukExample



