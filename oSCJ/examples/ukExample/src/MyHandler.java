import javax.realtime.*;
import javax.safetycritical.*;

public class MyHandler extends PeriodicEventHandler {
  
 public MyHandler(long psize, String name) {
    //"MyHandler"
	// super(new PriorityParameters(5),
    //  new PeriodicParameters(null, new RelativeTime(1000, 0)),
    //  new StorageConfigurationParameters(), 65535, name);
	 
	 super(null, null, null, psize, name);
  }

  public void handleEvent() {
      Terminal.getTerminal().writeln("MyHandler called.");
      
      Mission.getCurrentMission().requestSequenceTermination();
  }

@Override
public void register() {
	// TODO Auto-generated method stub
	
}

@Override
public StorageParameters getThreadConfigurationParameters() {
	// TODO Auto-generated method stub
	return null;
}
}
