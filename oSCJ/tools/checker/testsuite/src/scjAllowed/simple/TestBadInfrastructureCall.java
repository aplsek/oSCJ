package scjAllowed.simple;

import static javax.safetycritical.annotate.Scope.IMMORTAL;

import javax.realtime.MemoryArea;
import javax.safetycritical.Mission;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.Scope;

@SCJAllowed
public class TestBadInfrastructureCall {
    @Scope(IMMORTAL)
    @DefineScope(name="a", parent=IMMORTAL)
    MemoryArea mem;

    @SCJAllowed
    public void foo() {
        //## checkers.scjAllowed.SCJAllowedChecker.ERR_BAD_INFRASTRUCTURE_CALL
        mem.enter(null);
    }

    @DefineScope(name="a", parent=IMMORTAL)
    abstract static class X extends Mission { }
}