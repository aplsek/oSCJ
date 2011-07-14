#!/bin/bash

SPEC=/Users/plsek/_work/workspace_RT/jsr302-TCK/scj/specsrc
RI=/Users/plsek/_work/workspace_RT/jsr302-patch/RI/src/java
#/Users/plsek/workspace/jsr302-patch/RI

oSCJ=/Users/plsek/workspace/scj-current/ri/spec/common/src/


LOG_RI=log-RI.txt
LOG_oSCJ=log-Spec-VS-oSCJ.txt

java -classpath lib/javaparser-1.0.8.jar -jar lib/comparator.jar $SPEC $oSCJ $LOG_oSCJ

java -classpath lib/javaparser-1.0.8.jar -jar lib/comparator.jar $SPEC $RI $LOG_RI