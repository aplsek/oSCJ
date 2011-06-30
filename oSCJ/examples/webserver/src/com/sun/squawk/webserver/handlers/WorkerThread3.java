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
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;
import javax.safetycritical.annotate.Scope;

import org.sunspotworld.demo.SynchronizedSocket;
import org.sunspotworld.demo.WebServer;

import com.sun.squawk.webserver.Config;


@Scope("MyMission")
@SCJAllowed(value=LEVEL_1, members=true)
@DefineScope(name="WorkerThread3", parent="MyMission")
public class WorkerThread3 extends PeriodicEventHandler implements WorkerThread {

    WebServer server;
    SynchronizedSocket notifier;
    HTTPSession session = new HTTPSession();
    WorkerThread3 next;

    @SCJRestricted(INITIALIZATION)
    public WorkerThread3(WebServer server, SynchronizedSocket notifier) {
        super(new PriorityParameters(Thread.NORM_PRIORITY), 
                new PeriodicParameters(new RelativeTime(Config.period_rel_0ms_0ns, 0), 
                        new RelativeTime(Config.period_max,0)), 
                new StorageParameters(Config.threadBackStoreSize, 0,
                        Config.javaStackSize),
                "Worker-"
                + WorkerThreadConfig.workerCounter++);
        this.server = server;
        this.notifier = notifier;
    }

    @RunsIn("WorkerThread3")
    @SCJAllowed(SUPPORT)
    public void handleAsyncEvent() {
        
        @Scope("MyMission")
        @DefineScope(name="WorkerThread3", parent="MyMission")
        ManagedMemory mm = (ManagedMemory) RealtimeThread.getCurrentMemoryArea();
        while (true) {
            mm.enterPrivateMemory(Config.privateSize, session);
            // BackingStore.printCurrentBSStats();
        }
    }

    @Scope("MyMission")
    @SCJAllowed(value=LEVEL_1, members=true)
    @DefineScope(name="HTTPSession3", parent="WorkerThread3")
    class HTTPSession implements Runnable {

        @RunsIn("HTTPSession3")
        @SCJRestricted(maySelfSuspend = true)
        public void run() {
            debug(getName() + " is running ...");

            StreamConnection conn = null;
            try {
                String string = new String(getName());
                conn = notifier.acceptAndOpen(string);
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
        //System.err.println(s);
    }
}
