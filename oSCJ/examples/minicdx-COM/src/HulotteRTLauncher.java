

import javax.realtime.PriorityParameters;
import javax.realtime.PriorityScheduler;
import javax.realtime.RealtimeThread;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.api.factory.Factory;
import org.objectweb.fractal.api.factory.InstantiationException;
import org.objectweb.fractal.util.Fractal;

/**
 * <p>
 * The class provides a {@link #main(String[])} method for launching a Fractal
 * component compiled with Juliac.
 * </p>
 * 
 * <p>
 * The first command line argument must be the fully-qualified name of the
 * component to instantiate. If present, the second command line argument
 * designates the name of a {@link Runnable} server interface provided by the
 * component which is invoked by this program.
 * </p>
 */
public class HulotteRTLauncher {
    
    /**
     * Entry point for this program.
     */
    public static void main(final String[] args)
    throws
        ClassNotFoundException, InstantiationException, IllegalAccessException,
        java.lang.InstantiationException, IllegalArgumentException,
        NoSuchInterfaceException, IllegalLifeCycleException {
        
        if( args.length==0 || args.length>2 ) {
            usage();
            return;
        }
       
        /*
         * Retrieve the factory.
         */
        final String adl = args[0];
        Class<?> cl = Class.forName(adl);
        final Object o = cl.newInstance();
        
        // Set the maximum priority for the main thread
        PriorityParameters pp = new PriorityParameters(PriorityScheduler.instance().getMinPriority());
        
        RealtimeThread rt = new RealtimeThread(pp) {
        	public void run() {
        		if( o instanceof Factory ) {
        			
        			System.out.print("Main thread launched ");
        			System.out.println("with parameters: id="+Thread.currentThread().getId()+" priority="+Thread.currentThread().getPriority());

        			Factory factory = (Factory) o;
        			Component root = null;
					try {
						root = factory.newFcInstance();
					} catch (InstantiationException e) {
						e.printStackTrace();
						System.exit(-1);
					}
        			try {
						Fractal.getLifeCycleController(root).startFc();
					} catch (IllegalLifeCycleException e) {
						e.printStackTrace();
						System.exit(-1);
					} catch (NoSuchInterfaceException e) {
						System.exit(-1);
						e.printStackTrace();
					}
					
        			// If an interface name has been specified in the command line
        			// arguments, invoke it.
        			if( args.length == 2 ) {
        				String itfname = args[1];        
        				Object itf = null;
						try {
							itf = root.getFcInterface(itfname);
						} catch (NoSuchInterfaceException e) {
							e.printStackTrace();
							System.exit(-1);
						}
        				if( itf instanceof Runnable ) {
	        				((Runnable)itf).run();
        				}
        				else {
        					String msg =
        						"The "+itfname+
        						" interface should implement java.lang.Runnable or cd.RunnableMainArgs";
        					throw new IllegalArgumentException(msg);
        				}
        			}
        		}
        		else {
        			// Not a Fractal factory. Assume the class implements Runnable.
        			if( o instanceof Runnable ) {
        				((Runnable)o).run();
        			}
        			else {
        				String msg = "Illegal factory class: "+adl;
        				throw new IllegalArgumentException(msg);
        			}
        		}
        	}
        };
        
        rt.start();
        try {
			rt.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// FIXME: find a better way to stop the main thread
//		try {
//			Thread.currentThread().sleep(15000);
//			
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
    }
    
    /**
     * Display the usage information for this program.
     */
    public static void usage() {
        System.out.println("Usage: java "+HulotteRTLauncher.class.getName()+" factory [itfname]");
        System.out.println("where:");
        System.out.println("- factory is the fully-qualified name of a Fractal Factory");
        System.out.println("- itfname is the name of a Runnable Fractal interface");
    }
    
}
