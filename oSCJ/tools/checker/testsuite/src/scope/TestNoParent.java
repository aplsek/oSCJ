//tests/scope/TestNoParent.java:26: Scope Definitions are not consistent: 
//         ManagedMemory mem = ManagedMemory.getCurrentManagedMemory();
//                       ^
//        Scope *TestVariable* is not defined but is parent to other scope.
//1 error

package scope;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.Scope;

@Scope("scope.MyMission") 
public class TestNoParent extends PeriodicEventHandler {

     public TestNoParent(PriorityParameters priority, PeriodicParameters period,
            StorageParameters storage, long size) {
        super(priority, period, storage, size);
    }

    public void foo() {
         @DefineScope(name="scope.MyMission", parent="TestVariable")    // ERROR
         ManagedMemory mem = ManagedMemory.getCurrentManagedMemory();
     }

    @Override
    public void handleEvent() {
    }

    @Override
    public void register() {
    }

    @Override
    public StorageParameters getThreadConfigurationParameters() {
        return null;
    }
    
}