/**
 * 
 */
package papabench.scj.autopilot.tasks;

import static papabench.scj.utils.ParametersFactory.getPeriodicParameters;
import static papabench.scj.utils.ParametersFactory.getPriorityParameters;

import javax.safetycritical.StorageParameters;

import papabench.scj.autopilot.conf.PapaBenchAutopilotConf.LinkFBWSendTaskConf;
import papabench.scj.autopilot.modules.AutopilotModule;
import papabench.scj.commons.tasks.PapaBenchPeriodicTask;

/**
 * @author Michal Malohlava
 *
 */
//@SCJAllowed
public class LinkFBWSendTask extends PapaBenchPeriodicTask implements LinkFBWSendTaskConf {
	
	private AutopilotModule autopilotModule;
	
	public LinkFBWSendTask(AutopilotModule autopilotModule) {
		super(getPriorityParameters(PRIORITY), 
				getPeriodicParameters(RELEASE_MS, PERIOD_MS), 
				null, // FIXME 
				SIZE);
		
		this.autopilotModule = autopilotModule;
	}

	@Override
	public void handlePeriod() {
		// FIXME currenty the task do nothing -> the implementation of send is in StabilizationTask, however it should be here
	}
		
	public String getTaskName() {
		return NAME;
	}

    @Override
    public void register() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public StorageParameters getThreadConfigurationParameters() {
        // TODO Auto-generated method stub
        return null;
    }
}
