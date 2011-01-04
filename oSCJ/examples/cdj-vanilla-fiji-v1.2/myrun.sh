#!/bin/sh

K=K
# in KB
max_heap=1200
mid_heap=1200
min_heap=1200
step_20=20
step_200=200
PROGRAMS="cdj_hf_A"
CD_PAR="dummy MAX_FRAMES 1000 PERIOD 40"
trig_rate="9 8 7 6 5 4 3 2 1"

for p in $PROGRAMS; do
    heap=$min_heap
    while [ $heap -le $max_heap ]; do
	for r in $trig_rate; do
	    trigger=$((heap * r / 10))
	    echo "Prog: $p  Heap: $heap$K  Trig: $trigger$K"
	    echo $(date)
	    echo "FIVMR_LOG_GC=true FIVMR_GC_THREAD_PRIORITY=FIFO10 FIVMR_GC_MAX_MEM=$heap$K FIVMR_GC_TRIGGER=$trigger$K ./$p $CD_PAR > $p.$heap.$trigger.cap 2>&1\n"
	    FIVMR_LOG_GC=false \
		FIVMR_GC_THREAD_PRIORITY=FIFO10 \
		FIVMR_GC_MAX_MEM=$heap$K \
		FIVMR_GC_TRIGGER=$trigger$K \
		./$p $CD_PAR > $p.$heap.$trigger.cap 2>&1
	done
	if [ $heap -lt $mid_heap ]; then
	    heap=$((heap + step_20))
	else
	    heap=$((heap + step_200))
	fi
    done
done