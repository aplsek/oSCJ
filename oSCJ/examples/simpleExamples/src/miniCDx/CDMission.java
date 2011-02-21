package miniCDx;

import javax.realtime.AbsoluteTime;
import javax.realtime.Clock;
import javax.realtime.ImmortalMemory;
import javax.realtime.MemoryArea;
import javax.realtime.PriorityParameters;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.safetycritical.CyclicExecutive;
import javax.safetycritical.CyclicSchedule;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.MissionManager;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.Safelet;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.Terminal;

import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.SCJRestricted;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import static javax.safetycritical.annotate.Phase.CLEANUP;
import javax.safetycritical.annotate.Scope;
import javax.safetycritical.annotate.RunsIn;

public class CDMission extends CyclicExecutive {

	static PriorityParameters p = new PriorityParameters(18);
	static StorageParameters s = new StorageParameters(1000L, 1000L, 1000L);
	static RelativeTime t = new RelativeTime(5, 0);

	public CyclicSchedule getSchedule(PeriodicEventHandler[] handlers) {
		return new CyclicSchedule(
				new CyclicSchedule.Frame[] { new CyclicSchedule.Frame(
						t, handlers) });
	}

	public CDMission() {
		super(p, s);
	}

	protected void initialize() {
		new CDHandler();
		@DefineScope(name = "MissionInit", parent = "CDMission")
		MIRun miRun = new MIRun();
		ManagedMemory m = (ManagedMemory) ManagedMemory.getMemoryArea(this);
		m.enterPrivateMemory(2000, miRun);
	}

	/**
	 * A method to query the maximum amount of memory needed by this
	 * mission.
	 * 
	 * @return the amount of memory needed
	 */
	public long missionMemorySize() {
		return 5420;
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

@Scope("CDMission")
class MIRun implements Runnable {
	@RunsIn("MIRun")
	public void run() {
		//...
	}
}
