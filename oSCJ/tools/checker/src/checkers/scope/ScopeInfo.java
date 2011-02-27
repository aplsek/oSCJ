package checkers.scope;

import javax.safetycritical.annotate.Scope;


/**
 * ScopeInfo values:
 *
 * null --- A literal "null" value for ScopeInfo indicates that it's a primitive.
 * NULL --- The NULL scope constant I put in was to represent the null value in programs
 *         - a value that can be cast to any scope.
 *
 *
 */
public class ScopeInfo {
    public static final ScopeInfo CURRENT = new ScopeInfo(Scope.CURRENT);
    public static final ScopeInfo IMMORTAL = new ScopeInfo(Scope.IMMORTAL);
    public static final ScopeInfo NULL = new ScopeInfo("null");
    public static final ScopeInfo UNKNOWN = new ScopeInfo(Scope.UNKNOWN);
    private final String scope;

    public ScopeInfo(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }

    public boolean isCurrent() {
        return equals(CURRENT);
    }

    public boolean isImmortal() {
        return equals(IMMORTAL);
    }

    public boolean isNull() {
        return equals(NULL);
    }

    public boolean isUnknown() {
        return equals(UNKNOWN);
    }

    public boolean isReservedScope() {
        return isCurrent() || isImmortal() || isNull() || isUnknown();
    }

    public boolean isFieldScope() {
        return false;
    }

    public boolean isValidParentScope() {
        return !(isCurrent() || isNull() || isUnknown());
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && scope.equals(((ScopeInfo) obj).scope);
    }

    @Override
    public int hashCode() {
        return scope.hashCode();
    }

    @Override
    public String toString() {
        return scope;
    }
}
