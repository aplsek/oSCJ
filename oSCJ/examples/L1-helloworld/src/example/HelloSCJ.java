package example;



import static javax.safetycritical.annotate.Level.LEVEL_1;
import static javax.safetycritical.annotate.Level.SUPPORT;
import static javax.safetycritical.annotate.Phase.CLEANUP;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import static javax.safetycritical.annotate.Scope.IMMORTAL;

import javax.safetycritical.annotate.RunsIn;

import java.io.IOException;
import java.io.OutputStream;
import javax.realtime.AbsoluteTime;
import javax.realtime.Clock;
import javax.realtime.ImmortalMemory;
import javax.realtime.MemoryArea;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.realtime.PriorityParameters;
import javax.realtime.PeriodicParameters;
import javax.safetycritical.CyclicExecutive;
import javax.safetycritical.CyclicSchedule;
import javax.safetycritical.MissionManager;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.Mission;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.Safelet;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.Terminal;
import javax.safetycritical.LinearMissionSequencer;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;
import javax.safetycritical.annotate.Scope;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.io.Connector;
import javax.safetycritical.io.SimplePrintStream;

import javax.microedition.io.Connection;
import javax.microedition.io.ConnectionNotFoundException;


@Scope(IMMORTAL)
@SCJAllowed(value=LEVEL_1, members=true)
@DefineScope(name="A",parent=IMMORTAL)
public class HelloSCJ extends Mission implements Safelet {

    // From Mission
    @SCJRestricted(INITIALIZATION)
    @Override
    @SCJAllowed(SUPPORT)
    @RunsIn("A")
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

    // @Override
    @SCJRestricted(INITIALIZATION)
    @Scope(IMMORTAL)
    @SCJAllowed(SUPPORT)
    public MissionSequencer getSequencer() {
        // we assume this method is invoked only once
        StorageParameters sp = new StorageParameters(1000000, 0, 0);
        return new LinearMissionSequencer(new PriorityParameters(13), sp, this);
    }

    // @Override
    public long missionMemorySize() {
        return 100000;
    }

    @SCJAllowed(SUPPORT)
    @SCJRestricted(INITIALIZATION)
    public void setUp() {
    }

    @SCJAllowed(SUPPORT)
    @SCJRestricted(CLEANUP)
    public void tearDown() {
    }
}
