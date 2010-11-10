#!/bin/bash



#
# Any input file name must be in a fortmat: output_name_of_this_run.cap
#   --> plot in legend will have "name_of_this_run" as a name of that input
#

#set -e
#set -x


# ./bench.sh cdj_hf_A.1600 /Users/plsek/_work/fiji/fivm-scope/scj/oSCJ/examples/cdj-vanilla-fiji-v1.2/bench/regular_CDj_frame_1000_period_80_plane_60_GC_fifo10_detector_fifoMax
# ./bench.sh cdj_hf_A /Users/plsek/_work/fiji/fivm-scope/scj/oSCJ/examples/cdj-vanilla-fiji-v1.2/bench/regular_CDj_frame_1000_period_80_plane_60_GC_fifo10_detector_fifoMax
# ./bench.sh cdj_hf_CW /Users/plsek/_work/fiji/fivm-scope/scj/oSCJ/examples/cdj-vanilla-fiji-v1.2/bench/regular_CDj_frame_1000_period_80_plane_60_GC_fifo10_detector_fifoMax
# ./bench.sh cdj_hf_A /Users/plsek/_work/fiji/fivm-scope/scj/oSCJ/examples/minicdx/bench /Users/plsek/_work/fiji/fivm-scope/scj/oSCJ/examples/_bench/data/frame1000_period40

if [ $# -lt 2 ]; then
        echo "Error: not enough input parameters"
        echo "rexex - the first parameter should be the regular expression specifying the input files, it can be also \"\""
        echo "dir - second parameter is an input directory where .cap files are"
        echo "eg. ./bench.sh cdj_hf_A.1600 ./dir1 ./dir2"
        echo $@
        exit 1
fi;

regex=$1

mkdir -p plots
mkdir tmp

#input[0]=../minicdx/bench
#input[0]=/Users/plsek/_work/fiji/fivm-scope/scj/oSCJ/examples/cdj-vanilla-fiji-v1.2/bench/regular_CDj_frame_1000_period_80_plane_60_GC_fifo10_detector_fifoMax
#input[0]=$2

i=$((0))
for var in "$@" 
do
    echo $var
    if [ $i = 0 ]; then
    	#echo "nula"
    	i=$(( i + 1 )) # increase number by 1
    	continue
    fi
    #j=$(( i - 1 )) # increase number by 1
    #echo "j is: "$j
    #echo "vars: "$var
    ##echo $i
    #input[$j]=$var
    #let i++`
    for file in `find $var/ -maxdepth 1 -name "*.cap"`
    do
                cp $file tmp/
    done
done


timestamp=`date +"%m%d.%H%M"`



#for dir in $input
#do
#	echo "dir is: "$dir
#        for file in `find $dir -name "*.cap"`
#	do
#		cp $file tmp/
#	done
#done

echo "INPUT is:"
echo `ls tmp/`

# SPLIT
for file in `find ./tmp -name $regex"*.cap"`
do
	perl localbin/splitCapture.py $file
done


# add SCJ benchmark into it:
SCJ_FILE=`find ./tmp -name "*scj*.cap"`
perl localbin/splitCapture.py $SCJ_FILE


# FOR MEMORY:
mem_file=""
for file in `find ./tmp -name "*_m.dat"`
do
    if [[ -s $file ]] ; then
        mem_file=$mem_file" "
        mem_file=$mem_file$file
    else
	echo "$file is empty."
    fi ;
done 

if [ "$mem_file" == ""  ] ; then
    echo "Mem input is empty."
else
   ./R/memPlot.R $mem_file
fi;



# FOR PERFORMANCE:

perf_files=""
for file in `find . -name "*_d.dat"`
do
    if [[ -s $file ]] ; then
       perf_files=$perf_files" "
       perf_files=$perf_files$file 
    else
        echo "$file is empty."
    fi ;
done 

if [ "$perf_files" == ""  ] ; then
    echo "Perf input is empty."
else
   ./R/perfPlot.R $perf_files
fi;


#./R/perfPlot.R $perf_files
#./R/heapPerf.R $perf_files


#### SAVE PDF                                                                                                                                               
FIGURES="perf_bench mem_bench Time_Distr_box WCET_vs_heap WCET_vs_trigger Memory_Distr_box"
EXT="pdf"

for fig in $FIGURES; do
    if [ -e $fig.$EXT ]; then 
	cp $fig.$EXT ./plots/$regex$fig$timestamp.$EXT
    fi
done

rm -rf tmp

exit 0











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

./R/perfPlot.R ./data/perf_$timestamp_scj.dat ./data/perf_.$timestamp_fiji.dat  ./data/fiji_mem_hg_level_a.$timestamp.dat ./data/fiji_mem_hg_level_a.$timestamp.dat #./data/fiji_mem_hg_100k_a.$timestamp.dat 

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




