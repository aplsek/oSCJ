package javax.safetycritical;

import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;
import static javax.safetycritical.annotate.Level.SUPPORT;
import static javax.safetycritical.annotate.Phase.CLEANUP;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;
/**
 * In SCJ, all schedulable objects are managed by a mission. 
 * 
 * @author plsek
 *
 */
@SCJAllowed
public interface ManagedSchedulable extends Schedulable{
	
	/**
	 * Register this schedulable object with the current mission.
	 */
	@SCJAllowed 
	 @SCJRestricted(INITIALIZATION)
	public void register();
	
	/**
	 * Runs any end-of-mission clean up code associated with this schedulable object.
	 */
	@SCJAllowed(SUPPORT)
	@SCJRestricted(CLEANUP)
	public void cleanUp();
}
