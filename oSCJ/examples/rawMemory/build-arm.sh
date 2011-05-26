#!/bin/bash

set -e
set -x


FIJI_HOME="../../../../"
TARGET="--target ARMv7"
SCJFLAGS="--scj-L0 --scj-scope-backing 3100k --g-def-immortal-mem 5000k --more-opt"   #700 scope, 500 imm
FIJIFLAGS="--max-threads 3 --no-inline --c-debug yes --pollcheck-mode none --g-scope-checks no"  # -v 1

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

if [ $# -eq 0 ]
then
    SAFELET="HelloWorld"
else
    SAFELET=$1
fi

# COMPILE AND RUN
mkdir $HELLO_BUILD
javac -cp $FIJI_HOME/lib/scj.jar:$FIJI_HOME/lib/fijicore.jar:$FIJI_HOME/lib/fivmr.jar:$FIJI_HOME/lib/fijirt.jar:$FIJI_HOME/lib/fivm.jar -d $HELLO_BUILD `find ./src/ -name "*.java"`

CLASS=`find $HELLO_BUILD -name "*.class" `
echo $CLASS


$FIJI_HOME/bin/fivmc -o serial-arm-scj --scj-safelet $SAFELET $TARGET  $SCJFLAGS  $FIJIFLAGS $CLASS

scp serial-arm-scj root@192.168.0.2:~/ales/