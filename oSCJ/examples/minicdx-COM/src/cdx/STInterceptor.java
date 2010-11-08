package cdx;

import statetable.Vector3d;

public class STInterceptor implements IStateTable {

    IStateTable ist;

    public STInterceptor(IStateTable ist) {
	this.ist = ist;
    }

    public Vector3d get(final CallSign callsign) {
	return ist.get(callsign);
    }

    public void put(final CallSign callsign, final float x, final float y, final float z) {
	ist.put(callsign,x,y,z);
    }
}