package javax.microedition.io;

import static javax.safetycritical.annotate.Level.LEVEL_1;
import static javax.safetycritical.annotate.Scope.CALLER;

import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.SCJAllowed;

@SCJAllowed(value=LEVEL_1, members=true)
public class StreamConnectionNotifier {

    @RunsIn(CALLER)
    public StreamConnection acceptAndOpen() {
        // TODO Auto-generated method stub
        return null;
    }

}
