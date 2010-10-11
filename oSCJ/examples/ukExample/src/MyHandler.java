
import javax.safetycritical.*;

import realtime.*;

public class MyHandler extends PeriodicEventHandler {
  public MyHandler(long psize, String name) {
	  super(null, null, null, psize, name);
  }

  public void handleEvent() {
      Terminal.getTerminal().writeln("MyHandler called.");

      Mission.getCurrentMission().requestSequenceTermination();
  }

  @Override
  public void register() {
	  // TODO: Auto-generated method stub
	}

  @Override
  public StorageParameters getThreadConfigurationParameters() {
	  // TODO Auto-generated method stub
	  return null;
  }
}
