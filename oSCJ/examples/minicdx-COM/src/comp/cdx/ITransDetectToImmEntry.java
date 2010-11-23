package comp.cdx;

import cdx.RawFrame;

public interface ITransDetectToImmEntry {
	
	public RawFrame getFrame();
	public void incrementFrameNotReadyCount();
	public void incrementFramesProcessed();
	
	public void setNumberOfCollisions(int numberOfCollisions);
	public void setSuspectedSize(int suspectedSize);

}
