import javax.realtime.*;

import javax.safetycritical.*;

public class Main {
  public static void main(final String[] args) {
    Safelet safelet = new MyCyclicExecutive();
    safelet.setUp();
    safelet.getSequencer().start();
    safelet.tearDown();
  }
}
