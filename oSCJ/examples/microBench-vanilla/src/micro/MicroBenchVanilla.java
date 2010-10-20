package micro;

import bench.Benchmark;


public class MicroBenchVanilla  {
	
	
	public MicroBenchVanilla() {
	}
	

    public boolean stop = false;

	
    public void handleEvent() {
      
    	long start =System.nanoTime();
        //for (int i=0 ; i < Constants.MAX ; i++)
     	   generate();
        long end =  System.nanoTime(); 
        Benchmark.set(start, end);
        
    }
    
    //protected byte[] callsigns;
    protected float t;
    
    private void generate() {
    	
    	byte[] callsigns = new byte[Constants.NUMBER_OF_PLANES*6];
    	
    	RawFrame result=new RawFrame();
        for (byte k=0;k<Constants.NUMBER_OF_PLANES;k++) {
            callsigns[6*k]=112;
            callsigns[6*k+1]=108;
            callsigns[6*k+2]=97;
            callsigns[6*k+3]=110;
            callsigns[6*k+4]=101;
            callsigns[6*k+5]=(byte)(49+k);
        }
        float positions[] = new float[60*3];
        
        for (int k=0;k<Constants.NUMBER_OF_PLANES/2;k++) {
            positions[3*k]=(float)(100*Math.cos(t)+500+50*k);
            positions[3*k+1]=100.0f;
            positions[3*k+2]=5.0f;
            positions[Constants.NUMBER_OF_PLANES/2*3+3*k]=(float)(100*Math.sin(t)+500+50*k);
            positions[Constants.NUMBER_OF_PLANES/2*3+3*k+1]=100.0f;
            positions[Constants.NUMBER_OF_PLANES/2*3+3*k+2]=5.0f;
        }
        // increase the time
        t=t+0.25f;
        //result.copy(null,callsigns,positions);
    }

	public void start() {
		// TODO Auto-generated method stub
		
	}
}
