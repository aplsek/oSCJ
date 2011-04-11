package scope.scope.simple;

import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import static javax.safetycritical.annotate.Scope.IMMORTAL;

import javax.safetycritical.ManagedMemory;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.SCJRunnable;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;
import javax.safetycritical.annotate.Scope;

@DefineScope(name="a", parent=IMMORTAL)
@Scope("a")
public abstract class TestBadEnterPrivateMemorySCJRunnableScope extends MissionSequencer {

    @SCJRestricted(INITIALIZATION)
    public TestBadEnterPrivateMemorySCJRunnableScope() {super(null, null);}


    public void bar() {

        @Scope(IMMORTAL)
        @DefineScope(name="a", parent=IMMORTAL)
        ManagedMemory mem = null;
        Y y = new Y();
        //## checkers.scope.ScopeChecker.ERR_SCJ_RUNNABLE_BAD_SCOPE
        mem.enterPrivateMemory(1000, y);
    }

    @Scope("a")
    @DefineScope(name="b", parent="a")
    static abstract class X extends MissionSequencer {

        @SCJRestricted(INITIALIZATION)
        public X() {super(null, null);}

    }

    @SCJAllowed(members=true)
    @DefineScope(name="c", parent="a")
    static class Y implements SCJRunnable {
        @RunsIn("c")
        public void run() {
        }
    }
}
