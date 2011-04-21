package javax.safetycritical.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.StreamConnection;
import javax.safetycritical.annotate.SCJAllowed;

/** A connection for the default I/O device. */
@SCJAllowed
public class ConsoleConnection implements StreamConnection
{
  /** Create a new object of this type. */
  @SCJAllowed
  ConsoleConnection(String name) throws ConnectionNotFoundException
  {
  }

  @Override
  @SCJAllowed
  public void close() throws IOException
  {
  }

  @Override
  @SCJAllowed
  public InputStream openInputStream()
    throws IOException
  {
    return null;
  }

  /* (non-Javadoc)
   * @see javax.microedition.io.OutputConnection#openOutputStream()
   */
  @Override
  @SCJAllowed
  public OutputStream openOutputStream()
      throws IOException
  {
    return null;
  }
}
