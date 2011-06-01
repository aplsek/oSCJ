import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;

class MyHandler extends PeriodicEventHandler {
	public MyHandler(PriorityParameters prio, PeriodicParameters peri, StorageParameters stor) 
	{super(prio, peri, stor);
	//out = myout;
	}
	int cnt;
	//	PrintStream out;
			
		public void handleAsyncEvent(){
			HelloSCJ.out.println("Ping " + cnt);
			++cnt;
			}
		}