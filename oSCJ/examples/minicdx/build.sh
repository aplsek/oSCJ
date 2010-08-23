#!/bin/bash

set -e
set -x


#FIJI_HOME="../../../../"
FIJI_HOME="/Users/plsek/_work/fiji/fivm-pizloScope"
SCJFLAGS="--scj --scj-scope-backing 12m --g-def-immortal-mem 1m"
FIJIFLAGS=""  # -v 1

# CLEAN-UP
rm -rf build
mkdir build

# COMPILE & JAR
find ./cdx -name *.java > list
find ./simulator -name *.java >> list
find ./utils -name *.java >> list
javac -cp $FIJI_HOME/lib/scj.jar -d build/ @list	
cd build/ && find . -name "*.class" | xargs jar cf ../minicdx.jar && cd ..
rm -rf list

# 

# COMPILE FIJI
$FIJI_HOME/bin/fivmc -o minicdx $FIJIFLAGS $SCJFLAGS minicdx.jar --scj-safelet cdx.Level0Safelet
	
	
# RUN:	
./minicdx



