package purdue;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.realtime.AbsoluteTime;
import javax.realtime.Clock;
import javax.realtime.ImmortalMemory;
import javax.realtime.MemoryArea;
import javax.realtime.RawIntegralAccess;
import javax.realtime.RawIntegralAccessFactory;
import javax.realtime.RawMemoryName;
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

import edu.purdue.scj.VMSupport;

public class RawMemoryHandler extends PeriodicEventHandler {

    private int count_;

    public RawMemoryHandler(long psize, String name, int count) {

        super(null, null, new StorageParameters(psize, 1000L, 1000L), name);

        count_ = count;
    }

    public void handleAsyncEvent() {
        try {

            LED_RawMemoryName ledName = new LED_RawMemoryName("LED");
            
            // The following should be called by INFRASTRUCTURE:
            LEDAccessFactory ledFactory = new LEDAccessFactory(ledName);
            RawMemory.registerAccessFactory(ledFactory);
            
            // LED address, see also LEDAccessFactory class for more.
            final long ADDRESS = 0;
            final long SIZE = 0x10000;

            // create the RawMemoryAccess
            RawIntegralAccess led0 = RawMemory.createRawIntegralInstance(
                    ledName, ADDRESS, SIZE);

            // access the RawMemory:
            led0.setByte(0, 1); // turn the LED ON
            led0.setByte(0, 0); // turn the LED OFF

            led0.close(); // unmap the device and close the file-descriptor.

        } catch (Exception e) {
            e.printStackTrace();
        }

        // END
        Mission.getCurrentMission().requestSequenceTermination();
    }

    public void cleanUp() {
        Terminal.getTerminal().write("Handler: clean up.\n");
    }

    public StorageParameters getThreadConfigurationParameters() {
        return null;
    }
}

class RawMemory {

    private static HashMap<RawMemoryName, RawIntegralAccessFactory> factories = new HashMap();

    public static RawIntegralAccess createRawIntegralInstance(
            RawMemoryName name, long base, long size) throws Exception {
        RawIntegralAccessFactory factory = factories.get(name);
        if (factory == null)
            throw new Exception(
                    "ERR: No factory for give RawMemoryName available.");

        return factory.newIntegralAccess(base, size);
    }

    // This is VM-specific and should be called by the INFRASTRUCTURE
    public static void registerAccessFactory(RawIntegralAccessFactory factory) {
        if (factories.containsKey(factory.getName()))
            throw new IllegalArgumentException(
                    "The factory has already been registered.");
        factories.put(factory.getName(), factory);
    }

}

class LEDAccessFactory implements RawIntegralAccessFactory {
    String FILE = "/dev/mem";
    final long ADDRESS = 0;
    final long OFFSET = 0x49050000;
    final long SIZE = 0x10000;

    final int FLAGS = 2; // O_RDWR
    final int MODE = 6322;

    RawMemoryName name;

    public LEDAccessFactory(RawMemoryName n) {
        name = n;
    }

    @Override
    public RawMemoryName getName() {
        return name;
    }

    /**
     *   Calls the VM specific methods to map the physical device into the virtual memory.
     **/ 
    @Override
    public RawIntegralAccess newIntegralAccess(long base, long size)
            throws Exception {

        int fd = VMSupport.open(FILE, FLAGS, MODE);
        if (fd < 0) {
            throw new Exception("ERR: Failed to intitiate the RawMemoryAccess.");
        }

        int address = VMSupport.mmap(ADDRESS, SIZE, 0, FLAGS, fd, OFFSET);
        if (address < 0) {
            throw new Exception("ERR: Failed to intitiate the RawMemoryAccess.");
        }

        return new LED_RawIntegralAccess(address);
    }
}

class LED_RawMemoryName implements RawMemoryName {
    final String factoryName;

    public LED_RawMemoryName(String factoryName) {
        this.factoryName = factoryName;
    }
}

class LED_RawIntegralAccess implements RawIntegralAccess {

    final int ADDRESS;

    public LED_RawIntegralAccess(int id) {
        this.ADDRESS = id;
    }

    @Override
    public byte getByte(long offset) {
        return VMSupport.getByte(ADDRESS, offset);
    }

    @Override
    public void setByte(long offset, byte value) {
        VMSupport.setByte(ADDRESS, offset, value);
    }

    public void close() {
        // TODO:...
    }

    @Override
    public void getBytes(long offset, byte[] bytes, int low, int number) {
    }

    @Override
    public int getInt(long offset) {
        return 0;
    }

    @Override
    public void getInts(long offset, int[] ints, int low, int number) {
    }

    @Override
    public long getLong(long offset) {
        return 0;
    }

    @Override
    public void getLongs(long offset, long[] longs, int low, int number) {

    }

    @Override
    public short getShort(long offset) {
        return 0;
    }

    @Override
    public void getShorts(long offset, short[] shorts, int low, int number) {
    }

    @Override
    public void setBytes(long offset, byte[] bytes, int low, int number) {
    }

    @Override
    public void setInt(long offset, int value) {
    }

    @Override
    public void setInts(long offset, int[] its, int low, int number) {
    }

    @Override
    public void setByte(long offset, long value) {
    }

    @Override
    public void setLongs(long offset, long[] longs, int low, int number) {
    }

    @Override
    public void setShort(long offset, short value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setShorts(long offset, short[] shorts, int low, int number) {
        // TODO Auto-generated method stub

    }

}
