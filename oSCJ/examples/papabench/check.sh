#!/bin/bash

set -e
set -x



CHECKER=../../tools/checker/

# get source
find . -name *.java > list


# check:
$CHECKER/localbin/checkers/binary/javac -proc:only -Awarns -cp ../../../../lib/scj.jar:$CHECKER/lib/scjChecker.jar -processor checkers.SCJChecker @list