package rawFactories;

import javax.realtime.RawMemoryName;

public class SerialPort_RawMemoryName implements RawMemoryName {

    String FILE = "/dev/mem";
    final long ADDRESS = 0;
    final long SIZE = 0x10000;
    final long OFFSET = 0x49050000; 
    
    final int flags =  0x2;   //  O_RDWR
    final int mode = 6322;
}
