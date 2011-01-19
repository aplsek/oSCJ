
import javax.safetycritical.*;

//import realtime.*;

public class MyHandler extends PeriodicEventHandler {
  public MyHandler(long psize, String name) {
      super(null, null, new StorageParameters(psize, 0 , 0), name);
  }

  public void handleAsyncEvent() {
      Terminal.getTerminal().writeln("MyHandler called.");

      Mission.getCurrentMission().requestSequenceTermination();
  }

    //@Override
    //public void register() {
	  // TODO: Auto-generated method stub
    //	}

  @Override
  public StorageParameters getThreadConfigurationParameters() {
	  // TODO Auto-generated method stub
	  return null;
  }
}
