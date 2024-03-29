/**
 * 
 */
package papabench.scj.simulator.tasks;

import static papabench.scj.utils.ParametersFactory.getPeriodicParameters;
import static papabench.scj.utils.ParametersFactory.getPriorityParameters;

import javax.safetycritical.StorageParameters;

import papabench.scj.autopilot.data.Attitude;
import papabench.scj.autopilot.modules.AutopilotModule;
import papabench.scj.commons.tasks.PapaBenchPeriodicTask;
import papabench.scj.simulator.SimulatedDevice;
import papabench.scj.simulator.conf.PapaBenchSimulatorConf.SimulatorIRTaskConf;
import papabench.scj.simulator.model.FlightModel;
import papabench.scj.utils.LogUtils;

/**
 * @author Michal Malohlava
 *
 */
public class SimulatorIRTask extends PapaBenchPeriodicTask implements SimulatorIRTaskConf {
	
	private FlightModel flightModel;
	private SimulatedDevice irDevice;
	
	public SimulatorIRTask(FlightModel flightModel, AutopilotModule autopilotModule) {
		super(getPriorityParameters(PRIORITY), 
				getPeriodicParameters(RELEASE_MS, PERIOD_MS), 
				null, // CHECKME 
				SIZE);
		this.flightModel = flightModel;
		this.irDevice = (SimulatedDevice) autopilotModule.getIRDevice();
	}
	
	@Override
	public void handlePeriod() {
		Attitude attitude = flightModel.getState().getAttitude();
	
		LogUtils.log(this, "Att: " +attitude.toString());
	
		this.irDevice.update(this.flightModel);		
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
