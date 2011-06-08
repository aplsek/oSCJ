package purdue;

import static javax.safetycritical.annotate.Level.LEVEL_1;
import static javax.safetycritical.annotate.Level.SUPPORT;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import static javax.safetycritical.annotate.Scope.IMMORTAL;

import java.io.IOException;
import java.io.OutputStream;

import javax.microedition.io.ConnectionNotFoundException;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.Mission;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;
import javax.safetycritical.annotate.Scope;
import javax.safetycritical.io.Connector;
import javax.safetycritical.io.SimplePrintStream;

@Scope("MISSION")
@SCJAllowed(value=LEVEL_1, members=true)
public class MyMission extends Mission {
    
    // From Mission
    @SCJRestricted(INITIALIZATION)
    @Override
    @SCJAllowed(SUPPORT)
    protected void initialize() {

        OutputStream os = null;
        try {
            Connector a = Connector.newInstance();
            os = a.openOutputStream("console:");
        } catch (IOException e) {
            throw new Error("No console available");
        } catch (ConnectionNotFoundException e) {
        }
        // out = new SimplePrintStream(os);

        PeriodicEventHandler peh = new MyHandler(new SimplePrintStream(os),
                new PriorityParameters(11), new PeriodicParameters(
                        new RelativeTime(0, 0), new RelativeTime(1000, 0)),
                new StorageParameters(10000, 1000, 1000));
        peh.register();
    }

    @Override
    public long missionMemorySize() {
        return 0;
    }
}
