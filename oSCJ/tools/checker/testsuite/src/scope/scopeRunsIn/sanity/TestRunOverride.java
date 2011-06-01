package scope.scopeRunsIn.sanity;

import javax.realtime.PriorityParameters;

import javax.safetycritical.ManagedThread;
import javax.safetycritical.StorageParameters;

import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.SCJRestricted;
import javax.safetycritical.annotate.Scope;
import javax.safetycritical.annotate.SCJAllowed;
import static javax.safetycritical.annotate.Level.LEVEL_2;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import static javax.safetycritical.annotate.Scope.IMMORTAL;
import static javax.safetycritical.annotate.Level.SUPPORT;

@Scope("D")
@DefineScope(name="D", parent=IMMORTAL)
@SCJAllowed(value=LEVEL_2, members=true)
public class TestRunOverride extends ManagedThread {

    @SCJRestricted(INITIALIZATION)
    public TestRunOverride(int priority) {
        super(new PriorityParameters(priority), new StorageParameters(0, 0, 0));
    }

    @Override
    @RunsIn("D")
    @SCJAllowed(SUPPORT)
    public void run() { }
}
