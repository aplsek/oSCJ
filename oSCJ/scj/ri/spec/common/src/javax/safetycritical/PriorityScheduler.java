package javax.safetycritical;

import javax.safetycritical.annotate.SCJAllowed;


import javax.safetycritical.annotate.SCJRestricted;
import javax.safetycritical.annotate.SCJAllowed;
import static javax.safetycritical.annotate.Level.INFRASTRUCTURE;
import javax.safetycritical.annotate.Level;

import static javax.safetycritical.annotate.Level.LEVEL_1;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;
import static javax.safetycritical.annotate.Level.SUPPORT;
import static javax.safetycritical.annotate.Phase.CLEANUP;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;	


@SCJAllowed(LEVEL_1)
public class PriorityScheduler extends javax.realtime.PriorityScheduler {

	/**
	 * 
	 * @return Returns the maximum hardware real-time priority supported by this virtual machine. @SCJAllowed
	 */
	@SCJAllowed(LEVEL_1)
	@SCJRestricted()
	public int getMaxHardwarePriority() {
		//TODO:
		return 0;
	}
	
	
	/**
	 * Returns the minimum hardware real-time priority supported by this virtual machine.
	 */
	@SCJAllowed(LEVEL_1) 
	@SCJRestricted()
	public int getMinHardwarePriority() {
		//TODO:
		return 0;
	} 
}
