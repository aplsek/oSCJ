package cdx;

public class TDInterceptor implements ITransientDetector {

    ITransientDetector itd;

    public TDInterceptor(ITransientDetector itd) {
	this.itd = itd;
    }

    public void runDetectorInScope() {
	itd.runDetectorInScope();
    }

}