package checkers.scope;

import javax.safetycritical.annotate.Scope;

public class ScopeInfo {
    public static final ScopeInfo CURRENT = new ScopeInfo(Scope.CURRENT);
    public static final ScopeInfo IMMORTAL = new ScopeInfo(Scope.IMMORTAL);
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

    public boolean isUnknown() {
        return equals(UNKNOWN);
    }

    public boolean isFieldScope() {
        return false;
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
