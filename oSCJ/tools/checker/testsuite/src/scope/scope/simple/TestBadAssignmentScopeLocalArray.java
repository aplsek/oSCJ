package scope.scope.simple;

import javax.safetycritical.annotate.Scope;

public class TestBadAssignmentScopeLocalArray {
    void foo(@Scope(Scope.IMMORTAL) Object[] os,
            @Scope(Scope.IMMORTAL) Object oImm) {
        os[0] = oImm;
        //## checkers.scope.ScopeChecker.ERR_BAD_ASSIGNMENT_SCOPE
        os[0] = new Object();
    }

    void bar(Object[] os, Object o) {
        os[0] = o;
        os[0] = new Object();
    }
}