package purdue;
import javax.realtime.AbsoluteTime;
import javax.realtime.Clock;
import javax.realtime.ImmortalMemory;
import javax.realtime.MemoryArea;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.safetycritical.CyclicExecutive;
import javax.safetycritical.CyclicSchedule;
import javax.safetycritical.MissionManager;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.Safelet;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.Terminal;
import javax.safetycritical.Mission;

import javax.realtime.RawMemoryAccess;


public class SerialCommunicationHandler extends PeriodicEventHandler {

	private int count_;

	public SerialCommunicationHandler(long psize, String name, int count) {
		super(null, null, new StorageParameters(psize, 1000L, 1000L), name);

		count_ = count;
	}
	
	//String input = "/dev/ttyUSB0";
    //String input = "/dev/ttyS2";
    String file = "/dev/mem";
    
    
    
	public void handleAsyncEvent() {
	    
		Terminal.getTerminal().write(getName());
		
		Terminal.getTerminal().write("[SCJ-APP] byte-char test\n");
		Terminal.getTerminal().write("------------\n\n");
        
        
		Terminal.getTerminal().write("[SCJ-APP] Write2Serial test:\n");
		writeToSerial(file);
		
		Terminal.getTerminal().write("[SCJ-APP] ReadFromSerial test:\n");
		readFromSerial(file);
        
		
		
		// END
		Mission.getCurrentMission().requestSequenceTermination();
		Terminal.getTerminal().write("Handler: end\n");
	}
	
	
	private void writeToSerial(String input) {

        int fd = RawMemoryAccess.open(input, RawMemoryAccess.O_RDWR, 6322);
        if (fd < 0 ) {
            System.out.println("RAW: ERROR: " + fd);
            Mission.getCurrentMission().requestSequenceTermination();
            return;
        } else
            Terminal.getTerminal().write("RAW Open-File: OK, value:" + fd);

        int i = 0;
        String str = "hello\n";
        for (byte b: str.getBytes()) {
            // TODO: we should be testing the return value from the writeByte()
            i += RawMemoryAccess.writeByte(fd,b);
        }
    
        Terminal.getTerminal().write("[SCJ] Wrote bytes:" + i);
        
        RawMemoryAccess.close(fd);        
	}
	
	private void readFromSerial(String input) {

        int fd = RawMemoryAccess.open(input, RawMemoryAccess.O_RDWR, 6322);
        if (fd < 0 ) {
            Terminal.getTerminal().write("RAW: ERROR: " + fd);
            Mission.getCurrentMission().requestSequenceTermination();
            return;
        } else
            Terminal.getTerminal().write("RAW Open-File: OK, value: " + fd);

        byte b = RawMemoryAccess.readByte(fd);
        while(b > 0) {
            char c = (char) b;
            Terminal.getTerminal().write("Raw READ:"+b);
            Terminal.getTerminal().write("  , (char):"+c + "\n");
            b = RawMemoryAccess.readByte(fd);
        }
        
      
    
        RawMemoryAccess.close(fd);        
	}

	public void cleanUp() {
	    Terminal.getTerminal().write("Handler: clean up.\n");
	}

	public StorageParameters getThreadConfigurationParameters() {
		return null;
	}
}
