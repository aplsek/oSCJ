#!/bin/bash

set -e
set -x

SCJ=../minicdx
FIJI=../cdj-vanilla-fiji-v1.2/bench/
EMPTY_SCJ=../emptyBench/

timestamp=`date +"%m%d.%H%M"`



perl localbin/splitCapture.py $SCJ/output.cap
perl localbin/splitCapture.py $FIJI/output.cap
perl localbin/splitCapture.py $EMPTY_SCJ/output.cap


################### MEMORY ----

cp $SCJ/output_m.dat ./data/scj_mem.$timestamp.dat 
cp $FIJI/output_m.dat ./data/fiji_mem.$timestamp.dat 
cp $EMPTY_SCJ/output_m.dat ./data/scj_empty_mem.$timestamp.dat 

./R/memPlot.R ./data/scj_mem.$timestamp.dat ./data/fiji_mem.$timestamp.dat ./data/scj_empty_mem.$timestamp.dat 

cp ./mem_bench.pdf ./plots/mem_bench.$timestamp.pdf
rm -rf mem_bench.pdf

open ./plots/mem_bench.$timestamp.pdf





################### PERFORMANCE ----

cp $SCJ/output_d.dat ./data/perf_$timestamp_scj.dat
cp $FIJI/output_d.dat ./data/perf_.$timestamp_fiji.dat


./R/performancePlot.R ./data/perf_$timestamp_scj.dat ./data/perf_.$timestamp_fiji.dat

cp ./perf_bench.pdf ./plots/perf_bench.$timestamp.pdf
rm -rf perf_bench.pdf

open ./plots/perf_bench.$timestamp.pdf


################### CLEAN-UP----
rm -rf $SCJ/output_*
rm -rf $FIJI/output_*
rm -rf $EMPTY_SCJ/output_*