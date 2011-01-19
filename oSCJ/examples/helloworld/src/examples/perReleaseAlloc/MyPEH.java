//
package examples.perReleaseAlloc;

import java.util.Arrays;
import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.Scope;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.Mission;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;

@Scope("examples.perReleaseAlloc.MyApp")
public class MyPEH extends PeriodicEventHandler {

	static PriorityParameters pri;
	static PeriodicParameters per;
	static StorageParameters stor;

	static {
		pri = new PriorityParameters(13);
		per = new PeriodicParameters(new RelativeTime(0, 0),
				new RelativeTime(500, 0));
		stor = new StorageParameters(1000L, 1000L, 1000L);
	}

	public MyPEH() {
		super(pri, per, stor);
	}

	long median;
	
	@RunsIn("examples.perReleaseAlloc.MyPEH")
	public void handleAsyncEvent() {
		final long times[] = new long[1000];
		Runnable r = new Runnable(){
			public void run() {
				long[] copy = new long[1000];
				for(int i=0;i<1000;i++)  copy[i]=times[i];
				Arrays.sort(copy);
				median = copy[500];
			}
		};
		ManagedMemory m = ManagedMemory.getCurrentManagedMemory();
		m.enterPrivateMemory(8000, r);


		Mission.getCurrentMission().requestSequenceTermination();
	}

	public void cleanUp() {
	}

	public StorageParameters getThreadConfigurationParameters() {
		return null;
	}
}
