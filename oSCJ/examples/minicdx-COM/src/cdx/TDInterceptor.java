package cdx;

import javax.realtime.*;

public class TDInterceptor implements ITransientDetector {

    ITransientDetector itd;
    private static LTMemory transientDetector;

    public TDInterceptor(ITransientDetector itd) {
	this.itd = itd;
	transientDetector = new LTMemory(Constants.PERSISTENT_DETECTOR_SCOPE_SIZE, Constants.PERSISTENT_DETECTOR_SCOPE_SIZE);

    }

    public void runDetectorInScope() {
	//System.out.println("interceptor...");
	itd.runDetectorInScope();
    }

}