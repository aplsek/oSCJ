import javax.realtime.RelativeTime;
import javax.safetycritical.CyclicExecutive;
import javax.safetycritical.CyclicSchedule;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.Terminal;


public class MyCyclicExecutive extends CyclicExecutive {

	public MyCyclicExecutive() {
		super(null);
	}

	@Override
	public void setUp() {
	}

	@Override
	public void tearDown() {
	}

	@Override
	public CyclicSchedule getSchedule(PeriodicEventHandler[] peh) {
		CyclicSchedule.Frame[] frames = new CyclicSchedule.Frame[1];
        CyclicSchedule schedule = new CyclicSchedule(frames);
        frames[0] = new CyclicSchedule.Frame(new RelativeTime(200, 0), peh);
        return schedule;
	}

	@Override
	public long missionMemorySize() {
		return 100000;
	}

	@Override
	protected void initialize() {
		Terminal.getTerminal().writeln("Initialising Mission.");
		new MyHandler(20000, "MyHandler.\n");
	}

}
