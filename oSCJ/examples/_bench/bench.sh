#!/bin/bash



#
# Any input file name must be in a fortmat: output_name_of_this_run.cap
#   --> plot in legend will have "name_of_this_run" as a name of that input
#


#set -e
#set -x


# ./bench.sh cdj_hf_A.1600 /Users/plsek/_work/fiji/fivm-scope/scj/oSCJ/examples/cdj-vanilla-fiji-v1.2/bench/regular_CDj_frame_1000_period_80_plane_60_GC_fifo10_detector_fifoMax

if [ $# -ne 2 ]; then
        echo "Error: not enough input parameters"
        echo "rexex - the first parameter should be the regular expression specifying the input files, it can be also \"\""
        echo "dir - second parameter is an input directory where .cap files are"
        echo "eg. ./bench.sh cdj_hf_A.1600 ./dir"
        exit 1
fi;

regex=$1

#input[0]=../minicdx/bench
#input[0]=/Users/plsek/_work/fiji/fivm-scope/scj/oSCJ/examples/cdj-vanilla-fiji-v1.2/bench/regular_CDj_frame_1000_period_80_plane_60_GC_fifo10_detector_fifoMax
input[0]=$2


#input[0]=../minicdx/bench
#input[1]=../cdj-vanilla-fiji-v1.2/bench/
#input[2]=/Users/plsek/_work/fiji/fivm-scope/scj/oSCJ/examples/cdj-vanilla-fiji-v1.2/bench/regular_CDj_frame_1000_period_80_plane_60_GC_fifo10_detector_fifoMax

#input[2]=../emptyBench/



timestamp=`date +"%m%d.%H%M"`

mkdir tmp

for dir in $input
do
	for file in `find $dir -name "*.cap"`
	do
		cp $file tmp/
		#cd tmp/ && perl localbin/splitCapture.py $file
	
		# TODO: rename the file and add timestamps!!!!
		#cp $file data/$file$timestamp
	done
done

echo "INPUT is:"
# SPLiT
for file in `find ./tmp -name $regex"*.cap"`
do
	perl localbin/splitCapture.py $file
done



# FOR MEMORY:
mem_file=""
for file in `find ./tmp -name "*_m.dat"`
do
	mem_file=$mem_file" "
	mem_file=$mem_file$file
done 

./R/memPlot.R $mem_file


# FOR PERFORMANCE:

perf_files=""
for file in `find . -name "*_d.dat"`
do
	perf_files=$perf_files" "
	perf_files=$perf_files$file
done 

./R/performancePlot.R $perf_files






rm -rf tmp
exit 1










################### MEMORY ----                                                                        

cp $SCJ/output_m.dat ./data/scj_mem.$timestamp.dat
cp $FIJI/output_m.dat ./data/fiji_mem.$timestamp.dat
cp $EMPTY_SCJ/output_m.dat ./data/scj_empty_mem.$timestamp.dat

./R/memPlot.R ./data/scj_mem.$timestamp.dat ./data/fiji_mem.$timestamp.dat ./data/scj_empty_mem.$times\
tamp.dat

cp ./mem_bench.pdf ./plots/mem_bench.$timestamp.pdf
#rm -rf mem_bench.pdf                                                                                  

#open ./plots/mem_bench.$timestamp.pdf                                                                 



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




