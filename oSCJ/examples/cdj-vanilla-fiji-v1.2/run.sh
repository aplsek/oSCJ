#!/bin/sh

K=K
# in KB
max_heap=2000
min_heap=1000
step=200
PROGRAMS="cdj_hf_A cdj_hf_C cdj_hf_CW"
CD_PAR="dummy MAX_FRAMES 1000 PERIOD 80"
trig_rate="8 6 4 2"

for p in $PROGRAMS; do
    heap=$min_heap
    while [ $heap -le $max_heap ]; do
	trigger=$((heap - step))
	for r in $trig_rate; do
	    trigger=$((heap * r / 10))
	    echo "Prog: $p  Heap: $heap$K  Trig: $trigger$K"
	    echo $(date)
	    echo "FIVMR_GC_THREAD_PRIORITY=FIFO10 \
		FIVMR_GC_MAX_MEM=$heap$K \
		FIVMR_GC_TRIGGER=$trigger$K \
		./$p $CD_PAR > $p.$heap.$trigger.output 2>&1"
	    FIVMR_GC_THREAD_PRIORITY=FIFO10 \
		FIVMR_GC_MAX_MEM=$heap$K \
		FIVMR_GC_TRIGGER=$trigger$K \
		./$p $CD_PAR > $p.$heap.$trigger.output 2>&1
	done
	heap=$((heap + step))
    done
done
