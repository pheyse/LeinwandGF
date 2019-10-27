package de.bright_side.lgf.pc.model;

import java.util.List;

public class LPcAnimationResourceProperties {
	private List<String> frameImagePaths;
	private double frameDurationInSeconds;
	private double rotation;
	
	public double getFrameDurationInSeconds() {
		return frameDurationInSeconds;
	}
	public void setFrameDurationInSeconds(double frameDuration) {
		this.frameDurationInSeconds = frameDuration;
	}
	public double getRotation() {
		return rotation;
	}
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
	public List<String> getFrameImagePaths() {
		return frameImagePaths;
	}
	public void setFrameImagePaths(List<String> frameImagePaths) {
		this.frameImagePaths = frameImagePaths;
	}
	
	
	
}
