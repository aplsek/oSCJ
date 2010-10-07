#!/bin/bash

set -e
set -x

# FIJI HOME
FIJI=../../../../

FIJI_FLAGS="--g-def-max-mem=1300k --g-def-trigger=1m  -G hf --more-opt --max-threads 5 --uniprocessor"
RTEMSFLAGS="--target sparc-rtems4.9"

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
echo "Compiling cdj_x86"

$FIJI/bin/fivmc -o cdj-rtems_hf cdj.jar --main heap/Main --reflect cdj.reflectlog $FIJI_FLAGS $RTEMSFLAGS




#
#
# =====================================================================================
#
#



FIJI_FLAGS="--g-def-max-mem=1300k --g-def-trigger=1m  -G hf --g-pred-level a --more-opt --max-threads 5 --uniprocessor"


# COMPILE FIJI 1
echo "Compiling cdj_hg_level_a"

$FIJI/bin/fivmc -o cdj-rtems_hf_a cdj.jar --main heap/Main --reflect cdj.reflectlog $FIJI_FLAGS $RTEMSFLAGS




#
# =====================================================================================
#
#





FIJI_FLAGS="--g-def-max-mem=1300k --g-def-trigger=1m  -G hf --g-pred-level c --more-opt --max-threads 5 --uniprocessor"


# COMPILE FIJI 1
echo "Compiling cdj_hg_level_a"

$FIJI/bin/fivmc -o cdj-rtems_hf_cw cdj.jar --main heap/Main --reflect cdj.reflectlog $FIJI_FLAGS $RTEMSFLAGS





#
# =====================================================================================
#
#




FIJI_FLAGS="--g-def-max-mem=1300k --g-def-trigger=1m  -G hf --g-pred-level cw --more-opt --max-threads 5 --uniprocessor"


# COMPILE FIJI 1
echo "Compiling cdj_hg_level_a"

$FIJI/bin/fivmc -o cdj-rtems_hf_cw cdj.jar --main heap/Main --reflect cdj.reflectlog $FIJI_FLAGS $RTEMSFLAGS





echo "All done."










