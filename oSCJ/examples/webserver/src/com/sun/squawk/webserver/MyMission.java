package com.sun.squawk.webserver;

import static javax.safetycritical.annotate.Level.LEVEL_1;
import static javax.safetycritical.annotate.Level.SUPPORT;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import static javax.safetycritical.annotate.Scope.IMMORTAL;

import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnectionNotifier;
import javax.safetycritical.Mission;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;
import javax.safetycritical.annotate.Scope;

import org.sunspotworld.demo.SynchronizedSocket;
import org.sunspotworld.demo.WebServer;

import com.sun.squawk.webserver.handlers.WorkerThread1;

@Scope("MyMission")
@SCJAllowed(value = LEVEL_1, members = true)
public class MyMission extends Mission {

    WebServer server;

    private int port = 8080;

    // Runs in immortal memory
    @SCJRestricted(INITIALIZATION)
    public MyMission() {
        try {
            server = new WebServer(port);
        } catch (IOException e) {
            // System.err.println("Creating server ");
            // e.printStackTrace();
        }
    }

    @SCJRestricted(INITIALIZATION)
    @SCJAllowed(SUPPORT)
    protected void initialize() {
        try {
            SynchronizedSocket notifier = new SynchronizedSocket(
                    (StreamConnectionNotifier) Connector.open("socket://:"
                            + port));
            new WorkerThread1(server, notifier);
            //new WorkerThread2(server, notifier);
            //new WorkerThread3(server, notifier);
            //new WorkerThread4(server, notifier);

            // new AsyncHapHandler(Config.priority, Config.aperiod,
            // Config.storage,
            // Config.initPrivateSize);
        } catch (IOException e) {
            //System.err.println("Server initialization ");
            // e.printStackTrace();
        }
    }

    public long missionMemorySize() {
        return Config.missionMemSize;
    }
}
