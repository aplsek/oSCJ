#!/bin/bash

set -e
set -x


FIJI_HOME="../../../../"
SCJFLAGS="--scj --scj-scope-backing 1500k --g-def-immortal-mem 2330k --g-scope-checks yes --pollcheck-mode none"   #700 scope, 500 imm
FIJIFLAGS="--max-threads 3 --more-opt"  # -v 1

# rebuild SCJ.jar                 
SCJ="../../scj/ri"
CWD=`pwd`
cd $SCJ && make scj.jar && cd $CWD


# COMPILE & JAR
find ./cdx -name *.java > list
find ./simulator -name *.java >> list
javac -cp $FIJI_HOME/lib/scj.jar -d build/ @list	
cd build/ && find . -name "*.class" | xargs jar cf ../minicdx.jar && cd ..
rm -rf list

# 

# COMPILE FIJI
$FIJI_HOME/bin/fivmc -o minicdx-scope --scj-safelet cdx.Level0Safelet $FIJIFLAGS $SCJFLAGS minicdx.jar
	
	
# RUN:	
#sudo ./minicdx | tee miniCDx-scj.cap



