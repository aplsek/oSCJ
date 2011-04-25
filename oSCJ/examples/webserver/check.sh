#!/bin/bash

set -e
set -x


CHECKER_HOME=../../tools/checker/


$CHECKER_HOME/localbin/javac -proc:only -cp $CHECKER_HOME/lib/scj.jar:$CHECKER_HOME/lib/scj-checker.jar  -processor checkers.SCJChecker "$@" `find ./src -name "*.java"`


