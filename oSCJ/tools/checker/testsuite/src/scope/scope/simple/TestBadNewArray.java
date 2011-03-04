package scope.scope.simple;

import javax.safetycritical.ManagedMemory;
import javax.safetycritical.Mission;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.Scope;

@DefineScope(name="a", parent=Scope.IMMORTAL)
@Scope("a")
public abstract class TestBadNewArray extends Mission {
    @Scope("a")
    @DefineScope(name="b", parent="a")
    static abstract class X extends Mission {
        @DefineScope(name="a", parent=Scope.IMMORTAL)
        @Scope(Scope.IMMORTAL)
        ManagedMemory mem;

        @DefineScope(name="b", parent="a")
        @Scope("a")
        ManagedMemory mem2;

        public void foo() {
            try {
                mem.newArray(Y.class, 1);
                //## checkers.scope.ScopeChecker.ERR_BAD_NEW_ARRAY
                mem2.newArray(Y.class, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Scope("a")
    static class Y { }
}