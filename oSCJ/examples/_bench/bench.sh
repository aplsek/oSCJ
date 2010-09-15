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


perl localbin/splitCapture.py $FIJI/output_hg_level_a.cap 
perl localbin/splitCapture.py $FIJI/output_hg_100k.cap 





################### PERFORMANCE ----

cp $SCJ/output_d.dat ./data/perf_$timestamp_scj.dat
cp $FIJI/output_d.dat ./data/perf_.$timestamp_fiji.dat

cp $FIJI/output_hg_level_a_d.dat ./data/fiji_mem_hg_level_a.$timestamp.dat 
cp $FIJI/output_hg_100k_d.dat ./data/fiji_mem_hg_100k_a.$timestamp.dat 

./R/performancePlot.R ./data/perf_$timestamp_scj.dat ./data/perf_.$timestamp_fiji.dat  ./data/fiji_mem_hg_level_a.$timestamp.dat ./data/fiji_mem_hg_level_a.$timestamp.dat #./data/fiji_mem_hg_100k_a.$timestamp.dat 

cp ./perf_bench.pdf ./plots/perf_bench.$timestamp.pdf
#rm -rf perf_bench.pdf

#open ./plots/perf_bench.$timestamp.pdf






################### CLEAN-UP----
rm -rf $SCJ/output_*

rm -rf $EMPTY_SCJ/output_*


rm -rf $FIJI/output_d*
rm -rf $FIJI/output_m*
rm -rf $FIJI/output_r*

rm -rf $FIJI/output_hg_level_a_*
rm -rf $FIJI/output_hg_100k_*



exit 1




