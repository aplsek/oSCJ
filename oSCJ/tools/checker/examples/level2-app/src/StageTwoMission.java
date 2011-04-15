

import static javax.safetycritical.annotate.Level.LEVEL_2;
import static javax.safetycritical.annotate.Level.SUPPORT;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import static javax.safetycritical.annotate.Scope.IMMORTAL;

import javax.realtime.RelativeTime;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.Mission;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;
import javax.safetycritical.annotate.Scope;
import javax.safetycritical.annotate.RunsIn;



@SCJAllowed(members=true, value=LEVEL_2)
@Scope("StageMission")
public class StageTwoMission extends Mission {
	private static final int MISSION_MEMORY_SIZE = 10000;

	@Override
	@SCJRestricted(INITIALIZATION)
	@SCJAllowed(SUPPORT)
    public void initialize() {
		ManagedMemory.getCurrentManagedMemory().resize(MISSION_MEMORY_SIZE);
		(new SecondaryPeriodicEventHandler("stage2.eh1", new RelativeTime(0, 0),
				new RelativeTime(500, 0))).register();
	}

	@Override
	@SCJAllowed
    public long missionMemorySize() {
		return MISSION_MEMORY_SIZE;
	}
}
