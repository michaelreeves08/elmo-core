package Models;

import org.opencv.core.Point;

public class FaceDimensions {
	Point leftEye;
	Point rightEye;
	Point chin;
	int width;
	int height;
	int eyeDistance;
	Attributes attributes;
	
	public FaceDimensions(Point leftEye, Point rightEye, Point chin, int width, int height, int eyeDistance, Attributes attributes) {
		this.leftEye = leftEye;
		this.rightEye = rightEye;
		this.chin = chin;
		this.width = width;
		this.height = height;
		this.eyeDistance = eyeDistance;
		this.attributes = attributes;
	}
	
	public Point getLeftEye() {

		return leftEye;
	}
	
	public Point getRightEye() {
		return rightEye;
	}

	public Point getChin() {
		return chin;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getEyeDistance() {
		return eyeDistance;
	}

	public Attributes getAttributes() {
		return attributes;
	}
}
