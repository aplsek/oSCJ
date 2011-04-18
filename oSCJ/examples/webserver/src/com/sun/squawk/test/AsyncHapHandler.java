package com.sun.squawk.test;

import static javax.safetycritical.annotate.Level.LEVEL_1;
import static javax.safetycritical.annotate.Level.SUPPORT;
import static javax.safetycritical.annotate.Scope.IMMORTAL;

import javax.realtime.AperiodicParameters;
import javax.realtime.AsyncEvent;
import javax.realtime.PriorityParameters;
import javax.safetycritical.AperiodicEvent;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.ManagedAutonomousHappening;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.Scope;

/**
 * The asynchronous handler for POSIX SIGQUIT. When receiving signal, disable
 * printers, print the backing store tree, and count down to terminate the
 * program.
 */
@Scope(">")
@SCJAllowed(value=LEVEL_1, members=true)
@DefineScope(name="?", parent=IMMORTAL)
public class AsyncHapHandler extends AperiodicEventHandler {

    static class AsyncHappening extends ManagedAutonomousHappening {
        public AsyncHappening(AperiodicEventHandler handler) {
            super(Config.SIGQUIT);
            AsyncEvent event = new AperiodicEvent(handler);
            attach(event);
        }
    }

    public AsyncHapHandler(PriorityParameters priority, AperiodicParameters aperiod,
            StorageParameters storage, long initMemSize) {
        super(priority, aperiod, storage);
        new AsyncHappening(this).register();
    }

    @RunsIn("OneMission")
    @SCJAllowed(SUPPORT)
    public void handleAsyncEvent() {
        // This is NOT public API. Used only for debugging or for fun.
//        BackingStore.printBSTree(true);
    }
}
