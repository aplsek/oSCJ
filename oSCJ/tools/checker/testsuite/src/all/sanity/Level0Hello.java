package all.sanity;

import static javax.safetycritical.annotate.Level.SUPPORT;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import static javax.safetycritical.annotate.Phase.CLEANUP;
import static javax.safetycritical.annotate.Scope.IMMORTAL;

import javax.realtime.RelativeTime;
import javax.safetycritical.CyclicExecutive;
import javax.safetycritical.CyclicSchedule;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;
import javax.safetycritical.annotate.Scope;

@SCJAllowed(members=true)
@Scope(IMMORTAL)
@DefineScope(name="Level0App", parent=IMMORTAL)
public class Level0Hello extends CyclicExecutive {

    @SCJRestricted(INITIALIZATION)
    public Level0Hello() {
        super(null);
    }

    @Override
    @SCJAllowed(SUPPORT)
    @RunsIn("Level0App")
    public CyclicSchedule getSchedule(PeriodicEventHandler[] handlers) {
        return new CyclicSchedule(
                          new CyclicSchedule.Frame[] { new CyclicSchedule.Frame(new RelativeTime(200, 0),
                                  handlers) });
    }

    @Override
    @SCJRestricted(INITIALIZATION)
    @SCJAllowed(SUPPORT)
    @RunsIn("Level0App")
    public void initialize() {
        new WordHandler(20000);
    }

    @Override
    @SCJAllowed(SUPPORT)
    public long missionMemorySize() {
        return 5000000;
    }


    @SCJAllowed(members=true)
    @Scope("Level0App")
    @DefineScope(name="WordHandler", parent="Level0App")
    static public class WordHandler extends PeriodicEventHandler {

        @SCJAllowed()
        @SCJRestricted(INITIALIZATION)
        public WordHandler(long psize) {
            super(null, null, null);
        }

        @Override
        @SCJAllowed(SUPPORT)
        @RunsIn("WordHandler")
        public void handleAsyncEvent() {
            // printing HelloWorld!!!!
        }

        @Override
        @SCJAllowed(SUPPORT)
        @SCJRestricted(CLEANUP)
        public void cleanUp() {
        }
    }
}
