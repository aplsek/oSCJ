/*
 * Copyright 2005 Sun Microsystems, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of Sun Microsystems, Inc.
 * Use is subject to license terms.
 *
 * This is a part of the Squawk JVM.
 */
package org.sunspotworld.demo;

import static javax.safetycritical.annotate.Level.LEVEL_1;

import java.io.IOException;

import javax.safetycritical.annotate.SCJAllowed;

@SCJAllowed(value=LEVEL_1, members=true)
public class WebServer {

    public NanoHTTP server;
    public int openConnections;
    private int port;

    public WebServer(int port) throws IOException {
        this.port = port;
        server = new NanoHTTP();
        server.addApplication("/", new AppListServer("iPod Touch"));
        server.addApplication("/about", new AboutServer("iPod Touch"));
        server.addApplication("/stats", new VMStatsServer("iPod Touch"));
        server.addApplication("/files", new FileServer());
    }
}
