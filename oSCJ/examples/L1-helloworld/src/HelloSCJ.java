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
import javax.safetycritical.io.Connector;
import javax.safetycritical.io.SimplePrintStream;

import javax.microedition.io.Connection;
import javax.microedition.io.ConnectionNotFoundException;

import edu.purdue.scj.VMSupport;
import edu.purdue.scj.utils.Utils;

public class HelloSCJ extends Mission implements Safelet {

	static SimplePrintStream out;

	// From Mission
	protected void initialize() {

		OutputStream os = null;
		try {
		    //os = Connector.openOutputStream("console:");
              		Connector a = Connector.newInstance();
                        os = a.openOutputStream("console:");
		} catch (IOException e) {
			throw new Error("No console available");
		} catch (ConnectionNotFoundException e) {
		}
		out = new SimplePrintStream(os);

		PeriodicEventHandler peh = new MyHandler(new PriorityParameters(11),
				new PeriodicParameters(new RelativeTime(0, 0),
						new RelativeTime(1000, 0)), new StorageParameters(
						10000, 1000, 1000));
		peh.register();
	}

	// Safelet methods
	// @Override
	public MissionSequencer getSequencer() {
		// we assume this method is invoked only once
		StorageParameters sp = new StorageParameters(1000000, 0, 0);
		return new LinearMissionSequencer(new PriorityParameters(13), sp, this);
	}

	// @Override
	public long missionMemorySize() {
		return 100000;
	}
        public void setUp(){
        }
        public void tearDown(){
        }
}
