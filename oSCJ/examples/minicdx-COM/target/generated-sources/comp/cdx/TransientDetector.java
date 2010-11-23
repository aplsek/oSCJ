package comp.cdx;

import cdx.Aircraft;
import java.util.ArrayList;
import org.objectweb.fractal.fraclet.annotations.Attribute;
import org.objectweb.fractal.api.control.BindingController;
import cdx.CallSign;
import java.util.Collection;
import cdx.Collision;
import org.objectweb.fractal.fraclet.annotations.Component;
import cdx.Constants;
import java.util.HashMap;
import java.util.HashSet;
import cdx.IStateTable;
import cdx.ITransientDetector;
import org.objectweb.fractal.fraclet.annotations.Interface;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import cdx.Motion;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import cdx.RawFrame;
import cdx.Reducer;
import org.objectweb.fractal.fraclet.annotations.Requires;
import collision.Vector3d;

@Component(name = "TransientDetector", provides = @Interface(name = "iTransientDetector", signature = ITransientDetector.class))
public class TransientDetector implements ITransientDetector , TDAttributes , BindingController {
    @Attribute(name = "voxelSize")
    private float voxelSize;
    
    public float getVoxelSize() {
        return voxelSize;
    }
    
    public void setVoxelSize(float voxelSize) {
        this.voxelSize = voxelSize;
    }
    
    public String[] listFc() {
        List<java.lang.String>  __itfs__ = new ArrayList<java.lang.String> ();
        listFc(__itfs__);
        return __itfs__.toArray(new String[__itfs__.size()]);
    }
    
    @Requires(name = "iStateTable")
    private IStateTable iState;
    
    @Requires(name = "iTdToIe")
    private ITransDetectToImmEntry iTdToIe;
    
    private RawFrame currentFrame;
    
    public void runDetectorInScope() {
        final RawFrame f = iTdToIe.getFrame();
        if (f == null) {
            iTdToIe.incrementFrameNotReadyCount();
            System.out.println("Frame not ready");
            return ;
        } 
        setFrame(f);
        run();
        iTdToIe.incrementFramesProcessed();
    }
    
    /** 
     * Completes the list of bound interfaces.
     * 
     * @param set incomplete list of interface identifiers.
     */
    protected void listFc(List<java.lang.String>  set) {
        if ((this.iTdToIe) != null)
            set.add("iTdToIe");
        
        if ((this.iState) != null)
            set.add("iStateTable");
        
    }
    
    public Object lookupFc(String id) throws NoSuchInterfaceException {
        if (id.equals("iTdToIe"))
            return this.iTdToIe;
        
        if (id.equals("iStateTable"))
            return this.iState;
        
        throw new NoSuchInterfaceException((("Client interface \'" + id) + "\' is undefined."));
    }
    
    public void bindFc(String id, Object ref) throws NoSuchInterfaceException {
        if (id.equals("iTdToIe")) {
            this.iTdToIe = ((ITransDetectToImmEntry)(ref));
            return ;
        } 
        if (id.equals("iStateTable")) {
            this.iState = ((IStateTable)(ref));
            return ;
        } 
        throw new NoSuchInterfaceException((("Client interface \'" + id) + "\' is undefined."));
    }
    
    public void run() {
        if ((Constants.SYNCHRONOUS_DETECTOR) || (Constants.DEBUG_DETECTOR)) {
            dumpFrame("CD-PROCESSING-FRAME (indexed as received): ");
        } 
        final Reducer reducer = new Reducer(voxelSize);
        int numberOfCollisions = lookForCollisions(reducer ,createMotions());
        iTdToIe.setNumberOfCollisions(numberOfCollisions);
        if ((Constants.SYNCHRONOUS_DETECTOR) || (Constants.DEBUG_DETECTOR)) {
            System.out.println((("CD detected  " + numberOfCollisions) + " collisions."));
            int colIndex = 0;
            System.out.println("");
        } 
    }
    
    public void unbindFc(String id) throws NoSuchInterfaceException {
        if (id.equals("iTdToIe")) {
            this.iTdToIe = null;
            return ;
        } 
        if (id.equals("iStateTable")) {
            this.iState = null;
            return ;
        } 
        throw new NoSuchInterfaceException((("Client interface \'" + id) + "\' is undefined."));
    }
    
    @SuppressWarnings(value = "unchecked")
    public int lookForCollisions(final Reducer reducer, final List motions) {
        cdx.Benchmarker.set(2);
        final List check = reduceCollisionSet(reducer ,motions);
        int suspectedSize = check.size();
        iTdToIe.setSuspectedSize(suspectedSize);
        if (((Constants.SYNCHRONOUS_DETECTOR) || (Constants.DEBUG_DETECTOR)) && (!(check.isEmpty()))) {
            System.out.println((("CD found " + suspectedSize) + " potential collisions"));
            int i = 0;
            for (final Iterator iter = check.iterator() ; iter.hasNext() ; ) {
                final List col = ((List)(iter.next()));
                for (final Iterator aiter = col.iterator() ; aiter.hasNext() ; ) {
                    final Motion m = ((Motion)(aiter.next()));
                    System.out.println(((((("CD: potential collision " + i) + " (of ") + (col.size())) + " aircraft) includes motion ") + m));
                }
                i++;
            }
        } 
        int c = 0;
        final List ret = new LinkedList();
        for (final Iterator iter = check.iterator() ; iter.hasNext() ; )
            c += determineCollisions(((List)(iter.next())) ,ret);
        cdx.Benchmarker.done(2);
        return c;
    }
    
