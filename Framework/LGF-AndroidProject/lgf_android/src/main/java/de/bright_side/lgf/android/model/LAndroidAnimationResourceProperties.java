package de.bright_side.lgf.android.model;

import java.util.List;

public class LAndroidAnimationResourceProperties {
	private List<Integer> frameImages;
	private double frameDurationInSeconds;
	private double rotation;
	
	public double getFrameDurationInSeconds() {
		return frameDurationInSeconds;
	}
	public void setFrameDurationInSeconds(double frameDurationInSeconds) {
		this.frameDurationInSeconds = frameDurationInSeconds;
	}
	public double getRotation() {
		return rotation;
	}
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public List<Integer> getFrameImages() {
		return frameImages;
	}

	public void setFrameImages(List<Integer> frameImages) {
		this.frameImages = frameImages;
	}
}
