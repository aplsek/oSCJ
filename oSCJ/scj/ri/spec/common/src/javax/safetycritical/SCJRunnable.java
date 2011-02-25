package javax.safetycritical;

import javax.safetycritical.annotate.SCJAllowed;
import static javax.safetycritical.annotate.Level.SUPPORT;

@SCJAllowed
public interface SCJRunnable {
    @SCJAllowed(SUPPORT)
    public void run();
}
