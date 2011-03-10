package scope.defineScope.simple;

import static javax.safetycritical.annotate.Scope.IMMORTAL;

import javax.safetycritical.Mission;
import javax.safetycritical.annotate.DefineScope;

public class TestDuplicateScopeName {
    @DefineScope(name="a", parent=IMMORTAL)
    static abstract class X extends Mission { }
    @DefineScope(name="b", parent=IMMORTAL)
    static abstract class Y extends Mission { }
    @DefineScope(name="b", parent="a")
    //## checkers.scope.DefineScopeChecker.ERR_DUPLICATE_SCOPE_NAME
    static abstract class Z extends Mission { }
}
