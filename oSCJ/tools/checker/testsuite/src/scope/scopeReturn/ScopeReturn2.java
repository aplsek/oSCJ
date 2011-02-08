//testsuite/src/scope/scopeReturn/ScopeReturn2.java:58: Cannot assign expression in scope MyMission to variable in scope MyHandler.
//        MyFoo localFoo = mission.getFoo();              // ERROR
//              ^
//1 error

package scope.scopeReturn;

import static javax.safetycritical.annotate.Scope.IMMORTAL;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.Scope;



@DefineScope(name="MyMission",parent=IMMORTAL)
@Scope("MyMission") 
public class ScopeReturn2 extends Mission {

    //@Scope(IMMORTAL) static Foo foo = new Foo();
    
    protected void initialize() { 
    }

    @Override
    public long missionMemorySize() {
        return 0;
    }
    
    MyFoo foo = new MyFoo();
    
    @Scope("MyMission") @RunsIn("MyHandler")
    public MyFoo getFoo() {
        return this.foo;           // ERR
    }
}




@Scope("MyMission")
@DefineScope(name="MyHandler",parent="MyMission")
class MyPEH extends PeriodicEventHandler {
    
    ScopeReturn2 mission = null;
    
    public MyPEH(PriorityParameters priority,
            PeriodicParameters parameters, StorageParameters scp, long memSize, ScopeReturn2 mission) {
        super(priority, parameters, scp);
        
        this.mission = mission;
    }

    @RunsIn("MyHandler") 
    public void handleAsyncEvent() {
        //MyMission mission = (MyMission) Mission.getCurrentMission();
        
        MyFoo localFoo = mission.getFoo();              // ERROR
    }

    @Override
    public StorageParameters getThreadConfigurationParameters() {
        return null;
    }  
    
}


class MyFoo {}