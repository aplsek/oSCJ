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

FIJI_FLAGS="--g-def-max-mem=1200k --g-def-trigger=850k -G cmr --max-threads 5"
#$FIJI/bin/fivmc -o cdj-STAR-rtems_CMR.1200.850 cdj.jar --main heap/Main --reflect cdj.reflectlog $FIJI_FLAGS $RTEMSFLAGS                                                                                 



FIJI_FLAGS="--g-def-max-mem=1000k --g-def-trigger=700k -G cmr --max-threads 5"
#$FIJI/bin/fivmc -o cdj-rtems_cmr.1000.700 cdj.jar --main heap/Main --reflect cdj.reflectlog $FIJI_FLAGS $RTEMSFLAGS                                


FIJI_FLAGS="--g-def-max-mem=1200k --g-def-trigger=850k -G hf --g-pred-level a  --max-threads 5"
#$FIJI/bin/fivmc -o cdj-rtems_hf_A.1200.850 cdj.jar --main heap/Main --reflect cdj.reflectlog $FIJI_FLAGS $RTEMSFLAGS


FIJI_FLAGS="--g-def-max-mem=1200k --g-def-trigger=850k  -G hf --g-pred-level c --more-opt --max-threads 5 --uniprocessor"
#$FIJI/bin/fivmc -o cdj-rtems_hf_C.1200.850 cdj.jar --main heap/Main --reflect cdj.reflectlog $FIJI_FLAGS $RTEMSFLAGS


FIJI_FLAGS="--g-def-max-mem=1200k --g-def-trigger=850k  -G hf --g-pred-level cw --more-opt --max-threads 5 --uniprocessor"
$FIJI/bin/fivmc -o cdj-STAR-rtems_hf_CW.1200.850 cdj.jar --main heap/Main --reflect cdj.reflectlog $FIJI_FLAGS $RTEMSFLAGS



echo "All done."










