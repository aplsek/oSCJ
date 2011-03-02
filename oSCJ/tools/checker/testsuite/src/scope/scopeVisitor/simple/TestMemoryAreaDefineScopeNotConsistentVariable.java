package scope.scopeVisitor.simple;

import static javax.safetycritical.annotate.Scope.IMMORTAL;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.Mission;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.Scope;
import javax.safetycritical.annotate.SCJRestricted;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;

@DefineScope(name="Mission",parent=IMMORTAL)
@Scope("Mission")
public abstract class TestMemoryAreaDefineScopeNotConsistentVariable extends Mission {
}

@Scope("Mission")
@DefineScope(name="PEH",parent="Mission")
abstract class Handler5 extends PeriodicEventHandler {

    @SCJRestricted(INITIALIZATION)
    public Handler5(PriorityParameters priority, PeriodicParameters period,
            StorageParameters storage) {
        super(priority, period, storage);
    }

    public void method() {
        @DefineScope(name="PEH",parent="Mission")
        @Scope(IMMORTAL)
        //## checkers.scope.ScopeChecker.ERR_MEMORY_AREA_DEFINE_SCOPE_NOT_CONSISTENT_WITH_SCOPE
        ManagedMemory mem4;
    }
}