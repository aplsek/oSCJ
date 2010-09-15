#!/bin/bash

set -e
set -x

BUILD=./build

rm -rf $BUILD
mkdir $BUILD

find ./src -name "*.java" | xargs javac -cp ../../../../lib/scj.jar:lib/jsr308-all.jar:lib/checkers.jar: -d $BUILD

