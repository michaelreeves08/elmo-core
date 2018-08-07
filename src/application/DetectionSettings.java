package application;

import org.opencv.core.Size;

public class DetectionSettings {
	double scaleFactor;
	int minNeighbors;
	int flags;
	Size minSize;
	Size maxSize;
	
	public DetectionSettings(double scaleFactor, int minNeighbors, int flags, Size minSize, Size maxSize ) {
		this.scaleFactor = scaleFactor;
		this.minNeighbors = minNeighbors;
		this.flags = flags;
		this.minSize = minSize;
		this.maxSize = maxSize;
	}

	public double getScaleFactor() {
		return scaleFactor;
	}

	public int getMinNeighbors() {
		return minNeighbors;
	}

	public int getFlags() {
		return flags;
	}

	public Size getMinSize() {
		return minSize;
	}

	public Size getMaxSize() {
		return maxSize;
	}
	
	
}
