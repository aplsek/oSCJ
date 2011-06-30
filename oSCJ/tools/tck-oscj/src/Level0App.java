//package level0;
// no error here



import static javax.safetycritical.annotate.Level.SUPPORT;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import static javax.safetycritical.annotate.Scope.IMMORTAL;

import javax.safetycritical.CyclicExecutive;
import javax.safetycritical.CyclicSchedule;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;
import javax.safetycritical.annotate.Scope;
//import javax.safetycritical.Mission;
//import javax.safetycritical.StorageParameters;
import javax.realtime.RelativeTime;
@SCJAllowed(members=true)
@Scope("Level0App")
@DefineScope(name="Level0App", parent=IMMORTAL)
public class Level0App extends CyclicExecutive {

    @SCJRestricted(INITIALIZATION)
    public Level0App() { 
       super(null);
       System.out.println("OK!-1"); 
    }

    @Override
    @SCJAllowed(SUPPORT)
    public CyclicSchedule getSchedule(PeriodicEventHandler[] handlers) {
	CyclicSchedule.Frame[] frames = new CyclicSchedule.Frame[1];
	CyclicSchedule schedule = new CyclicSchedule(frames);
	frames[0] = new CyclicSchedule.Frame(new RelativeTime(200, 0), handlers);
	System.out.println("OK!-4"); 
        return schedule; 
   }

    @Override
    @SCJRestricted(INITIALIZATION)
    @SCJAllowed(SUPPORT)
    public void initialize() { 
        new WordHandler2(300000);
        System.out.println("OK!-3");
    }

    @Override
    public long missionMemorySize() {
        return 5000000;
    }

    @Override
    @SCJAllowed(SUPPORT)
    public void setUp() {
	System.out.println("OK!-2"); 
    }

    @Override
    @SCJAllowed(SUPPORT)
    public void tearDown() {
	System.out.println("OK!-5"); 
    }

}
