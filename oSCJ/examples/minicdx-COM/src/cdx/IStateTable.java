package cdx;

import statetable.Vector3d;

public interface IStateTable {
	
	public Vector3d get(final CallSign callsign);
	public void put(final CallSign callsign, final float x, final float y, final float z);
	
}
