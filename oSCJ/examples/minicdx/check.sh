#!/bin/bash

set -e
set -x

CHECKER=../../tools/checker/

M_PWD=`pwd`
cd $CHECKER && ant jar && cd $M_PWD

# get source
find ./cdx -name *.java -not -path "*bench*" -not -name "Launcher.java" > list
find ./simulator -name *.java >> list


# check:
$CHECKER/localbin/javac $1 -proc:only -cp $CHECKER/lib/scj.jar:$CHECKER/lib/scj-checker.jar -processor checkers.SCJChecker `cat list`