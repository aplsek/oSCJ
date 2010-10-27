#!/bin/bash

set -e
set -x


FIJI_HOME="../../../../"
SCJFLAGS="--scj --scj-scope-backing 500k --g-def-immortal-mem 500k --g-scope-checks no --pollcheck-mode none"   #700 scope, 500 imm
FIJIFLAGS="--max-threads 5 --more-opt --uniprocessor"  # -v 1
RTEMSFLAGS="--target sparc-rtems4.9"


# rebuild SCJ.jar                                                                                  
SCJ="../../scj/ri"
CWD=`pwd`
cd $SCJ && make scj.jar && cd $CWD

# CLEAN-UP
rm -rf build
mkdir build
rm -rf md5*

# COMPILE & JAR
find ./src -name *.java > jlist
javac -cp $FIJI_HOME/lib/scj.jar -d build/ @jlist
cd build/ && find . -name "*.class" | xargs jar cf ../list.jar && cd ..
rm -rf jlist

# 

# COMPILE FIJI
$FIJI_HOME/bin/fivmc -o list-rtems $FIJIFLAGS $SCJFLAGS $RTEMSFLAGS md5.jar 


echo "compilation completed"
# RUN:
#sudo ./md5-rtems | tee md5-scj.cap

# RUN:
sparc-rtems4.9-run list-rtems | tee rtems-list-scj.cap

