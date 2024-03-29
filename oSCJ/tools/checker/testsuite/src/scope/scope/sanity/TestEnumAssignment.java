package scope.scope.sanity;

import static javax.safetycritical.annotate.Level.SUPPORT;
import static javax.safetycritical.annotate.Level.LEVEL_2;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import static javax.safetycritical.annotate.Scope.CALLER;
import static javax.safetycritical.annotate.Scope.IMMORTAL;

import javax.realtime.PriorityParameters;
import javax.safetycritical.ManagedThread;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;
import javax.safetycritical.annotate.Scope;


@Scope("IMMORTAL")
@DefineScope(name="D", parent=IMMORTAL)
@SCJAllowed(value = LEVEL_2,members = true)
public abstract class TestEnumAssignment extends ManagedThread {

    @SCJRestricted(INITIALIZATION)
    public TestEnumAssignment(PriorityParameters scheduling,
            StorageParameters mem_info) {
        super(scheduling, mem_info);
    }


    @Scope(IMMORTAL)
    enum MyEnum {
      ONE, TWO
    };

    private MyEnum myEnum;

    @Override
    @RunsIn("D")
    @SCJAllowed(SUPPORT)
    public void run() {
        MyEnum m = getEnum();
    }

    @RunsIn(CALLER)
    MyEnum getEnum() {
        return myEnum;
    }

}
