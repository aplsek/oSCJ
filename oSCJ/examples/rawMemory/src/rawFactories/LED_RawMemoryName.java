package rawFactories;

import javax.realtime.RawMemoryName;

public class LED_RawMemoryName implements RawMemoryName {

    String FILE = "/dev/mem";
    final long ADDRESS = 0;
    final long SIZE = 0x10000;
    final long OFFSET = 0x49050000; 
    
}
