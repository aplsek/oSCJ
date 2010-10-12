#!/bin/sh

K=K
# in KB




PROGRAMS=`find . -name "cdj-rtems*" -not -name "*build*"`

CD_PAR="dummy MAX_FRAMES 1000 PERIOD 80"
trig_rate="8 6 4 2"

for p in $PROGRAMS; do
    echo $p

    #		./$p $CD_PAR > $p.$heap.$trigger.output 2>&1
    echo "executing "$p

    sparc-rtems4.9-run $p 2>&1 | tee $p.output	
    
done