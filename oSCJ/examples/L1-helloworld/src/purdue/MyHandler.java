package purdue;
import static javax.safetycritical.annotate.Level.LEVEL_1;
import static javax.safetycritical.annotate.Level.SUPPORT;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import static javax.safetycritical.annotate.Scope.IMMORTAL;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;
import javax.safetycritical.annotate.Scope;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.io.SimplePrintStream;

/**
 * 
 * 
 * 
 */
@SCJAllowed(value=LEVEL_1, members=true)
@Scope("A")
@DefineScope(name="H",parent="A")
class MyHandler extends PeriodicEventHandler {
    
    @SCJRestricted(INITIALIZATION)
    public MyHandler(SimplePrintStream out, PriorityParameters prio,
            PeriodicParameters peri, StorageParameters stor) {
        super(prio, peri, stor);
        myout = out;
    }

    int cnt;
    SimplePrintStream myout;

    @RunsIn("H")
    @SCJAllowed(SUPPORT)
    public void handleAsyncEvent() {
        myout.println("Ping " + cnt);
        ++cnt;
    }
}