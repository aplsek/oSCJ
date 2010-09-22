/**
 * 
 */
package papabench.scj.commons.tasks;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.ReleaseParameters;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;

import papabench.scj.commons.conf.CommonTaskConfiguration;
import papabench.scj.utils.LogUtils;

/**
 * PeriodicEventHandler wrapper.
 * 
 * @author Michal Malohlava
 *
 */
public abstract class PapaBenchPeriodicTask extends PeriodicEventHandler implements CommonTaskConfiguration {
	
	/* 
	 * Static variable to distinguish between two repetition of PapaBench schedule
	 * 
	 * Required by JPF.
	 */	
	public static int PAPBENCH_TASKS_EXECUTION_COUNTER = 0;

	public PapaBenchPeriodicTask(PriorityParameters pp, PeriodicParameters rp,
			StorageParameters stp, long memSize) {
		super(pp, rp, stp, memSize);
	}
	
	@Override
	final public void handleEvent() {
		LogUtils.log(getTaskName(), "PERIOD handler enter");		
		
		try {
			PAPBENCH_TASKS_EXECUTION_COUNTER++;
			
			handlePeriod();			
		} finally {		
			LogUtils.log(getTaskName(), "PERIOD handler return");
		}
	}
	
	public abstract void handlePeriod();

}
