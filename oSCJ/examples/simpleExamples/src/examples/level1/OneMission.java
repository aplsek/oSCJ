package examples.level1;


import java.util.Arrays;
import javax.safetycritical.annotate.RunsIn;
import static javax.safetycritical.annotate.Scope.UNKNOWN;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.Scope;
import static javax.safetycritical.annotate.Scope.IMMORTAL;
import javax.safetycritical.annotate.DefineScope;
import static javax.safetycritical.annotate.Level.LEVEL_1;

import javax.realtime.Clock;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.PriorityScheduler;
import javax.safetycritical.Safelet;
import javax.safetycritical.StorageParameters;

@Scope("OneMission")
@DefineScope(name="OneMission", parent=IMMORTAL)
@SCJAllowed(value=LEVEL_1, members=true)
public class OneMission extends Mission {
  public void initialize() {
    new PEH(new PriorityParameters(PriorityScheduler.instance().getNormPriority()),
            new PeriodicParameters(new RelativeTime(0, 0), new RelativeTime(500, 0)),
            new StorageParameters(50000L, 1000L, 1000L));
  }

@Override
public long missionMemorySize() {
	return 1000L;
}
}
