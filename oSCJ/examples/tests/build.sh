#!/bin/bash


RUBY=ruby

set -e
set -x


FIJI_HOME="/Users/plsek/_work/fiji/fivm-pizloScope"
SCJFLAGS="--scj --scj-scope-backing 12m --g-def-immortal-mem 1m"








# CLEAN-UP
rm -rf build
rm -rf hello.build
rm -rf enterPrivate.build
rm -rf hello
rm -rf enterPrivate
rm -rf log


# INIT
mkdir build


# TEST 1 : HelloWorld
echo "Checking HelloWorld";
javac -cp $FIJI_HOME/lib/scj.jar -d build/ src/HelloWorld*.java
time $FIJI_HOME/bin/fivmc -o hello  $SCJFLAGS --scj-safelet HelloWorld build/HelloWorld*.class
./hello | tee log
$RUBY localbin/verifysuccess-lines "set-up." "HelloWorld." "HelloWorld." "cleanUp." "teardown." < log





# TEST 2 : EnterPrivateMemory
echo "Checking EnterPrivateMemory";
javac -cp $FIJI_HOME/lib/scj.jar -d build/ src/EnterPrivateTest*.java
time $FIJI_HOME/bin/fivmc -o enterPrivate  $SCJFLAGS --scj-safelet EnterPrivateTest build/EnterPrivateTest*.class
./enterPrivate | tee log
$RUBY localbin/verifysuccess-lines "EnterPrivateMemory OK." "EnterPrivateMemory OK." < log





# TEST 3 : ...

