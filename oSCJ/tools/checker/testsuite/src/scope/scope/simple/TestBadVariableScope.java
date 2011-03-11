package scope.scope.simple;

import static javax.safetycritical.annotate.Scope.IMMORTAL;

import javax.safetycritical.Mission;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.Scope;

public class TestBadVariableScope {

    @Scope(IMMORTAL)
    class Foo {
        public void foo2() {
            //## checkers.scope.ScopeChecker.ERR_BAD_VARIABLE_SCOPE
            @Scope(IMMORTAL) X x;
        }
    }

    @Scope("a")
    @DefineScope(name="a", parent=IMMORTAL)
    static abstract class X extends Mission { }
}