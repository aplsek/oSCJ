package cdx;

import javax.realtime.*;

public class TDInterceptor implements ITransientDetector, Runnable {

	ITransientDetector itd;
	private static LTMemory transientDetectorScope;

	public TDInterceptor(ITransientDetector itd) {
		this.itd = itd;
		transientDetectorScope = new LTMemory(
				Constants.TRANSIENT_DETECTOR_SCOPE_SIZE,
				Constants.TRANSIENT_DETECTOR_SCOPE_SIZE);

		// System.out.println("transient-Scope: " + transientDetectorScope);
	}

	public void runDetectorInScope() {
		// System.out.println("interceptor...");
		//itd.runDetectorInScope();
		transientDetectorScope.enter(this);
	}
	
	public void run() {
	    //System.out.println("scoped memory entered.");
		itd.runDetectorInScope();
	}

}