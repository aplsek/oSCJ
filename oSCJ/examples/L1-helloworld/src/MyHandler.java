import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.io.SimplePrintStream;
class MyHandler extends PeriodicEventHandler {
        public MyHandler(SimplePrintStream out, PriorityParameters prio, PeriodicParameters peri, StorageParameters stor) 
	{super(prio, peri, stor);
	myout = out;
	}
	int cnt;
		SimplePrintStream myout;
			
		public void handleAsyncEvent(){
			myout.println("Ping " + cnt);
			++cnt;
			}
		}