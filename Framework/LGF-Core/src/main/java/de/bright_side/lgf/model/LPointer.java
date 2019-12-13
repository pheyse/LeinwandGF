package de.bright_side.lgf.model;

/**
 * @author phhey
 *
 */
public class LPointer {
	private LVector dragDistance;
	private LVector touchDownPos;
	private LVector pos;
	private LVector movement;
	
	/**
	 * @return the drag distance since the pointer was down
	 */
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
	/**
	 * @return the movement of the pointer (while down) since the last update method call
	 */
	public LVector getMovement() {
		return movement;
	}
	public void setMovement(LVector movement) {
		this.movement = movement;
	}
	@Override
	public String toString() {
		return "LPointer [dragDistance=" + dragDistance + ", touchDownPos=" + touchDownPos + ", pos=" + pos + ", movement=" + movement + "]";
	}
	
}
