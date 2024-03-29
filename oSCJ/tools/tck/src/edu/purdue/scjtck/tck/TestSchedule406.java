package edu.purdue.scjtck.tck;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.PriorityScheduler;
import javax.realtime.RelativeTime;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageConfigurationParameters;

/**
 * Level 1
 * 
 * - A preempted schedulable object must be placed at the front of the run queue
 * for its active priority level.
 */
public class TestSchedule406 extends TestCase {

    public MissionSequencer getSequencer() {
        return new GeneralSingleMissionSequencer(new GeneralMission() {

            private volatile int _curRunner;
            private volatile boolean _done;
            private volatile boolean _preempted;
            private long _iter = _prop._iterations;

            public void initialize() {

                int NOR_PRIORITY = PriorityScheduler.instance()
                        .getNormPriority();
                int MIN_PRIORITY = PriorityScheduler.instance()
                        .getMinPriority();

                PriorityParameters high = new PriorityParameters(NOR_PRIORITY);
                PriorityParameters low = new PriorityParameters(NOR_PRIORITY
                        - (NOR_PRIORITY - MIN_PRIORITY) / 2);

                for (int i = 0; i < 5; i++) {
                    final int index = i;

                    // final String s = index + " back to run";
                    new PeriodicEventHandler(low, new PeriodicParameters(null,
                            new RelativeTime(Long.MAX_VALUE, 0)),
                            new StorageConfigurationParameters(0, 0, 0),
                            _prop._schedObjMemSize) {
                        public void handleEvent() {
                            while (!_done) {
                                if (_preempted) {
                                    int curRunner = _curRunner;
                                    _preempted = false;

                                    if (curRunner != index)
                                        fail("Thread being preempted should be placed in front of the run queue");

                                    // Terminal.getTerminal().writeln(s);
                                    Thread.yield();

                                } else
                                    _curRunner = index;
                            }
                        }
                    };
                }
                new PeriodicEventHandler(high, new PeriodicParameters(
                        new RelativeTime(20, 0), new RelativeTime(100, 0)),
                        new StorageConfigurationParameters(0, 0, 0),
                        _prop._schedObjMemSize) {
                    public void handleEvent() {
                        _preempted = true;
                        if (--_iter <= 0) {
                            _done = true;
                            requestSequenceTermination();
                        }
                        // Terminal.getTerminal().writeln(
                        // _curRunner + " ------------- being preempted");
                    }
                };
            }
        });
    }
}
