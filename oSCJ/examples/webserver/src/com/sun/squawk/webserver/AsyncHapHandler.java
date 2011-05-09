package com.sun.squawk.webserver;

import static javax.safetycritical.annotate.Level.LEVEL_1;
import static javax.safetycritical.annotate.Level.SUPPORT;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;
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
import javax.safetycritical.annotate.SCJRestricted;
import javax.safetycritical.annotate.Scope;

/**
 * The asynchronous handler for POSIX SIGQUIT. When receiving signal, disable
 * printers, print the backing store tree, and count down to terminate the
 * program.
 */
@Scope("IMMORTAL")
@SCJAllowed(value=LEVEL_1, members=true)
@DefineScope(name="AsyncHapHandler", parent=IMMORTAL)
public class AsyncHapHandler extends AperiodicEventHandler {
    
    // XXX/TODO: this class is not used!!!s
    
    @SCJAllowed(value=LEVEL_1, members=true)
    static class AsyncHappening extends ManagedAutonomousHappening {
       
        @SCJRestricted(INITIALIZATION)
        public AsyncHappening(AperiodicEventHandler handler) {
            super(Config.SIGQUIT);
            AsyncEvent event = new AperiodicEvent(handler);
            attach(event);
        }
    }

    @SCJRestricted(INITIALIZATION)
    public AsyncHapHandler(PriorityParameters priority, AperiodicParameters aperiod,
            StorageParameters storage, long initMemSize) {
        super(priority, aperiod, storage);
        new AsyncHappening(this);
    }

    @RunsIn("AsyncHapHandler")
    @SCJAllowed(SUPPORT)
    public void handleAsyncEvent() {
        // This is NOT public API. Used only for debugging or for fun.
//        BackingStore.printBSTree(true);
    }
}
