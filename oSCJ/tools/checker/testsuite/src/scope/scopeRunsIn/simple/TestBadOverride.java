package scope.scopeRunsIn.simple;

import static javax.safetycritical.annotate.Level.SUPPORT;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import javax.safetycritical.annotate.Scope;

import static javax.safetycritical.annotate.Scope.IMMORTAL;

@SCJAllowed(members=true)
public class TestBadOverride {

    @Scope(IMMORTAL)
    @DefineScope(name="a", parent=IMMORTAL)
    static abstract class X extends MissionSequencer {
        @SCJRestricted(INITIALIZATION)
        public X() {super(null, null);}
    }

    @Scope("a")
    @DefineScope(name="PEH", parent="a")
    @SCJAllowed(members=true)
    static abstract class PEH extends PeriodicEventHandler {

        @SCJRestricted(INITIALIZATION)
        public PEH(PriorityParameters priority, PeriodicParameters period,
                StorageParameters storage) {
            super(priority, period, storage);
        }

        PEHImplementation peh;

        @Override
        @SCJAllowed(SUPPORT)
        @RunsIn("PEH")
        public void handleAsyncEvent () {
        }

    }

    @SCJAllowed(members=true)
    static public class PEHImplementation implements MyRunnable {
        public void run() {
        }
    }


    @SCJAllowed(members=true)
    static interface MyRunnable extends Runnable {

        @RunsIn("PEH")
        public void run();
    }
}
