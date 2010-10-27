package crossScope_A.inference;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.Scope;

import crossScope_A.TestInference;


@Scope("crossScope_A.TestNullInference") 
public class TestNullInference extends Mission {

    protected
    void initialize() { 
        new Handler(null, null, null, 0);
    }

    @Override
    public long missionMemorySize() {
        return 0;
    }
    
}


@Scope("crossScope_A.TestNullInference")  
@RunsIn("crossScope_A.Handler") 
class Handler extends PeriodicEventHandler {

	private TestInference mission;
	
    public Handler(PriorityParameters priority,
            PeriodicParameters parameters, StorageParameters scp, long memSize) {
        super(priority, parameters, scp, memSize);
    }

    public
    void handleEvent() {
    	Mission mission = null; 							// OK
    	mission = Mission.getCurrentMission();				// OK!!!
    	
    	Mission myMission; 									// OK
    	myMission = Mission.getCurrentMission();			// TODO: is this OK!!!
    }

    @Override
    public StorageParameters getThreadConfigurationParameters() {
        return null;
    }
}
