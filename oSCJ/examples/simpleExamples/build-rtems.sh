#!/bin/bash

set -e
set -x



FIJI_HOME="../../../../"
SCJFLAGS="--scj --scj-scope-backing 310k --g-def-immortal-mem 500k --more-opt"   #700 scope, 500 imm
FIJIFLAGS="--max-threads 5"  # -v 1
RTEMSFLAGS="--target sparc-rtems4.9"

# rebuild SCJ.jar                                                                                
SCJ="../../scj/ri"
CWD=`pwd`
cd $SCJ && make scj.jar && cd $CWD

# HELLO HOME
HELLO=.
HELLO_BUILD=$HELLO/build


# CLEAN-UP
rm -rf $HELLO_BUILD
rm -rf hello
rm -rf hello.build

# COMPILE AND RUN
mkdir $HELLO_BUILD
javac -cp $FIJI_HOME/lib/scj.jar -d $HELLO_BUILD $HELLO/src/HelloWorld*.java
$FIJI_HOME/bin/fivmc -o hello-rtems  $SCJFLAGS $FIJIFLAGS $RTEMSFLAGS $HELLO_BUILD/*.class


sparc-rtems4.9-run hello-rtems
