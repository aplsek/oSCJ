//scope/TestScopeCheck2.java:94: Cannot assign expression in scope MyTestRunnable to variable in scope scope.TestScopeCheck2.
//            data = new Object();   //// ERROR
//                 ^
//                     visitNewClass
//1 error

package scope;

import javax.safetycritical.CyclicExecutive;
import javax.safetycritical.CyclicSchedule;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.Scope;

@Scope("scope.TestScopeCheck2")
public class TestScopeCheck2  extends CyclicExecutive  {

    public TestScopeCheck2() {
        super(null);
    }
    
    public CyclicSchedule getSchedule(PeriodicEventHandler[] handlers) {
        return null;
    }

    public void initialize() {
        new MyWordHandler2(20000);
    }

    public long missionMemorySize() {
        return 5000000;
    }

    @Override
    public void setUp() {}
    @Override
    public void tearDown() {}

}



@Scope("scope.TestScopeCheck2")
@RunsIn("scope.TestScopeCheck2.MyWordHandler2")
class MyWordHandler2 extends PeriodicEventHandler {

    public MyWordHandler2(long psize) {
        super(null, null, null, psize);
    }

    public Object data;
    
    @RunsIn("scope.TestScopeCheck2.MyWordHandler2")
    public void handleEvent() {
       
        
     @DefineScope(name="scope.TestScopeCheck2.MyWordHandler2",  
                parent="scope.TestScopeCheck2")
     ManagedMemory mem = ManagedMemory.getCurrentManagedMemory();
     
     mem.enterPrivateMemory(300, 
                     new /*@DefineScope(name="MyTestRunnable",
                      parent="scope.TestScopeCheck2.MyWordHandler2")*/ 
                     MyErrorRunnable());
    }

    @SCJAllowed()
    public void cleanUp() {}
    
    @SCJAllowed()
    public void register() {}
    
    @SCJAllowed()
    @Override
    public void handleAsyncEvent() {}

    @Override
    public StorageParameters getThreadConfigurationParameters() {
        return null;
    }

    @Scope("scope.TestScopeCheck2.MyWordHandler2")
    @RunsIn("MyTestRunnable")
    class MyErrorRunnable implements Runnable {

        public MyErrorRunnable() {
        }
        
        @Override
        public void run() {
            data = new Object();   //// ERROR
        }
    }
}


