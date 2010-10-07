
#!/bin/bash

set -e
set -x


FIJI_HOME="../../../../"
SCJFLAGS="--scj --scj-scope-backing 310k --g-def-immortal-mem 330k --g-scope-checks no --pollcheck-mode none"   #700 scope, 500 imm
FIJIFLAGS="--max-threads 5 --more-opt"  # -v 1
RTEMSFLAGS="--target sparc-rtems4.9"



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
$FIJI_HOME/bin/fivmc -o minicdx-rtems $FIJIFLAGS $RTEMSFLAGS $SCJFLAGS minicdx.jar
	
	
# RUN:	
sparc-rtems4.9-run minicdx-rtems > output.cap



