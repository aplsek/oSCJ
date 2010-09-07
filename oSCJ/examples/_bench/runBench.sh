#!/bin/bash

set -e
set -x


MINICDJ=../minicdx
CDJ=../cdj-vanilla-fiji-v1.2

EMPTYBENCH=../emptyBench

# Compile and RUN 
cd $MINICDJ && ./build.sh 


# Compile and RUN
cd $CDJ && ./build.sh


# Compile and RUN
cd $EMPTYBENCH && ./build.sh


# plot results
./bench.sh
