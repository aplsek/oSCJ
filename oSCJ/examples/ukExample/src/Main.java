
import javax.safetycritical.*;

//import realtime.*;

public class Main {
  public static void main(final String[] args) {
    Safelet safelet = new MyCyclicExecutive();
    safelet.setUp();
    safelet.getSequencer().start();
    safelet.tearDown();
  }
}
