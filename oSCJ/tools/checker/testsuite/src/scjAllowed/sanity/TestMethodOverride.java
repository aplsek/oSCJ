package scjAllowed.sanity;

import static javax.safetycritical.annotate.Level.LEVEL_1;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;

import javax.safetycritical.Mission;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

@SCJAllowed(value = LEVEL_1, members = true)
public class TestMethodOverride extends Mission {

    @Override
    public long missionMemorySize() {
        return 0;
    }

    @Override
    @SCJRestricted(INITIALIZATION)
    protected void initialize() {
    }

}
