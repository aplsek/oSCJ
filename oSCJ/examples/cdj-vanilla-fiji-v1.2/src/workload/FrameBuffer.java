package workload;

import immortal.Constants;
import immortal.RawFrame;

/**
 * Cyclic buffer of frames. Used for frames generated by simulator, but not yet processed by detector. The memory is
 * allocated at construction time and always re-used (frame data is copied in the same locations). The frames, as well
 * as the buffer, live in immortal memory. The constructor runs in immortal memory. Note that the buffer is
 * (intentionally) not synchronized. In a weird combination of priorities that is not intended to be used, buffer frames
 * could be overwritten.
 */
public abstract class FrameBuffer {

    // empty buffer ... first == last
    // full buffer .... last + 1 == first
    // - so there is still one empty slot, but we don't want to use it,
    // because we would not then recognize empty from full buffer
    //
    // last .. where the next frame will be stored
    // first .. where the next frame will be read

    public int       first, last;

    float  t;
    int[]  lengths;
    byte[] callsigns;
    RawFrame buf;
    
    static int frameno = 0;
    
    
    public FrameBuffer() {
        t = 0.0f;
        lengths = new int[Constants.NUMBER_OF_PLANES];
        callsigns = new byte[Constants.NUMBER_OF_PLANES * 6];
        buf = new RawFrame();
        for (int k = 0; k < lengths.length; k++)
            lengths[k] = 6;
    }
    
    public abstract void putFrameInternal(final float[] positions_, final int[] lengths_, final byte[] callsigns_);


    public abstract void putFrame(final float[] positions_, final int[] lengths_, final byte[] callsigns_);

    /*@javax.safetycritical.annotate.RunsIn("cdx.CollisionDetectorHandler")*/
    public abstract RawFrame getFrame();
    
}
