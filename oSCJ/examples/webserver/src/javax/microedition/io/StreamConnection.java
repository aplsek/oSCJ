package javax.microedition.io;

import static javax.safetycritical.annotate.Level.LEVEL_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.safetycritical.annotate.SCJAllowed;

@SCJAllowed(value=LEVEL_1, members=true)
public class StreamConnection {

    public OutputStream openOutputStream() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public InputStream openInputStream() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public void close() throws IOException {
        // TODO Auto-generated method stub

    }

}
