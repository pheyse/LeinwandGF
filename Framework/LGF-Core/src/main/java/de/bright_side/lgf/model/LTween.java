package de.bright_side.lgf.model;

public class LTween {
	private LVector posChange;
	private LVector sizeChange;
	private double rotationChange;
    private double textSizeChange;
    private double durationInSeconds;
    private LAction completionAction;
    
	public LVector getPosChange() {
		return posChange;
	}
	public void setPosChange(LVector posChange) {
		this.posChange = posChange;
	}
	public double getRotationChange() {
		return rotationChange;
	}
	public void setRotationChange(double rotationChange) {
		this.rotationChange = rotationChange;
	}
	public double getTextSizeChange() {
		return textSizeChange;
	}
	public void setTextSizeChange(double textSizeChange) {
		this.textSizeChange = textSizeChange;
	}
	public double getDurationInSeconds() {
		return durationInSeconds;
	}
	public void setDurationInSeconds(double durationInSeconds) {
		this.durationInSeconds = durationInSeconds;
	}
	public LAction getCompletionAction() {
		return completionAction;
	}
	public void setCompletionAction(LAction completionAction) {
		this.completionAction = completionAction;
	}
	public LVector getSizeChange() {
		return sizeChange;
	}
	public void setSizeChange(LVector sizeChange) {
		this.sizeChange = sizeChange;
	}
	@Override
	public String toString() {
		return "LTween [posChange=" + posChange + ", sizeChange=" + sizeChange + ", rotationChange=" + rotationChange + ", textSizeChange=" + textSizeChange
				+ ", durationInSeconds=" + durationInSeconds + ", completionAction=" + completionAction + "]";
	}

    
}
