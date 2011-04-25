package com.sun.squawk.webserver.handlers;

import static javax.safetycritical.annotate.Level.LEVEL_1;

import javax.safetycritical.annotate.SCJAllowed;


@SCJAllowed(value=LEVEL_1, members=true)
public class WorkerThreadConfig {
    static int workerCounter = 0;
}
