//package edu.purdue.scjtck;

import javax.realtime.AperiodicParameters;
import javax.realtime.AsyncEventHandler;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.PriorityScheduler;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.ManagedThread;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.Safelet;
import javax.safetycritical.SingleMissionSequencer; //import javax.safetycritical.StorageConfigurationParameters;
import javax.safetycritical.StorageParameters;

import javax.safetycritical.Terminal;
import javax.safetycritical.annotate.Level;

//import edu.purdue.scj.PropFileReader;

public abstract class MainSafelet implements Safelet {

	protected Properties _prop = new Properties();

	protected Thread _launcher;

	/* Parameters generated from properties */
	protected PriorityParameters _priorityParam;
	protected AperiodicParameters _aperiodicParam;
	protected PeriodicParameters _periodicParam;
	// protected StorageConfigurationParameters _storageParam;
	protected StorageParameters _storageParam;

	/* ----------------- Methods ------------------- */

	public void setUp() {
		_launcher = Thread.currentThread();
		// _prop.parseArgs(PropFileReader.readAll());
		_priorityParam = new PriorityParameters(_prop._priority);
		_periodicParam = new PeriodicParameters(new RelativeTime(_prop._iDelay,
				0), new RelativeTime(_prop._period, 0));
		_aperiodicParam = new AperiodicParameters(new RelativeTime(
				_prop._period, 0), new AsyncEventHandler());
		// _storageParam = new StorageConfigurationParameters(0, 0, 0); above
		// same.
		_storageParam = new StorageParameters(0, 0, 0);

		Terminal.getTerminal().writeln(getInfo());
	}

	public void tearDown() {
		Terminal.getTerminal().writeln(report());
	}

	public Level getLevel() {
		return _prop._level;
	}

	protected abstract String getInfo();

	protected abstract String report();

	/* -------------- Wrapped Classes -------------- */
	public abstract class GeneralMission extends Mission {
		public long missionMemorySize() {
			return _prop._missionMemSize;
		}

		@Override
		// U u
		protected void cleanUp() {
			super.cleanUp();
			_launcher.interrupt();
		}
	}

	public class GeneralSingleMissionSequencer extends SingleMissionSequencer {
		public GeneralSingleMissionSequencer(Mission mission) {
			super(new PriorityParameters(_prop._priority),
					new StorageParameters(0, 0, 0), mission);
		}
	}

	public abstract class GeneralMissionSequencer extends MissionSequencer {
		public GeneralMissionSequencer() {
			super(new PriorityParameters(_prop._priority),
					new StorageParameters(0, 0, 0));
		}
	}

	public abstract class GeneralPeriodicEventHandler extends
			PeriodicEventHandler {

		public GeneralPeriodicEventHandler() {
			super(new PriorityParameters(_prop._priority),
					new PeriodicParameters(new RelativeTime(_prop._iDelay, 0),
							new RelativeTime(_prop._period, 0)),
					new StorageParameters(_prop._schedObjMemSize, 0, 0));
		}
	}

	public abstract class GeneralAperiodicEventHandler extends
			AperiodicEventHandler {

		public GeneralAperiodicEventHandler() {
			super(new PriorityParameters(_prop._priority),
					new AperiodicParameters(new RelativeTime(_prop._period, 0),
							new AsyncEventHandler()), new StorageParameters(
							_prop._schedObjMemSize, 0, 0));
		}

		public GeneralAperiodicEventHandler(String name) {
			super(new PriorityParameters(_prop._priority),
					new AperiodicParameters(new RelativeTime(_prop._period, 0),
							new AsyncEventHandler()), new StorageParameters(
							_prop._schedObjMemSize, 0, 0), name);
		}
	}

	public class GeneralManagedThread extends ManagedThread {

		public GeneralManagedThread() {
			super(new PriorityParameters(_prop._priority),
					new StorageParameters(0, 0, 0), null);
		}
	}

	public class Terminator extends PeriodicEventHandler {

		public Terminator() {
			super(new PriorityParameters(PriorityScheduler.instance()
					.getMaxPriority()), new PeriodicParameters(
					new RelativeTime(_prop._duration, 0), new RelativeTime(
							Long.MAX_VALUE, 0)), new StorageParameters(0, 0, 0));
		}

		// @Override
		public void handleAsyncEvent() {
			((ManagedMemory) RealtimeThread.getCurrentMemoryArea())
					.getManager().getMission().requestSequenceTermination();
		}
	}
}
