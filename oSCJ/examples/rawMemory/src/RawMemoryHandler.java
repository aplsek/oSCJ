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

        int fd = RawMemoryAccess.open(input, 0, 6322);
        if (fd < 0 ) {
            System.out.println("RAW: ERROR: " + fd);
            Mission.getCurrentMission().requestSequenceTermination();
            return;
        } else
            System.out.println("RAW: OK!!, value:" + fd);


        byte b = 'h';
        byte end = '\n';
        RawMemoryAccess.writeByte(fd,b);
        b= 'e';
        RawMemoryAccess.writeByte(fd,b);
        b = 'l';
        RawMemoryAccess.writeByte(fd,b);
        RawMemoryAccess.writeByte(fd,b);
        b = 'o';
        RawMemoryAccess.writeByte(fd,b);
        RawMemoryAccess.writeByte(fd,end);
    
        RawMemoryAccess.close(fd);        
	}
	
	private void readFromSerial(String input) {

        int fd = RawMemoryAccess.open(input, 0, 6322);
        if (fd < 0 ) {
            System.out.println("RAW: ERROR: " + fd);
            Mission.getCurrentMission().requestSequenceTermination();
            return;
        } else
            System.out.println("RAW: OK!!, value:" + fd);

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
