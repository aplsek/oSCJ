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


public class RawMemoryHandler extends PeriodicEventHandler {

	private int count_;

	public RawMemoryHandler(long psize, String name, int count) {

		super(null, null, new StorageParameters(psize, 1000L, 1000L), name);

		count_ = count;
	}

	/**
	 * 
	 * Testing Enter Private Memory
	 * 
	 */
	public void handleAsyncEvent() {
		Terminal.getTerminal().write(getName());
		
		//String input = "/dev/ttyUSB0";
		//String input = "/dev/ttyS2";
		String input = "./input";


		Terminal.getTerminal().write("[SCJ-APP] byte-char test\n");
		
		

		Terminal.getTerminal().write("------------\n\n");
        
        
		Terminal.getTerminal().write("[SCJ-APP] Write2Serial test:\n");
		writeToSerial(input);
		
		Terminal.getTerminal().write("[SCJ-APP] ReadFromSerial test:\n");
		readFromSerial(input);
        
		
		
		
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

        // TODO: we should be testing the return value from the writeByte()
        byte b = 'h';
        byte end = '\n';
        int i = 0;
        i += RawMemoryAccess.writeByte(fd,b);
        b= 'e';
        i += RawMemoryAccess.writeByte(fd,b);
        b = 'l';
        i += RawMemoryAccess.writeByte(fd,b);
        i += RawMemoryAccess.writeByte(fd,b);
        b = 'o';
        i += RawMemoryAccess.writeByte(fd,b);
        i += RawMemoryAccess.writeByte(fd,end);
    
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
