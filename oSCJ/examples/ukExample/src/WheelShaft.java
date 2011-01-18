import javax.realtime.AperiodicParameters;
import javax.realtime.PriorityParameters;

import javax.safetycritical.*;
import javax.safetycritical.annotate.*;

//import realtime.*;

import static javax.safetycritical.annotate.Level.*;

@SCJAllowed(LEVEL_1)
abstract public class WheelShaft extends AperiodicEventHandler {
  /* needs to read ROM for value */
  private long count;

  public WheelShaft(
      PriorityParameters priority,
      AperiodicParameters release,
      StorageParameters storage) {
    super(priority, release, storage, "WheelShaft");
    count = 0;
  }

  public static int getCallibration() {
    return 100;
  }

  public synchronized long getCount() {
    return count;
  }

  @SCJAllowed(LEVEL_1)
  public void handleAsyncEvent() {
    synchronized (this) {
      /*count = count + getAndClearPendingFireCount();*/
      while (getAndDecrementPendingFireCount() != 0) {
        count++;
      }
    }
    /*System.out.println("WHEEL SHAFT INTERRUPT - Count = " + count);*/
  }
}
