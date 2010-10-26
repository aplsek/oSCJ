package crossScope_A;

import static javax.safetycritical.annotate.Allocate.Area.THIS;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.Allocate;
import javax.safetycritical.annotate.CrossScope;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.Scope;
import crossScope_A.TestErrorCrossScope.Foo;
import crossScope_A.TestErrorCrossScope.Handler;


@Scope("crossScope_A.TestGetCurrentManagedMemory") 
public class TestGetCurrentManagedMemory {


    protected
    void initialize() { 
        new Handler(null, null, null, 0);
    }

    public long missionMemorySize() {
        return 0;
    }
    
    @Scope("crossScope_A.TestGetCurrentManagedMemory")  
    @RunsIn("crossScope_A.Handler") 
    class Handler extends PeriodicEventHandler {

    	Foo foo ;
    	
        public Handler(PriorityParameters priority,
                PeriodicParameters parameters, StorageParameters scp, long memSize) {
            super(priority, parameters, scp, memSize);
        
            this.foo = new Foo();			// OK
        }

        public void handleEvent() {

        	this.foo = new Foo();			// ERROR
        	
        	MyRunnable aRunnable = new MyRunnable(this);
        	@DefineScope(name="crossScope_A.Handler.Child", parent="crossScope_A.Handler") 
            ManagedMemory mem = ManagedMemory.getCurrentManagedMemory();
            mem.enterPrivateMemory(1000, aRunnable);  
        	
        }        

        @Allocate({THIS})
        public Foo getCurrentFoo() {
        	return this.foo;
        }
        
        @Override
        public StorageParameters getThreadConfigurationParameters() {
            return null;
        }
    }
    
    
    @Scope("crossScope_A.Handler")  
    @RunsIn("crossScope_A.Handler.Child") 
    class MyRunnable implements Runnable {
		
    	private Handler handler;
    	
    	public MyRunnable(Handler handler) {
    		this.handler = handler;
    	}
    	
    	public void run() {
			BigBar bb = new BigBar();
			bb.method();
			
			Foo foo = handler.getCurrentFoo();				// OK
			
			foo.methodAlloc();								// ERROR< should be @CrossScope
		}
    }
    
    
    class Bar {
    	public void method() {
    		ManagedMemory mem = ManagedMemory.getCurrentManagedMemory();  // ERROR
    	}
    }

    @RunsIn("crossScope_A.Handler.Child") 
    class BigBar {
    	public void method() {
    		ManagedMemory mem = ManagedMemory.getCurrentManagedMemory();  // OK
    	}
    }
    
    
    class Foo {
    	
    	Bar bar;
    	
    	public void method() {
    		
    	}
    	
    	public void methodAlloc() {
    		this.bar = new Bar();			// OK
    	}
    }

	
}
