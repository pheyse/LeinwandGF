package de.bright_side.lgf.model;

public class LPointer {
	private LVector dragDistance;
	private LVector touchDownPos;
	private LVector pos;
	public LVector getDragDistance() {
		return dragDistance;
	}
	public void setDragDistance(LVector dragDistance) {
		this.dragDistance = dragDistance;
	}
	public LVector getTouchDownPos() {
		return touchDownPos;
	}
	public void setTouchDownPos(LVector touchDownPos) {
		this.touchDownPos = touchDownPos;
	}
	public LVector getPos() {
		return pos;
	}
	public void setPos(LVector pos) {
		this.pos = pos;
	}

}
