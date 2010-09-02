#!/bin/bash

set -e
set -x


FIJI_HOME="../../../../"
SCJFLAGS="--scj --scj-scope-backing 310k --g-def-immortal-mem 500k"   #700 scope, 500 imm
FIJIFLAGS="--max-threads 5"  # -v 1




# CLEAN-UP
rm -rf ./build
rm -rf emptyBench
rm -rf emptyBench.build
rm -rf emptyBench.jar



# COMPILE AND RUN
mkdir ./build

# COMPILE & JAR
find ./src -name *.java > list
javac -cp $FIJI_HOME/lib/scj.jar -d build/ @list	
cd build/ && find . -name "*.class" | xargs jar cf ../emptyBench.jar && cd ..
rm -rf list


$FIJI_HOME/bin/fivmc -o emptyBench -v 1  $SCJFLAGS  $FIJIFLAGS emptyBench.jar



./emptyBench > output.cap