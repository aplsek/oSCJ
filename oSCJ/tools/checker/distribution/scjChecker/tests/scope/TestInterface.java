//scope/TestInterface.java:21: @RunsIn annotations must agree with their overridden annotations.
//    public void run() {   
//                ^
//1 error

package scope;

import javax.safetycritical.ManagedMemory;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.Scope;

@Scope("immortal")
interface Test {
    @RunsIn("a")
    void run();
}

@Scope("immortal")
public class TestInterface implements Test {
    @Scope("a")
    class A {
        
    }
    
    public void run() {             // ERROR
        A a = new A();
    }
    
    static class Helper {
        static void foo() {
            ManagedMemory.
            getCurrentManagedMemory().
                enterPrivateMemory(0, new /*@DefineScope(name="a", parent="immortal")*/ R1());
        }
        @Scope("immortal") @RunsIn("a")
        static class R1 implements Runnable {
            @Override
            public void run() {
            }
        }
    }
}
