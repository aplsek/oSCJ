package javax.safetycritical;

import static javax.safetycritical.annotate.Level.LEVEL_2;
// import javax.safetycritical.annotate.BlockFree;

import javax.realtime.PriorityParameters;
import javax.safetycritical.annotate.SCJAllowed;

@SCJAllowed(LEVEL_2)
public class NoHeapRealtimeThread extends javax.realtime.RealtimeThread {
    /**
     * Does not allow this to escape local variables. Creates a link from the
     * constructed object to the scheduling parameter. Thus, scheduling must
     * reside in a scope that encloses "this".
     * <p>
     * The priority represented by scheduling parameter is consulted only once,
     * at construction time. If scheduling.getPriority() returns different
     * values at different times, only the initial value is honored.
     * 
     * <pre>
     * \issue{
     * TBD: what is the "default" ThreadConfigurationParameters?  Or
     * should we remove this constructor?
     * }
     * </pre>
     */
    @SCJAllowed(LEVEL_2)
    public NoHeapRealtimeThread(PriorityParameters scheduling,
            StorageParameters mem_info) {
        super(null, null);
    }

    /**
     * Does not allow this to escape local variables. Creates a link from the
     * constructed object to the scheduling, memory, and logic parameters .
     * Thus, all of these parameters must reside in a scope that enclose "this".
     * <p>
     * The priority represented by scheduling parameter is consulted only once,
     * at construction time. If scheduling.getPriority() returns different
     * values at different times, only the initial value is honored.
     */
    @SCJAllowed(LEVEL_2)
    public NoHeapRealtimeThread(PriorityParameters scheduling,
            StorageParameters mem_info, Runnable logic) {
        super(null, null); // super(schedule, null, null, area, null, null);
    }

    @SCJAllowed(LEVEL_2)
    public void start() {
    }
}
