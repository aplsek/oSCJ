package com.sun.squawk.webserver;

import static javax.safetycritical.annotate.Level.LEVEL_1;
import static javax.safetycritical.annotate.Level.SUPPORT;
import static javax.safetycritical.annotate.Scope.IMMORTAL;

import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.SingleMissionSequencer;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.Scope;

@Scope(IMMORTAL)
@SCJAllowed(value=LEVEL_1, members=true)
@DefineScope(name="MyMission", parent=IMMORTAL)
public class MySequencer extends SingleMissionSequencer {

    private boolean first = true;

    @SCJAllowed(SUPPORT)
    @RunsIn("MyMission")
    protected Mission getNextMission() {
        if (first) {
            first = false;
            return new MyMission();
        }
        return null;
    }

    public MySequencer(PriorityParameters priority, StorageParameters storage) {
        super(priority, storage, null);
    }
}
