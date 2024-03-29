// no error here

package scope.schedulable.simple;

import static javax.safetycritical.annotate.Level.SUPPORT;
import static javax.safetycritical.annotate.Phase.*;
import static javax.safetycritical.annotate.Scope.IMMORTAL;
import javax.safetycritical.CyclicExecutive;
import javax.safetycritical.CyclicSchedule;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;
import javax.safetycritical.annotate.Scope;
import javax.safetycritical.annotate.RunsIn;

@SCJAllowed(members=true)
@Scope(IMMORTAL)
@DefineScope(name="Level0App", parent=IMMORTAL)
public class TestHandleAsyncEventRunsIn extends CyclicExecutive {

    @SCJRestricted(INITIALIZATION)
    public TestHandleAsyncEventRunsIn() {
        super(null);
    }

    @Override
    @SCJAllowed(SUPPORT)
    @SCJRestricted(INITIALIZATION)
    @RunsIn("Level0App")
    public CyclicSchedule getSchedule(PeriodicEventHandler[] handlers) {
        return null;
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
    //## checkers.scope.SchedulableChecker.ERR_SCHEDULABLE_NO_RUNS_IN
    static class WordHandler extends PeriodicEventHandler {

        @SCJAllowed()
        @SCJRestricted(INITIALIZATION)
        public WordHandler(long psize) {
            super(null, null, null);
        }

        @Override
        @SCJAllowed(SUPPORT)
        public void handleAsyncEvent() {
        }

        @Override
        @SCJAllowed(SUPPORT)
        @SCJRestricted(CLEANUP)
        public void cleanUp() {
        }
    }
}
