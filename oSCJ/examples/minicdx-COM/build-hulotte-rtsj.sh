#!/bin/bash

set -e
set -x

# JAVA="/opt/jrts-2.2/"
JAVA="/home/floiret/Documents/Recherche/Applications/JAVA/JVMs/jrts-2.2_fcs"

CLASSPATH=$JAVA/jre/lib/rt2.jar:libs/fractal-api.jar:libs/fraclet-annotations-3.2.jar:libs/spoon-core-1.4.2.jar:libs/aval-1.0b-spoonlet.jar:libs/julia-runtime-2.5.2.jar:libs/juliac-runtime-comp-2.1-SNAPSHOT.jar:libs/juliac-runtime-oo.jar:libs/hulotte-component-library.jar:libs/koch.runtime-java1.5.jar:libs/hulotte-runtime-java1.5.jar

#SCJFLAGS="--scj --scj-scope-backing 1500k --g-def-immortal-mem 2330k --g-scope-checks no --pollcheck-mode none"   #700 scope, 500 imm

# CLEAN-UP
rm -rf build
mkdir build

# COMPILE & JAR
find ./src -name *.java > list1
find ./target/generated-sources -name *.java > list2

$JAVA/bin/javac -classpath $CLASSPATH -d build/ @list1
CLASSPATH+=:build
$JAVA/bin/javac -classpath $CLASSPATH -d build/ @list2
cd build/ && find . -name "*.class" | xargs jar cf ../minicdx.jar && cd ..
rm -rf list1 list2
	
# RUN:	 
$JAVA/bin/java -cp minicdx.jar:$CLASSPATH -Dfractal.provider=org.objectweb.fractal.juliac.runtime.comp.Juliac HulotteRTLauncher minicdx r #| tee miniCDx-com-scj.cap

