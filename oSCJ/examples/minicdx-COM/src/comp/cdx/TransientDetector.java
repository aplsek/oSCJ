package comp.cdx;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.objectweb.fractal.fraclet.annotations.Attribute;
import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;
import org.objectweb.fractal.fraclet.annotations.Requires;

import cdx.Aircraft;
import cdx.Benchmarker;
import cdx.CallSign;
import cdx.Collision;
import cdx.Constants;
import cdx.IStateTable;
import cdx.ITransientDetector;
import cdx.Motion;
import cdx.RawFrame;
import cdx.Reducer;
import collision.Vector3d;

@Component(name = "TransientDetector", provides = @Interface(name = "iTransientDetector", signature = cdx.ITransientDetector.class))
public class TransientDetector implements ITransientDetector, TDAttributes {

	// Component attribute
	@Attribute(name="voxelSize")
	private float voxelSize;
	
	public float getVoxelSize() {
		return voxelSize;
	}
	public void setVoxelSize(float voxelSize) {
		this.voxelSize = voxelSize;
	}
	
	// Required interfaces
	@Requires(name="iStateTable")
	private IStateTable iState; 
	@Requires(name="iTdToIe")
	private ITransDetectToImmEntry iTdToIe;
	
	private RawFrame currentFrame;

	public void runDetectorInScope() {

		// The direct accesses to ImmortalEntry fields are replaced by a method call via
		// an interface
		//final RawFrame f = ImmortalEntry.frameBuffer.getFrame();
		final RawFrame f = iTdToIe.getFrame();
		
		if (f == null) {
			//ImmortalEntry.frameNotReadyCount++;
			iTdToIe.incrementFrameNotReadyCount();
			System.out.println("Frame not ready");
			return;
		}
		setFrame(f);   

		run();

		//ImmortalEntry.framesProcessed++;
		iTdToIe.incrementFramesProcessed();
	}

	public void run() {
		if (Constants.SYNCHRONOUS_DETECTOR || Constants.DEBUG_DETECTOR) {
			dumpFrame("CD-PROCESSING-FRAME (indexed as received): ");
		}

		final Reducer reducer = new Reducer(voxelSize);

		int numberOfCollisions = lookForCollisions(reducer, createMotions());

		//		if (ImmortalEntry.recordedRuns < ImmortalEntry.maxDetectorRuns) {
		//			ImmortalEntry.detectedCollisions[ImmortalEntry.recordedRuns] = numberOfCollisions;
		//		}
		iTdToIe.setNumberOfCollisions(numberOfCollisions);

		if (Constants.SYNCHRONOUS_DETECTOR || Constants.DEBUG_DETECTOR) {
			System.out.println("CD detected  " + numberOfCollisions
					+ " collisions.");
			int colIndex = 0;
			System.out.println("");
		}
	}

	@SuppressWarnings("unchecked")
	public int lookForCollisions(final Reducer reducer, final List motions) {
		Benchmarker.set(2);
		final List check = reduceCollisionSet(reducer, motions);
		int suspectedSize = check.size();
		
		//		if (ImmortalEntry.recordedRuns < ImmortalEntry.maxDetectorRuns) {
		//			ImmortalEntry.suspectedCollisions[ImmortalEntry.recordedRuns] = suspectedSize;
		//		}
		iTdToIe.setSuspectedSize(suspectedSize);
		
		if ((Constants.SYNCHRONOUS_DETECTOR || Constants.DEBUG_DETECTOR) && !check.isEmpty()) {
			System.out.println("CD found "+suspectedSize+" potential collisions");
			int i=0;
			for(final Iterator iter = check.iterator(); iter.hasNext();) {
				final List col = (List)iter.next();
				for(final Iterator aiter = col.iterator(); aiter.hasNext();) {
					final Motion m = (Motion)aiter.next();
					System.out.println("CD: potential collision "+i+" (of "+col.size()+" aircraft) includes motion "+m);
				}
				i++;            
			}
		}

		int c = 0;
		final List ret = new LinkedList();
		for (final Iterator iter = check.iterator(); iter.hasNext();)
			c += determineCollisions((List) iter.next(), ret);
		Benchmarker.done(2);
		return c; 
	}

	/**
	 * Takes a List of Motions and returns an List of Lists of Motions, where
	 * the inner lists implement RandomAccess. Each Vector of Motions that is
	 * returned represents a set of Motions that might have collisions.
	 */
	@SuppressWarnings("unchecked")
	public List reduceCollisionSet(final Reducer it, final List motions) {
		Benchmarker.set(3);
		final HashMap voxel_map = new HashMap();
		final HashMap graph_colors = new HashMap();

		for (final Iterator iter = motions.iterator(); iter.hasNext();)
			it.performVoxelHashing((Motion) iter.next(), voxel_map,
					graph_colors);

		final List ret = new LinkedList();
		for (final Iterator iter = voxel_map.values().iterator(); iter
		.hasNext();) {
			final List cur_set = (List) iter.next();
			if (cur_set.size() > 1)
				ret.add(cur_set);
		}
		Benchmarker.done(3);
		return ret;
	}

