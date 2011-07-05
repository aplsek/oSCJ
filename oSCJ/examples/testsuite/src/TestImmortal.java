import javax.realtime.ImmortalMemory;
import javax.realtime.MemoryArea;
import javax.realtime.RelativeTime;
import javax.safetycritical.CyclicExecutive;
import javax.safetycritical.CyclicSchedule;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.Safelet;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.Terminal;

import edu.purdue.scj.VMSupport;

public class TestImmortal extends CyclicExecutive {

    public TestImmortal() {
        super(null);
    }

    //    public static void main(final String[] args) {
    //  Safelet safelet = new TestImmortal();
    //  safelet.setUp();
    //  safelet.getSequencer().start();
    //  safelet.tearDown();
    //    }

    private static void writeln(String msg) {
        Terminal.getTerminal().writeln(msg);
    }

    public CyclicSchedule getSchedule(PeriodicEventHandler[] handlers) {
        CyclicSchedule.Frame[] frames = new CyclicSchedule.Frame[1];
        CyclicSchedule schedule = new CyclicSchedule(frames);
        frames[0] = new CyclicSchedule.Frame(new RelativeTime(200, 0), handlers);
        return schedule;
    }

    boolean testResult = false;

    public void initialize() {
        new WordHandler(20000, "Immortal Memory test.\n", 1);
    }

    /**
     * A method to query the maximum amount of memory needed by this mission.
     * 
     * @return the amount of memory needed
     */
    public long missionMemorySize() {
        return 500000;
    }

    /**
     * This code should be running in IMMORTAL
     * 
     * Spec says: "... The infrastructure invokes setUp before invoking getSequencer.
     * Application developers place code to be executed before the
     * MissionSequencer begins to execute within this method. Upon entry into
     * this method, the current allocation context is ImmortalMemoryArea. User
     * code may introduce nested PrivateMemory areas for temporary computations.
     * ..."
     */
    public void setUp() {

        VMSupport.setCurrentArea(VMSupport.getImmortalArea());

        // checking the instance of immortal memory
        MemoryArea immortal = ImmortalMemory.instance();
        long im = immortal.memoryConsumed();
        long im_VM = VMSupport.memoryConsumed(VMSupport.getImmortalArea());
        long curr_VM = VMSupport.memoryConsumed(VMSupport.getCurrentArea());

        // checking the size of immortal memory
        long sizeIM = immortal.size();
        long size_VM = VMSupport.getScopeSize(VMSupport.getImmortalArea());
        long size_CURR = VMSupport.getScopeSize(VMSupport.getCurrentArea());

        if (im == im_VM && im_VM == curr_VM)
            testResult = true;

        if (sizeIM == size_VM && size_VM == size_CURR)
            testResult = true;

        if (!testResult) {
            System.out.println("im : " + im);
            System.out.println("im_VM : " + im_VM);
            System.out.println("curr_VM : " + curr_VM);

            System.out.println("sizeIM : " + sizeIM);
            System.out.println("size_VM : " + size_VM);
            System.out.println("size_CURR : " + size_CURR);
        }

    }

    public void tearDown() {
    }

    public void cleanUp() {
    }

    public class WordHandler extends PeriodicEventHandler {

        private int count_;

        private WordHandler(long psize, String name, int count) {
        	super(null, null, new StorageParameters(psize,0,0), name);
            count_ = count;
        }

        /**
         * 
         * Testing Enter Private Memory
         * 
         */
        public void handleAsyncEvent() {
            Terminal.getTerminal().write("Immortal Memory Test : START\n");
            if (testResult)
                Terminal.getTerminal().write("Immortal Memory test OK.\n");
            else
                Terminal.getTerminal().write("Immortal Memory Test: FAILED.\n");

            Terminal.getTerminal().write("Immortal Memory Test : END\n");

            getCurrentMission().requestSequenceTermination();
        }

        public void cleanUp() {
        }

        public StorageParameters getThreadConfigurationParameters() {
            return null;
        }
    }

}
