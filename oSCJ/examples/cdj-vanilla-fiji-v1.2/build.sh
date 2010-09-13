#!/bin/bash

set -e
set -x

# FIJI HOME
FIJI=../../../../

FIJI_FLAGS="--g-def-max-mem=1300k --g-def-trigger=1m  -G hf --more-opt --max-threads 5 --uniprocessor"


#--g-def-max-mem=830k --g-def-trigger=700k
#--g-def-max-mem=830k --g-def-trigger=500k
#--g-def-max-mem=1830k --g-def-trigger=1000k

#CLEANUP
echo "Cleaning class files..."
find build/ -name "*.class" | xargs rm -f
rm -rf cdj.jar
rm -rf cdj_x86
rm -rf build/
mkdir build/


find . -name *.java > list
echo "Compiling with javac..."
#javac src/heap/Main.java -classpath "$FIJI/runtimej/build:$FIJI/lib/fijicore.jar:$FIJI/lib/fivmcommon.jar:$FIJI/lib/fijirt.jar:$FIJI/lib/fivmr.jar" -d ./build
javac -classpath "$FIJI/runtimej/build:$FIJI/lib/fijicore.jar:$FIJI/lib/fivmcommon.jar:$FIJI/lib/fijirt.jar:$FIJI/lib/fivmr.jar" -d ./build @list
rm -rf list

# JAR
echo "Creating jar package"
cd build/ && find . -name "*.class" | xargs jar cf ../cdj.jar
cd ..



# COMPILE FIJI
echo "Compiling cdj_x86"

$FIJI/bin/fivmc -o cdj_x86 cdj.jar --main heap/Main --reflect cdj.reflectlog $FIJI_FLAGS

echo "running CDj!"
./cdj_x86  > bench/output.cap



echo "All done."











#echo "Compiling cdj_cmr"
#../fivm_sparc/bin/fivmc -o cdj_cmr cdj.jar --main heap/Main --target=sparc-rtems4.9 --reflect cdj.reflectlog --g-def-max-mem=50M --internal-inst instrumentation.h -G cmr --pollcheck-mode portable --g-def-trigger=30M --more-opt

#echo "Compiling cdj_hf_c"
#../fivm_sparc/bin/fivmc -o cdj_hf_c cdj.jar --main heap/Main --target=sparc-rtems4.9 --reflect cdj.reflectlog --g-def-max-mem=50M --internal-inst instrumentation.h -G hf --pollcheck-mode portable --g-def-trigger=30M --more-opt --g-pred-level=c

#echo "Compiling cdj_hf_a"
#../fivm_sparc/bin/fivmc -o cdj_hf_a cdj.jar --main heap/Main --target=sparc-rtems4.9 --reflect cdj.reflectlog --g-def-max-mem=50M --internal-inst instrumentation.h -G hf --pollcheck-mode portable --g-def-trigger=30M --more-opt --g-pred-level=a

#echo "Compiling cdj_hf_cw"
#../fivm_sparc/bin/fivmc -o cdj_hf_cw cdj.jar --main heap/Main --target=sparc-rtems4.9 --reflect cdj.reflectlog --g-def-max-mem=50M --internal-inst instrumentation.h -G hf --pollcheck-mode portable --g-def-trigger=30M --more-opt --g-pred-level=cw

