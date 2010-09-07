/**
 *  This file is part of miniCDx benchmark of oSCJ.
 *
 *   miniCDx is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   miniCDx is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with miniCDx.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *   Copyright 2009, 2010 
 *   @authors  Daniel Tang, Ales Plsek
 *
 *   See: http://sss.cs.purdue.edu/projects/oscj/
 */
package cdx;

import javax.safetycritical.annotate.SCJAllowed;

/**
 * Cyclic buffer of frames. Used for frames generated by simulator, but not yet processed by detector. The memory is
 * allocated at construction time and always re-used (frame data is copied in the same locations). The frames, as well
 * as the buffer, live in immortal memory. The constructor runs in immortal memory. Note that the buffer is
 * (intentionally) not synchronized. In a weird combination of priorities that is not intended to be used, buffer frames
 * could be overwritten.
 */
@javax.safetycritical.annotate.Scope("immortal")
@SCJAllowed(members=true)
public class FrameBuffer {

    // empty buffer ... first == last
    // full buffer .... last + 1 == first
    // - so there is still one empty slot, but we don't want to use it,
    // because we would not then recognize empty from full buffer
    //
    // last .. where the next frame will be stored
    // first .. where the next frame will be read

    public int       first, last;

    protected float  t;
    protected int[]  lengths;
    protected byte[] callsigns;
    private RawFrame buf;

    public FrameBuffer() {
        t = 0.0f;
        lengths = new int[Constants.NUMBER_OF_PLANES];
        callsigns = new byte[Constants.NUMBER_OF_PLANES * 6];
        buf = new RawFrame();
        for (int k = 0; k < lengths.length; k++)
            lengths[k] = 6;
    }

    public void putFrameInternal(final float[] positions_, final int[] lengths_, final byte[] callsigns_) {}

    static int frameno = 0;

    public void putFrame(final float[] positions_, final int[] lengths_, final byte[] callsigns_) {}

    /*@javax.safetycritical.annotate.RunsIn("cdx.CollisionDetectorHandler")*/
    public RawFrame getFrame() {
        for (byte k = 0; k < Constants.NUMBER_OF_PLANES; k++) {
            callsigns[6 * k] = 112;
            callsigns[6 * k + 1] = 108;
            callsigns[6 * k + 2] = 97;
            callsigns[6 * k + 3] = 110;
            callsigns[6 * k + 4] = 101;
            callsigns[6 * k + 5] = (byte) (49 + k);
        }
        float positions[] = new float[60 * 3];

        for (int k = 0; k < Constants.NUMBER_OF_PLANES / 2; k++) {
            positions[3 * k] = (float) (100 * Math.cos(t) + 500 + 50 * k);
            positions[3 * k + 1] = 100.0f;
            positions[3 * k + 2] = 5.0f;
            positions[Constants.NUMBER_OF_PLANES / 2 * 3 + 3 * k] = (float) (100 * Math.sin(t) + 500 + 50 * k);
            positions[Constants.NUMBER_OF_PLANES / 2 * 3 + 3 * k + 1] = 100.0f;
            positions[Constants.NUMBER_OF_PLANES / 2 * 3 + 3 * k + 2] = 5.0f;
        }
        // increase the time
        t = t + 0.25f;
        buf.copy(lengths, callsigns, positions);
        return buf;
    }
}





/*
public RawFrame getFrame() {
    
    if (iterations % 100 == 0) {
        planes += delta;
        counter = 0;
    }
    
    if (counter == 20) {
        planes = Constants.NUMBER_OF_PLANES;
        counter = 0;
    }
         
    
    for (byte k = 0; k < planes; k++) {
        callsigns[6 * k] = 112;
        callsigns[6 * k + 1] = 108;
        callsigns[6 * k + 2] = 97;
        callsigns[6 * k + 3] = 110;
        callsigns[6 * k + 4] = 101;
        callsigns[6 * k + 5] = (byte) (49 + k);
    }
    float positions[] = new float[60 * 3];

    for (int k = 0; k < planes / 2; k++) {
        positions[3 * k] = (float) (100 * Math.cos(t) + 500 + 50 * k);
        positions[3 * k + 1] = 100.0f;
        positions[3 * k + 2] = 5.0f;
        positions[planes / 2 * 3 + 3 * k] = (float) (100 * Math.sin(t) + 500 + 50 * k);
        positions[planes / 2 * 3 + 3 * k + 1] = 100.0f;
        positions[planes / 2 * 3 + 3 * k + 2] = 5.0f;
    }
    // increase the time
    t = t + 0.25f;
    buf.copy(lengths, callsigns, positions);
    
    iterations++;
    counter++;
    
    return buf;
}*/