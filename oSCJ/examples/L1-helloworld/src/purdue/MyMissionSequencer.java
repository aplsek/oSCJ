package purdue;

import static javax.safetycritical.annotate.Level.LEVEL_1;
import static javax.safetycritical.annotate.Level.SUPPORT;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import static javax.safetycritical.annotate.Scope.IMMORTAL;

import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;
import javax.safetycritical.annotate.Scope;
import javax.safetycritical.annotate.RunsIn;

@Scope(IMMORTAL)
@SCJAllowed(value=LEVEL_1, members=true)
@DefineScope(name="MISSION",parent=IMMORTAL)
public class MyMissionSequencer extends MissionSequencer {

    @SCJRestricted(INITIALIZATION)
    public MyMissionSequencer(PriorityParameters priority,
            StorageParameters storage) {
        super(priority, storage);
    }

    @Override
    @SCJAllowed(SUPPORT)
    @RunsIn("MISSION") 
    protected Mission getNextMission() {
        return new MyMission();
    }

}
