package com.sun.squawk.test;

import static javax.safetycritical.annotate.Level.LEVEL_1;
import static javax.safetycritical.annotate.Level.SUPPORT;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import static javax.safetycritical.annotate.Scope.IMMORTAL;

import java.io.IOException;

import javax.safetycritical.Mission;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;
import javax.safetycritical.annotate.Scope;

import org.sunspotworld.demo.WebServer;


@Scope(">")
@SCJAllowed(value=LEVEL_1, members=true)
public class MyMission extends Mission {

    WebServer server;

    // Runs in immortal memory
    @SCJRestricted(INITIALIZATION)
    public MyMission() {
        try {
            server = new WebServer(8080);
        } catch (IOException e) {
            System.err.println("Creating server ");
            e.printStackTrace();
        }
    }

    @SCJRestricted(INITIALIZATION)
    @SCJAllowed(SUPPORT)
    protected void initialize() {
        try {
            server.initialize();
//            new AsyncHapHandler(Config.priority, Config.aperiod, Config.storage,
//                    Config.initPrivateSize).register();
        } catch (IOException e) {
            System.err.println("Server initialization ");
            //e.printStackTrace();
        }
    }

    public long missionMemorySize() {
        return Config.missionMemSize;
    }
}
