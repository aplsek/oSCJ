package org.sunspotworld.demo;

import static javax.safetycritical.annotate.Level.LEVEL_1;
import static javax.safetycritical.annotate.Scope.CALLER;
import java.io.IOException;
import javax.safetycritical.annotate.RunsIn;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import javax.safetycritical.annotate.SCJAllowed;

@SCJAllowed(value=LEVEL_1, members=true)
public class SynchronizedSocket {
    StreamConnectionNotifier notifier;

    public SynchronizedSocket(StreamConnectionNotifier notifier) {
        this.notifier = notifier;
    }

    @RunsIn(CALLER)
    public synchronized StreamConnection acceptAndOpen(String who) throws IOException {
        //System.err.print(who);
        //System.err.println(" is listenning ...");
        StreamConnection ret = notifier.acceptAndOpen();
        //System.err.print(who);
        //System.err.println(" gets a request ...");
        return ret;
    }
}
