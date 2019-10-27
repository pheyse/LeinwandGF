package de.bright_side.lgf.model;

public class LAnimationFrame {
	private LImage image;
	private double durationInSeconds;

	public LAnimationFrame(LImage image, double durationInSeconds) {
		this.image = image;
		this.durationInSeconds = durationInSeconds;
	}

	public LImage getImage() {
		return image;
	}

	public void setImage(LImage image) {
		this.image = image;
	}

	public double getDurationInSeconds() {
		return durationInSeconds;
	}

	public void setDurationInSeconds(double durationInSeconds) {
		this.durationInSeconds = durationInSeconds;
	}

	@Override
	public String toString() {
		return "LAnimationFrame [image=" + image + ", durationInSeconds=" + durationInSeconds + "]";
	}
	
	
	
}