	@SuppressWarnings("unchecked")
	public boolean checkForDuplicates(final List collisions, Motion one,
			Motion two) {
		// (Peta) I have also changed the comparison employed in this method as
		// it is another major source of overhead
		// Java was checking all the callsign elements, while C just checked the
		// callsign array addresses
		byte c1 = one.getAircraft().getCallsign()[5];
		byte c2 = two.getAircraft().getCallsign()[5];
		for (final Iterator iter = collisions.iterator(); iter.hasNext();) {
			Collision c = (Collision) iter.next();
			if ((c.first().getCallsign()[5] == c1)
					&& (c.second().getCallsign()[5] == c2)) {
				// Benchmarker.done(4);
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public int determineCollisions(final List motions, List ret) {
		// (Peta) changed to iterators so that it's not killing the algorithm
		Benchmarker.set(5);
		int _ret = 0;
		Motion[] _motions = (Motion[]) motions.toArray(new Motion[motions
		                                                          .size()]);
		// Motion[] _motions= (Motion)motions.toArray();
		for (int i = 0; i < _motions.length - 1; i++) {
			final Motion one = _motions[i]; // m2==two, m=one
			for (int j = i + 1; j < _motions.length; j++) {
				final Motion two = _motions[j];
				// if (checkForDuplicates(ret, one, two)) { // This is removed
				// because it is very very slow...
				final Vector3d vec = one.findIntersection(two);
				if (vec != null) {
					ret.add(new Collision(one.getAircraft(), two.getAircraft(),
							vec));
					_ret++;
				}
			}
		}
		Benchmarker.done(5);
		return _ret;
	}

	public void dumpFrame(String debugPrefix) {

		String prefix = debugPrefix + frameno + " ";
		int offset = 0;
		for (int i = 0; i < currentFrame.planeCnt; i++) {

			int cslen = currentFrame.lengths[i];
			System.out.println(prefix
					+ new String(currentFrame.callsigns, offset, cslen) + " "
					+ currentFrame.positions[3 * i] + " "
					+ currentFrame.positions[3 * i + 1] + " "
					+ currentFrame.positions[3 * i + 2] + " ");
			offset += cslen;
		}
	}

	int frameno = -1; // just for debug

	public void setFrame(final RawFrame f) {
		if (Constants.DEBUG_DETECTOR || Constants.DUMP_RECEIVED_FRAMES
				|| Constants.SYNCHRONOUS_DETECTOR) {
			frameno++;
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
	@SuppressWarnings("unchecked")
	public List createMotions() {
		Benchmarker.set(6);
		final List ret = new LinkedList();
		final HashSet poked = new HashSet();

		Aircraft craft;
		Vector3d new_pos;

		for (int i = 0, pos = 0; i < currentFrame.planeCnt; i++) {

			final float x = currentFrame.positions[3 * i], y = currentFrame.positions[3 * i + 1], z = currentFrame.positions[3 * i + 2];
			final byte[] cs = new byte[currentFrame.lengths[i]];
			for (int j = 0; j < cs.length; j++)
				cs[j] = currentFrame.callsigns[pos + j];
			pos += cs.length;
			craft = new Aircraft(cs);
			new_pos = new Vector3d(x, y, z);

			poked.add(craft);
			final statetable.Vector3d old_pos = iState
			.get(new CallSign(craft.getCallsign()));

			if (old_pos == null) {
				//iState.put(mkCallsignInPersistentScope(craft.getCallsign()),
				//		new_pos.x, new_pos.y, new_pos.z);
			    iState.put(new CallSign(craft.getCallsign()),          
                    new_pos.x, new_pos.y, new_pos.z);

				final Motion m = new Motion(craft, new_pos);
				if (cdx.Constants.DEBUG_DETECTOR
						|| cdx.Constants.SYNCHRONOUS_DETECTOR) {
					System.out
					.println("createMotions: old position is null, adding motion: "
							+ m);
				}
				ret.add(m);
			} else {
				final Vector3d save_old_position = new Vector3d(old_pos.x,
						old_pos.y, old_pos.z);
				old_pos.set(new_pos.x, new_pos.y, new_pos.z);

				final Motion m = new Motion(craft, save_old_position, new_pos);
				if (cdx.Constants.DEBUG_DETECTOR
						|| cdx.Constants.SYNCHRONOUS_DETECTOR) {
					System.out.println("createMotions: adding motion: " + m);
				}
				ret.add(m);
			}
		}
		Benchmarker.done(6);
		return ret;
	}




	/**
	 * This Runnable enters the StateTable in order to allocate the callsign in
	 * the PersistentScope
	 */
//	static class R implements Runnable {
//		CallSign c;
//		byte[] cs;
//
//		public void run() {
//			c = new CallSign(cs);
//		}
//	}
//
//	private final R r = new R();
/*
	CallSign mkCallsignInPersistentScope(final byte[] cs) {
		/*
	    	try {
	            //r.cs = (byte[]) MemoryArea.newArrayInArea(r, byte.class, cs.length);


	        } catch (IllegalAccessException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        for (int i = 0; i < cs.length; i++)
	            r.cs[i] = cs[i];

	        //MemoryArea.getMemoryArea(iState).executeInArea(r);
	        return r.c;
		 *//*
		return new CallSign(cs);
	}
*/

}
