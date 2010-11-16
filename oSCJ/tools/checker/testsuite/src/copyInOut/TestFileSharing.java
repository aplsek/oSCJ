package copyInOut;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.CrossScope;
import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.Scope;

// PLAN: open a file
//       share it with SOs
//
@Scope("copyInOut.TestFileSharing")
public class TestFileSharing extends Mission {

	public MyOutputStream output;

	protected void initialize() {
		new MyHandler(null, null, null, 0);

		// init the output stream
	}

	@Override
	public long missionMemorySize() {
		return 0;
	}

	@Scope("copyInOut.TestFileSharing")
	@RunsIn("copyInOut.MyHandler")
	class MyHandler extends PeriodicEventHandler {

		public MyHandler(PriorityParameters priority,
				PeriodicParameters parameters, StorageParameters scp,
				long memSize) {
			super(priority, parameters, scp, memSize);
		}

		@Override
		public StorageParameters getThreadConfigurationParameters() {
			return null;
		}

		@Scope("Unknown") MyOutputStream myOutput;

		@Override
		public void handleEvent() {
			TestFileSharing mission = (TestFileSharing) Mission.getCurrentMission();

			myOutput = mission.output;

			byte[] b;  //  = ... initialize";
			myOutput.write(b);
		}
	}
}


class MyOutputStream {
	@CrossScope
	public void write(byte[] b) {

	}

	@LivesIn("Unknown")
	public FileChannel getChannel() {
		return null;
	}
}


class FileChannel {

}


