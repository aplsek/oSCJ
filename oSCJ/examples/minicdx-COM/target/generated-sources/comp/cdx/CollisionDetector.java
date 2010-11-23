package comp.cdx;

import java.util.ArrayList;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.fraclet.annotations.Component;
import cdx.ITransientDetector;
import org.objectweb.fractal.fraclet.annotations.Interface;
import java.util.List;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.fraclet.annotations.Requires;

@Component(name = "CollisionDetector", provides = @Interface(name = "r", signature = Runnable.class))
public class CollisionDetector implements Runnable , BindingController {
    public boolean stop = false;
    
    @Requires(name = "iTransientDetector")
    private ITransientDetector iTransientDetector;
    
    @Requires(name = "iCdToIe")
    private ICollDetectToImmEntry iCdToIe;
    
    public void run() {
        long now = java.lang.System.nanoTime();
        iCdToIe.beforeRun(now ,false);
        long timeBefore = cdx.NanoClock.now();
        iTransientDetector.runDetectorInScope();
        long timeAfter = cdx.NanoClock.now();
        iCdToIe.afterRun(timeBefore ,timeAfter);
    }
    
    public String[] listFc() {
        List<java.lang.String>  __itfs__ = new ArrayList<java.lang.String> ();
        listFc(__itfs__);
        return __itfs__.toArray(new String[__itfs__.size()]);
    }
    
    /** 
     * Completes the list of bound interfaces.
     * 
     * @param set incomplete list of interface identifiers.
     */
    protected void listFc(List<java.lang.String>  set) {
        if ((this.iCdToIe) != null)
            set.add("iCdToIe");
        
        if ((this.iTransientDetector) != null)
            set.add("iTransientDetector");
        
    }
    
    public Object lookupFc(String id) throws NoSuchInterfaceException {
        if (id.equals("iCdToIe"))
            return this.iCdToIe;
        
        if (id.equals("iTransientDetector"))
            return this.iTransientDetector;
        
        throw new NoSuchInterfaceException((("Client interface \'" + id) + "\' is undefined."));
    }
    
    public void bindFc(String id, Object ref) throws NoSuchInterfaceException {
        if (id.equals("iCdToIe")) {
            this.iCdToIe = ((ICollDetectToImmEntry)(ref));
            return ;
        } 
        if (id.equals("iTransientDetector")) {
            this.iTransientDetector = ((ITransientDetector)(ref));
            return ;
        } 
        throw new NoSuchInterfaceException((("Client interface \'" + id) + "\' is undefined."));
    }
    
    public void unbindFc(String id) throws NoSuchInterfaceException {
        if (id.equals("iCdToIe")) {
            this.iCdToIe = null;
            return ;
        } 
        if (id.equals("iTransientDetector")) {
            this.iTransientDetector = null;
            return ;
        } 
        throw new NoSuchInterfaceException((("Client interface \'" + id) + "\' is undefined."));
    }
    
}

