#!/bin/bash

set -e
set -x


FIJI_HOME="../../../../"
SCJFLAGS="--scj --scj-scope-backing 900k --g-def-immortal-mem 900k"   #700 scope, 500 imm
FIJIFLAGS="--max-threads 5"  # -v 1

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
$FIJI_HOME/bin/fivmc -o hello  $SCJFLAGS  $FIJIFLAGS $HELLO_BUILD/*.class
./hello
