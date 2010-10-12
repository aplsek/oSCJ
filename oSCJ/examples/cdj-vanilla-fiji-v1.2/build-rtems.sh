#!/bin/bash

set -e
set -x

# FIJI HOME
FIJI=../../../../
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

#
#
# =====================================================================================

FIJI_FLAGS="--g-def-max-mem=500k --g-def-trigger=300k  -G hf --g-pred-level a --more-opt --max-threads 5 --uniprocessor"
# COMPILE FIJI 1
echo "Compiling cdj_hg_level_a"
$FIJI/bin/fivmc -o cdj-rtems_hf_A cdj.jar --main heap/Main --reflect cdj.reflectlog $FIJI_FLAGS $RTEMSFLAGS



#echo "TESTING, exit after 1 compilation!!!!!!"
#exit 1



#
# =====================================================================================
#
#





FIJI_FLAGS="--g-def-max-mem=700k --g-def-trigger=500k  -G hf --g-pred-level c --more-opt --max-threads 5 --uniprocessor"


# COMPILE FIJI 1
echo "Compiling cdj_hg_level_a"

$FIJI/bin/fivmc -o cdj-rtems_hf_C cdj.jar --main heap/Main --reflect cdj.reflectlog $FIJI_FLAGS $RTEMSFLAGS





#
# =====================================================================================
#
#




FIJI_FLAGS="--g-def-max-mem=900k --g-def-trigger=700k  -G hf --g-pred-level cw --more-opt --max-threads 5 --uniprocessor"


# COMPILE FIJI 1
echo "Compiling cdj_hg_level_a"

$FIJI/bin/fivmc -o cdj-rtems_hf_CW cdj.jar --main heap/Main --reflect cdj.reflectlog $FIJI_FLAGS $RTEMSFLAGS





echo "All done."










