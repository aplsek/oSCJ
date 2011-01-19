//
package examples.error;

import java.util.Arrays;
import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.Scope;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.CyclicSchedule;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.Mission;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.realtime.AbsoluteTime;
import javax.realtime.Clock;
import javax.realtime.ImmortalMemory;
import javax.realtime.MemoryArea;
import javax.realtime.RealtimeThread;
import javax.safetycritical.CyclicExecutive;
import javax.safetycritical.MissionManager;
import javax.safetycritical.Safelet;
import javax.safetycritical.Terminal;


import javax.safetycritical.annotate.SCJRestricted;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import static javax.safetycritical.annotate.Phase.CLEANUP;
import javax.safetycritical.annotate.Scope;

@Scope("examples.error.MyAppErr") 
public class MyAppErr extends CyclicExecutive {
	static PriorityParameters p = new PriorityParameters(18); 
 	static StorageParameters s = new StorageParameters(1000L, 1000L, 1000L); 
 	static RelativeTime t = new RelativeTime(5,0);
 	
 	public CyclicSchedule getSchedule(PeriodicEventHandler[] handlers) { 
		return new CyclicSchedule(new CyclicSchedule.Frame[]{new CyclicSchedule.Frame(t,handlers)});
	}

	public MyAppErr() {
		super(p, s);
	}

	public void initialize() {
		new Err();
	}

	/**
	 * A method to query the maximum amount of memory needed by this
	 * mission.
	 * 
	 * @return the amount of memory needed
	 */
	public long missionMemorySize() {
		return 1420;   // MIN without printing is 430  bytes.
	}

	@SCJRestricted(INITIALIZATION) 
	public void setUp() {
	}

	@SCJRestricted(CLEANUP)
	public void tearDown() {
	}

	@SCJRestricted(CLEANUP)
	public void cleanUp() {
	}
}

@Scope("examples.error.MyAppErr") 
@RunsIn("examples.error.Err") 
class Err extends PeriodicEventHandler {

	static PriorityParameters pri;
	static PeriodicParameters per;
	static StorageParameters stor;

	static {
		pri = new PriorityParameters(13);
		per = new PeriodicParameters(new RelativeTime(0, 0),
				new RelativeTime(500, 0));
		stor = new StorageParameters(1000L, 1000L, 1000L);
	}

	public Err() {
		super(pri, per, stor);
	}
	
	List a = new List();

	public void handleAsyncEvent() {
		List b = new List();
		setTail(b,a);
		setTail(a,b);
		this.a = b;


		Mission.getCurrentMission().requestSequenceTermination();
	}
	
	public void setTail(List x,List y) {
		x.tail = y;
	}

	public void cleanUp() {
	}

	public StorageParameters getThreadConfigurationParameters() {
		return null;
	}
}

class List {
	List tail;
}