#!/bin/bash


RUBY=ruby

set -e
set -x


FIJI_HOME="../../../../"
SCJFLAGS="--scj --scj-scope-backing 12m --g-def-immortal-mem 1m"
FIJIFLAGS="--max-threads 5"







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
#echo "Checking HelloWorld";
#javac -cp $FIJI_HOME/lib/scj.jar -d build/ src/HelloWorld*.java
#time $FIJI_HOME/bin/fivmc -o hello  $SCJFLAGS --scj-safelet HelloWorld build/HelloWorld*.class
#./hello 




# TEST 2 : EnterPrivateMemory
#echo "Checking EnterPrivateMemory";
#javac -cp $FIJI_HOME/lib/scj.jar -d build/ src/EnterPrivateTest*.java
#time $FIJI_HOME/bin/fivmc -o enterPrivate  $SCJFLAGS --scj-safelet EnterPrivateTest build/EnterPrivateTest*.class
#./enterPrivate





# TEST3  : Immortal
#echo "Checking ImmortalMemory";
javac -cp $FIJI_HOME/lib/scj.jar -d build/ src/TestImmortal*.java
time $FIJI_HOME/bin/fivmc -o immortalTest  $SCJFLAGS $FIJIFLAGS --scj-safelet TestImmortal build/TestImmortal*.class
./immortalTest 



# TEST4  : Immortal2
#echo "Checking ImmortalMemory";
#javac -cp $FIJI_HOME/lib/scj.jar -d build/ src/ImmortalTest2*.java
#time $FIJI_HOME/bin/fivmc -o immortalTest2  $SCJFLAGS build/ImmortalTest2*.class
#./immortalTest2


