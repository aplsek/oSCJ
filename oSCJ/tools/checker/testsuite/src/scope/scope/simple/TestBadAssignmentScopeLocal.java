package scope.scope.simple;

import javax.safetycritical.Mission;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.Scope;

import scope.scope.simple.TestBadAssignmentScopeField.X;

public class TestBadAssignmentScopeLocal {
    @DefineScope(name="a", parent=Scope.IMMORTAL)
    static abstract class X extends Mission {
        Y y1;
        @Scope(Scope.IMMORTAL) Y y2;
        static Y y3;
    }
    static class Y { }

    void foo(X x, Y y, @Scope(Scope.IMMORTAL) Y yImm) {
        y = x.y1;
        //## checkers.scope.ScopeChecker.ERR_BAD_ASSIGNMENT_SCOPE
        y = x.y2;
        yImm = x.y2;
    }

    void bar(@Scope(Scope.IMMORTAL) X x, Y y, @Scope(Scope.IMMORTAL) Y yImm) {
        //## checkers.scope.ScopeChecker.ERR_BAD_ASSIGNMENT_SCOPE
        y = x.y1;
        //## checkers.scope.ScopeChecker.ERR_BAD_ASSIGNMENT_SCOPE
        y = x.y2;
        yImm = x.y1;
        yImm = x.y2;
    }

    void baz(Y y, @Scope(Scope.IMMORTAL) Y yImm) {
        //## checkers.scope.ScopeChecker.ERR_BAD_ASSIGNMENT_SCOPE
        y = X.y3;
        yImm = X.y3;
    }
}