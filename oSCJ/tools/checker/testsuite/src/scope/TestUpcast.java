//scope/TestUpcast.java:50: Cannot assign expression in scope a to variable in scope IMMORTAL.
//        TestUpcast.cast = cast;   // ERROR
//                        ^
//scope/TestUpcast.java:52: Cannot assign expression in scope null to variable in scope IMMORTAL.
//        TestUpcast.upCast = (TestUpcast) cast;  // ERROR
//                          ^
///Users/plsek/_work/workspace_RT/scj-annotations/tests/scope/TestUpcast.java:54: Cannot assign expression in scope a to variable in scope IMMORTAL.
//        TestUpcast.upCast = cast;   // ERROR
//                          ^
//3 errors

package scope;

import javax.safetycritical.ManagedMemory;
import javax.safetycritical.PrivateMemory;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.Scope;
import static javax.safetycritical.annotate.Scope.IMMORTAL;


public class TestUpcast {
   
    static public L cast = null;
    static public TestUpcast upCast = null;
    
    static class Helper {
        static void foo() {
            ManagedMemory.
            getCurrentManagedMemory().
                enterPrivateMemory(0, new R1());
        }
        @Scope(IMMORTAL)
        @DefineScope(name="a", parent=IMMORTAL)
        static class R1 implements Runnable {
            @Override
            @RunsIn("a")
            public void run() {
            }
        }
    }
}

@Scope("a")
class L extends TestUpcast {
    public void foo() {
        TestUpcast f = (TestUpcast) new L(); // OK
        TestUpcast g = new L(); // OK
        
        L cast = new L();
        TestUpcast fail = (TestUpcast) cast; // OK
        
        TestUpcast.cast = cast;   // ERROR
        
        TestUpcast.upCast = (TestUpcast) cast;  // ERROR
        
        TestUpcast.upCast = cast;   // ERROR
    }
}