    /** 
     * Takes a List of Motions and returns an List of Lists of Motions, where
     * the inner lists implement RandomAccess. Each Vector of Motions that is
     * returned represents a set of Motions that might have collisions.
     */
    @SuppressWarnings(value = "unchecked")
    public List reduceCollisionSet(final Reducer it, final List motions) {
        cdx.Benchmarker.set(3);
        final HashMap voxel_map = new HashMap();
        final HashMap graph_colors = new HashMap();
        for (final Iterator iter = motions.iterator() ; iter.hasNext() ; )
            it.performVoxelHashing(((Motion)(iter.next())) ,voxel_map ,graph_colors);
        final List ret = new LinkedList();
        for (final Iterator iter = voxel_map.values().iterator() ; iter.hasNext() ; ) {
            final List cur_set = ((List)(iter.next()));
            if ((cur_set.size()) > 1)
                ret.add(cur_set);
            
        }
        cdx.Benchmarker.done(3);
        return ret;
    }
    
    @SuppressWarnings(value = "unchecked")
    public boolean checkForDuplicates(final List collisions, Motion one, Motion two) {
        byte c1 = one.getAircraft().getCallsign()[5];
        byte c2 = two.getAircraft().getCallsign()[5];
        for (final Iterator iter = collisions.iterator() ; iter.hasNext() ; ) {
            Collision c = ((Collision)(iter.next()));
            if (((c.first().getCallsign()[5]) == c1) && ((c.second().getCallsign()[5]) == c2)) {
                return false;
            } 
        }
        return true;
    }
    
    @SuppressWarnings(value = "unchecked")
    public int determineCollisions(final List motions, List ret) {
        cdx.Benchmarker.set(5);
        int _ret = 0;
        Motion[] _motions = ((Motion[])(motions.toArray(new Motion[motions.size()])));
        for (int i = 0 ; i < ((_motions.length) - 1) ; i++) {
            final Motion one = _motions[i];
            for (int j = i + 1 ; j < (_motions.length) ; j++) {
                final Motion two = _motions[j];
                final Vector3d vec = one.findIntersection(two);
                if (vec != null) {
                    ret.add(new Collision(one.getAircraft() , two.getAircraft() , vec));
                    _ret++;
                } 
            }
        }
        cdx.Benchmarker.done(5);
        return _ret;
    }
    
    public void dumpFrame(String debugPrefix) {
        String prefix = (debugPrefix + (frameno)) + " ";
        int offset = 0;
        for (int i = 0 ; i < (currentFrame.planeCnt) ; i++) {
            int cslen = currentFrame.lengths[i];
            System.out.println(((((((((prefix + (new String(currentFrame.callsigns , offset , cslen))) + " ") + (currentFrame.positions[(3 * i)])) + " ") + (currentFrame.positions[((3 * i) + 1)])) + " ") + (currentFrame.positions[((3 * i) + 2)])) + " "));
            offset += cslen;
        }
    }
    
    int frameno = -1;
    
    public void setFrame(final RawFrame f) {
        if (((Constants.DEBUG_DETECTOR) || (Constants.DUMP_RECEIVED_FRAMES)) || (Constants.SYNCHRONOUS_DETECTOR)) {
            (frameno)++;
        } 
        currentFrame = f;
        if (Constants.DUMP_RECEIVED_FRAMES) {
            dumpFrame("CD-R-FRAME: ");
        } 
    }
    
    /** 
     * This method computes the motions and current positions of the aircraft
     * Afterwards, it stores the positions of the aircrafts into the StateTable
     * in the persistentScope
     * 
     * @return
     */
    @SuppressWarnings(value = "unchecked")
    public List createMotions() {
        cdx.Benchmarker.set(6);
        final List ret = new LinkedList();
        final HashSet poked = new HashSet();
        Aircraft craft;
        Vector3d new_pos;
        for (int i = 0, pos = 0 ; i < (currentFrame.planeCnt) ; i++) {
            final float x = currentFrame.positions[(3 * i)];
            final float y = currentFrame.positions[((3 * i) + 1)];
            final float z = currentFrame.positions[((3 * i) + 2)];
            final byte[] cs = new byte[currentFrame.lengths[i]];
            for (int j = 0 ; j < (cs.length) ; j++)
                cs[j] = currentFrame.callsigns[(pos + j)];
            pos += cs.length;
            craft = new Aircraft(cs);
            new_pos = new Vector3d(x , y , z);
            poked.add(craft);
            final statetable.Vector3d old_pos = iState.get(new CallSign(craft.getCallsign()));
            if (old_pos == null) {
                iState.put(mkCallsignInPersistentScope(craft.getCallsign()) ,new_pos.x ,new_pos.y ,new_pos.z);
                final Motion m = new Motion(craft , new_pos);
                if ((Constants.DEBUG_DETECTOR) || (Constants.SYNCHRONOUS_DETECTOR)) {
                    System.out.println(("createMotions: old position is null, adding motion: " + m));
                } 
                ret.add(m);
            } else {
                final Vector3d save_old_position = new Vector3d(old_pos.x , old_pos.y , old_pos.z);
                old_pos.set(new_pos.x ,new_pos.y ,new_pos.z);
                final Motion m = new Motion(craft , save_old_position , new_pos);
                if ((Constants.DEBUG_DETECTOR) || (Constants.SYNCHRONOUS_DETECTOR)) {
                    System.out.println(("createMotions: adding motion: " + m));
                } 
                ret.add(m);
            }
        }
        cdx.Benchmarker.done(6);
        return ret;
    }
    
    /** 
     * This Runnable enters the StateTable in order to allocate the callsign in
     * the PersistentScope
     */
    CallSign mkCallsignInPersistentScope(final byte[] cs) {
        return new CallSign(cs);
    }
    
}

