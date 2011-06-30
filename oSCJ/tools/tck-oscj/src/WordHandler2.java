//package level0;


import static javax.safetycritical.annotate.Level.SUPPORT;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;

import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;
import javax.safetycritical.annotate.Scope;
import javax.safetycritical.Mission;
import javax.safetycritical.StorageParameters;

@SCJAllowed(members=true)
@Scope("Level0App")
@DefineScope(name="WordHandler", parent="Level0App")
public class WordHandler2 extends PeriodicEventHandler {

    @SCJAllowed()
    @SCJRestricted(INITIALIZATION)
    public WordHandler2(long psize) {
        super(null, null,new StorageParameters(psize, 1000L, 1000L), null);
    }

    @Override
    @SCJAllowed(SUPPORT)
    @RunsIn("WordHandler")
    public void handleAsyncEvent() {
        // printing HelloWorld!!!!
	Mission.getCurrentMission().requestSequenceTermination();
    }

    @Override
    @SCJAllowed(SUPPORT)
    public void cleanUp() {
    }
}
