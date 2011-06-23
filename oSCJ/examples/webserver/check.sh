#!/bin/bash

set -e
set -x


CHECKER_HOME=../../tools/checker/

M_PWD=`pwd`cd $CHECKER && ant jar && cd $M_PWD

$CHECKER_HOME/localbin/javac -proc:only -cp $CHECKER_HOME/lib/scj.jar:$CHECKER_HOME/lib/scj-checker.jar  -processor checkers.SCJChecker "$@" `find ./src -name "*.java"`


