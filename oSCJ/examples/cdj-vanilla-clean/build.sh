#!/bin/bash

set -e
set -x

# FIJI HOME
FIJI=../../../../

#CLEANUP
echo "Cleaning class files..."
find build/ -name "*.class" | xargs rm -f
rm -rf cdj.jar
rm -rf cdj_x86
rm -rf build/
mkdir build/

find . -name *.java > list
echo "Compiling with javac..."
javac -classpath "$FIJI/runtimej/build:$FIJI/lib/fijicore.jar:$FIJI/lib/fivmcommon.jar:$FIJI/lib/fijirt.jar:$FIJI/lib/fivmr.jar" -d ./build @list
rm -rf list

# JAR
echo "Creating jar package"
cd build/ && find . -name "*.class" | xargs jar cf ../cdj.jar
cd ..


# COMPILE FIJI 1
#echo "Compiling cdj_x86"
#FIJI_FLAGS="--g-def-max-mem=1300k --g-def-trigger=1m  -G hf --more-opt --max-threads 5 --uniprocessor"
#$FIJI/bin/fivmc -o cdj_x86 cdj.jar --main heap/Main --reflect cdj.reflectlog $FIJI_FLAGS

#
# =====================================================================================
#
#FIJI_FLAGS="--g-def-max-mem=1300k --g-def-trigger=1000k  -G hf --g-pred-level a --more-opt --max-threads 5 --uniprocessor"
#echo "Compiling cdj_hg_level_a"
#$FIJI/bin/fivmc -o cdj_hf_A.1300.1000 cdj.jar --main heap/Main --reflect cdj.reflectlog $FIJI_FLAGS 


#FIJI_FLAGS="--g-def-max-mem=1300k --g-def-trigger=1m  -G hf --g-pred-level c --more-opt --max-threads 5 --uniprocessor"
#echo "Compiling cdj_hg_level_a"
#$FIJI/bin/fivmc -o cdj_hf_C.1300.1000 cdj.jar --main heap/Main --reflect cdj.reflectlog $FIJI_FLAGS 


FIJI_FLAGS="--g-def-max-mem=1000k --g-def-trigger=700k  -G hf --g-pred-level cw --more-opt --max-threads 5 --uniprocessor"
$FIJI/bin/fivmc -o cdj_hf_CW.1000.700 cdj.jar --main heap/Main --reflect cdj.reflectlog $FIJI_FLAGS 

sudo ./cdj_hf_CW.1000.700 | tee output_cdj_STAR_CW.1000.700.cap

echo "All done."


