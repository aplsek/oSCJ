
#!/bin/bash

set -e
set -x


FIJI_HOME="../../../../"
SCJFLAGS="--scj --scj-scope-backing 300k --g-def-immortal-mem 530k --g-scope-checks no --pollcheck-mode none"   #700 scope, 500 imm
FIJIFLAGS="--max-threads 5 --more-opt"  # -v 1
RTEMSFLAGS="--target sparc-rtems4.9"

# rebuild SCJ.jar                                                                                  
SCJ="../../scj/ri"
CWD=`pwd`
cd $SCJ && make scj.jar && cd $CWD

# CLEAN-UP
rm -rf build
mkdir build

# COMPILE & JAR
find ./src -name *.java > list
javac -cp $FIJI_HOME/lib/scj.jar -d build/ @list	
cd build/ && find . -name "*.class" | xargs jar cf ../microBench.jar && cd ..
rm -rf list

# 

# COMPILE FIJI
$FIJI_HOME/bin/fivmc -o rtems-microBench-scj $FIJIFLAGS $RTEMSFLAGS $SCJFLAGS microBench.jar
	
	
# RUN:	
sparc-rtems4.9-run rtems-microBench-scj | tee rtems-microBench-scj.cap



