#!/bin/bash

set -e
set -x


FIJI_HOME="/Users/plsek/_work/fiji/fivm-pizloScope"
SCJFLAGS="--scj --scj-scope-backing 12m --g-def-immortal-mem 1m"

# HELLO HOME
HELLO=.
HELLO_BUILD=$HELLO/build


# CLEAN-UP
rm -rf $HELLO_BUILD
rm -rf myhello
rm -rf myhello.build

# COMPILE AND RUN
mkdir $HELLO_BUILD
javac -cp $FIJI_HOME/lib/scj.jar -d $HELLO_BUILD $HELLO/src/MyHelloWorld*.java
$FIJI_HOME/bin/fivmc -o myhello  $SCJFLAGS --scj-safelet MyHelloWorld $HELLO_BUILD/*.class
./myhello MyHelloWorld