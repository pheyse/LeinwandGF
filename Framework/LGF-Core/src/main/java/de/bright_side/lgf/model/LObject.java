package de.bright_side.lgf.model;

import java.util.List;

public class LObject {
    private String type;
    private String id;
    private LVector pos;
    private LImage image;
    private LColor backgroundColor;
    private LVector size;
    private String text;
    private double speed;
    private double rotation;
    private LPolygon collisionPolygon;
    private boolean visible = true;
    private Long blinkingInterval;
    private LFont textFont;
    private LColor textColor;
    private LColor textOutlineColor;
    private LColor textShadowColor;
    private LVector textShadowOffset;
    private double textSize = 10;
    private List<LVector> path;
    private int nextPathItem;
    private double maximumRotationPerSecond;
    private List<LAnimationFrame> animationFrames;
    private int animationFrameIndex;
    private double remainingSecondsInAnimationFrame;
    private boolean touchable;
    private LAction touchAction;
    private List<LTween> tweens;
    private double opacity = 1;
    private LVector panelSize;
    private List<LObject> panelObjects;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LVector getPos() {
        return pos;
    }

    public void setPos(LVector pos) {
        this.pos = pos;
    }

    public LImage getImage() {
        return image;
    }

    public void setImage(LImage image) {
        this.image = image;
    }

    public LColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(LColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public LVector getSize() {
        return size;
    }

    public void setSize(LVector size) {
        this.size = size;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public LPolygon getCollisionPolygon() {
        return collisionPolygon;
    }

    public void setCollisionPolygon(LPolygon collisionPolygon) {
        this.collisionPolygon = collisionPolygon;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Long getBlinkingInterval() {
        return blinkingInterval;
    }

    public void setBlinkingInterval(Long blinkingInterval) {
        this.blinkingInterval = blinkingInterval;
    }

    public LFont getTextFont() {
        return textFont;
    }

    public void setTextFont(LFont textFont) {
        this.textFont = textFont;
    }

    public LColor getTextColor() {
        return textColor;
    }

    public void setTextColor(LColor textColor) {
        this.textColor = textColor;
    }

    public LColor getTextOutlineColor() {
        return textOutlineColor;
    }

    public void setTextOutlineColor(LColor textOutlineColor) {
        this.textOutlineColor = textOutlineColor;
    }

    public double getTextSize() {
        return textSize;
    }

    public void setTextSize(double textSize) {
        this.textSize = textSize;
    }

    public LColor getTextShadowColor() {
        return textShadowColor;
    }

    public void setTextShadowColor(LColor textShadowColor) {
        this.textShadowColor = textShadowColor;
    }

    public LVector getTextShadowOffset() {
        return textShadowOffset;
    }

    public void setTextShadowOffset(LVector textShadowOffset) {
        this.textShadowOffset = textShadowOffset;
    }

    public List<LVector> getPath() {
        return path;
    }

    public void setPath(List<LVector> path) {
        this.path = path;
    }

    public int getNextPathItem() {
        return nextPathItem;
    }

    public void setNextPathItem(int nextPathItem) {
        this.nextPathItem = nextPathItem;
    }

    public double getMaximumRotationPerSecond() {
        return maximumRotationPerSecond;
    }

    public void setMaximumRotationPerSecond(double maximumRotationPerSecond) {
        this.maximumRotationPerSecond = maximumRotationPerSecond;
    }
    
    public List<LAnimationFrame> getAnimationFrames() {
		return animationFrames;
	}

	public void setAnimationFrames(List<LAnimationFrame> animationFrames) {
		this.animationFrames = animationFrames;
	}

	public int getAnimationFrameIndex() {
		return animationFrameIndex;
	}

	public void setAnimationFrameIndex(int animationFrameIndex) {
		this.animationFrameIndex = animationFrameIndex;
	}

	public double getRemainingSecondsInAnimationFrame() {
		return remainingSecondsInAnimationFrame;
	}

	public void setRemainingSecondsInAnimationFrame(double remainingSecondsInAnimationFrame) {
		this.remainingSecondsInAnimationFrame = remainingSecondsInAnimationFrame;
	}

    public boolean isTouchable() {
        return touchable;
    }

    public void setTouchable(boolean touchable) {
        this.touchable = touchable;
    }

    public LAction getTouchAction() {
        return touchAction;
    }

    public void setTouchAction(LAction touchAction) {
        this.touchAction = touchAction;
    }

	public List<LTween> getTweens() {
		return tweens;
	}

	public void setTweens(List<LTween> tweens) {
		this.tweens = tweens;
	}

	public double getOpacity() {
		return opacity;
	}

	public void setOpacity(double opacity) {
		this.opacity = opacity;
	}

	public LVector getPanelSize() {
		return panelSize;
	}

	public void setPanelSize(LVector panelSize) {
		this.panelSize = panelSize;
	}

	public List<LObject> getPanelObjects() {
		return panelObjects;
	}

	public void setPanelObjects(List<LObject> panelObjects) {
		this.panelObjects = panelObjects;
	}

	@Override
	public String toString() {
		return "LObject [type=" + type + ", id=" + id + ", pos=" + pos + ", image=" + image + ", backgroundColor=" + backgroundColor + ", size=" + size + ", text=" + text
				+ ", speed=" + speed + ", rotation=" + rotation + ", collisionPolygon=" + collisionPolygon + ", visible=" + visible + ", blinkingInterval=" + blinkingInterval
				+ ", textFont=" + textFont + ", textColor=" + textColor + ", textOutlineColor=" + textOutlineColor + ", textShadowColor=" + textShadowColor + ", textShadowOffset="
				+ textShadowOffset + ", textSize=" + textSize + ", path=" + path + ", nextPathItem=" + nextPathItem + ", maximumRotationPerSecond=" + maximumRotationPerSecond
				+ ", animationFrames=" + animationFrames + ", animationFrameIndex=" + animationFrameIndex + ", remainingSecondsInAnimationFrame=" + remainingSecondsInAnimationFrame
				+ ", touchable=" + touchable + ", touchAction=" + touchAction + ", tweens=" + tweens + ", opacity=" + opacity + ", panelSize=" + panelSize + ", panelObjects="
				+ panelObjects + "]";
	}

}
