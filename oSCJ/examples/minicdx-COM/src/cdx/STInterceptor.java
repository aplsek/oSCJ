package cdx;

import javax.realtime.LTMemory;
import javax.realtime.MemoryArea;
import javax.realtime.RealtimeThread;

import statetable.Vector3d;

public class STInterceptor implements IStateTable {

	IStateTable ist;

	public STInterceptor(IStateTable ist) {
		this.ist = ist;
	}

	public Vector3d get(final CallSign callsign) {
		//System.out.println("ST-sIns");
		
		//System.out.println("area of this:" + RealtimeThread.getCurrentMemoryArea());
		//System.out.println("area of ist+ " + MemoryArea.getMemoryArea(ist) );
		
		//
		
		Vector3d vec = ist.get(callsign);
		//System.out.println("Vector OK.");
		//return ist.get(callsign);
		return vec;
	}

	public void put(final CallSign callsign, final float x, final float y,
			final float z) {
		/*
		
		//System.out.println("Insert into stateTable...");
		
		R r = new R();
		r.cs = callsign.getVal();
		
		
		
		//System.out.println("copy call sign ok...");
		
		MemoryArea.getMemoryArea(enterMemory).executeInArea(r);
		//System.out.println("execInArea.... OK");
		//ist.put(callsign, x, y, z);
		ist.put(r.out, x, y, z);
		
		//System.out.println("Insert into stateTable... OK");
		*/
		
		Run r = new Run();
		r.x = x;
		r.y = y;
		r.z = z;
		r.in = callsign;
		MemoryArea.getMemoryArea(ist).executeInArea(r);
		
	}
	
	class Run implements Runnable {
		CallSign in;
		float x;
		float y;
		float z;
		
		public void run() {
			final byte[] b = new byte[in.getVal().length];
			for (int i = 0; i < b.length; i++)
				b[i] = in.getVal()[i];
			
			ist.put(new CallSign(b), x, y, z);
		}
	}
	
	
//	static class R implements Runnable {
//		CallSign in;
//		CallSign out;
//		
//		byte[] cs;
//		public void run() {
//			final byte[] b = new byte[cs.length];
//			for (int i = 0; i < b.length; i++)
//				b[i] = cs[i];
//			//System.out.println("Copying the callsign...");
//			
//			out = new CallSign(b);
//		}
//	}
//	private final R r = new R();

	
	//LTMemory enterMemory ;
	//public void setMem(LTMemory persistentMemory) {
	//	this.enterMemory = persistentMemory;
	//}
}