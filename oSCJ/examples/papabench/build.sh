#!/bin/bash

set -e
set -x

FIJI_HOME="../../../../"
SCJFLAGS="--scj --scj-scope-backing 12m --g-def-immortal-mem 10m"
FIJIFLAGS=""  # -v 1

# CLEAN-UP
rm -rf build
mkdir build

# COMPILE & JAR
find ./src -name *.java > list
javac -cp $FIJI_HOME/lib/scj.jar -d build/ @list	
cd build/ && find . -name "*.class" | xargs jar cf ../papa.jar && cd ..
rm -rf list

# 

# COMPILE FIJI
$FIJI_HOME/bin/fivmc -o papa $FIJIFLAGS $SCJFLAGS papa.jar --scj-safelet papabench.scj.PapaBenchSCJLevel0Application
	
	
# RUN:	
./papa