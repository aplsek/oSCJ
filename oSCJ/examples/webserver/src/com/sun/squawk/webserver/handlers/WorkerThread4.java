package com.sun.squawk.webserver.handlers;

import static javax.safetycritical.annotate.Level.LEVEL_1;
import static javax.safetycritical.annotate.Level.SUPPORT;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import static javax.safetycritical.annotate.Scope.CALLER;
import static javax.safetycritical.annotate.Scope.IMMORTAL;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.StreamConnection;
import javax.realtime.RealtimeThread;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.SCJRunnable;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;
import javax.safetycritical.annotate.Scope;

import org.sunspotworld.demo.SynchronizedSocket;
import org.sunspotworld.demo.WebServer;

import com.sun.squawk.webserver.Config;
import com.sun.squawk.webserver.handlers.WorkerThread1.HTTPSession;

@Scope("MyMission")
@SCJAllowed(value=LEVEL_1, members=true)
@DefineScope(name="WorkerThread4", parent="MyMission")
public class WorkerThread4 extends PeriodicEventHandler implements WorkerThread {

    SynchronizedSocket notifier;
    HTTPSession session = new HTTPSession();
    WorkerThread1 next;
    WebServer server;
    
    @SCJRestricted(INITIALIZATION)
    @SCJAllowed(value=LEVEL_1, members=true)
    public WorkerThread4(WebServer server, SynchronizedSocket notifier) {
        super(Config.priority, Config.period, Config.storage, "Worker-"
                + WorkerThreadConfig.workerCounter++);
       /* 
        super(new PriorityParameters(Config.priority),
                new PeriodicParameters(new RelativeTime(0, 0), new RelativeTime(
                        Config.period, 0)),
                        new StorageParameters(Config.storage, 1000L, 1000L),
                        "Worker-" + workerCounter++);
        */
        this.server = server;
        this.notifier = notifier;
    }

    @RunsIn("WorkerThread4")
    @SCJAllowed(SUPPORT)
    public void handleAsyncEvent() {
        
        @Scope("MyMission")
        @DefineScope(name="WorkerThread4", parent="MyMission")
        ManagedMemory mm = (ManagedMemory) RealtimeThread.getCurrentMemoryArea();
        while (true) {
            mm.enterPrivateMemory(Config.privateSize, session);
            // BackingStore.printCurrentBSStats();
        }
    }
    
    @SCJAllowed(value=LEVEL_1, members=true)
    @DefineScope(name="HTTPSession4", parent="WorkerThread4")
    class HTTPSession implements SCJRunnable {

        @RunsIn("HTTPSession4")
        @SCJAllowed(SUPPORT)
        @SCJRestricted(maySelfSuspend = true)
        public void run() {
            debug(getName() + " is running ...");

            StreamConnection conn = null;
            try {
                conn = notifier.acceptAndOpen(getName());
            } catch (IOException e) {
                debug("Error in accept:");
                //e.printStackTrace();
            }

            InputStream is;
            OutputStream os;
            server.openConnections++;
            try {
                long start = System.currentTimeMillis();
                // debug(getName() + " handles request starting @ " +
                // System.currentTimeMillis());

                is = conn.openInputStream();
                os = conn.openOutputStream();
                server.server.handleRequest(is, os);
                os.write(0);
                os.flush();

                // debug(getName() + " handles request finished @ " +
                // System.currentTimeMillis());
                debug(getName() + ": service takes " + (System.currentTimeMillis() - start));
                is.close();
                os.close();
            } catch (IOException e) {
                debug("Closing connection!");
                //e.printStackTrace();
            }
            try {
                conn.close();
                server.openConnections--;
            } catch (IOException ioe) {
                debug("Error in closing connection!");
                //ioe.printStackTrace();
            }

//            if (Config.DEBUG)
//                BackingStore.printBSTree(true);
        }
    }

    @RunsIn(CALLER)
    private static void debug(String s) {
      //  System.err.println(s);
    }
}
