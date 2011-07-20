#!/bin/bash

set -e
set -x

SPEC=~/_work/workspace_RT/jsr302-September/scj/specsrc/
RI=~/_work/workspace_RT/jsr302-September/RI/src/java/
oSCJ=/Users/plsek/fiji/scj-dan/oSCJ/tools/comparator/input/oSCJ

LIB=/Users/plsek/fiji/scj-dan/oSCJ/tools/comparator/input/lib/src
SPECLIB=/Users/plsek/fiji/scj-dan/oSCJ/tools/comparator/input/lib-spec/


LOG_RI=log-RI.txt
LOG_oSCJ=log-Spec-VS-oSCJ.txt

#java -classpath lib/javaparser-1.0.8.jar -jar lib/comparator.jar $SPEC $oSCJ $LOG_oSCJ
#
#java -classpath lib/javaparser-1.0.8.jar -jar lib/comparator.jar $SPEC $RI $LOG_RI

#java -classpath lib/javaparser-1.0.8.jar -jar lib/comparator.jar $oSCJ $SPEC log.txt


#java -classpath lib/javaparser-1.0.8.jar -jar lib/comparator.jar $oSCJ $RI logRI.txt


java -classpath lib/javaparser-1.0.8.jar -jar lib/comparator2.jar $LIB $SPECLIB logLIB.txt

