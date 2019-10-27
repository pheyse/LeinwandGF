package de.bright_side.lgf.model;

public class LImage {
	private LVector size;
	private Object imageObject;
	private Object preRenderedObject;
	private String id;
	private boolean hasAlpha;

	public LVector getSize() {
		return size;
	}

	public void setSize(LVector size) {
		this.size = size;
	}

	public Object getImageObject() {
		return imageObject;
	}

	public void setImageObject(Object imageObject) {
		this.imageObject = imageObject;
	}

	public Object getPreRenderedObject() {
		return preRenderedObject;
	}

	public void setPreRenderedObject(Object preRenderedObject) {
		this.preRenderedObject = preRenderedObject;
	}

	public boolean isHasAlpha() {
		return hasAlpha;
	}

	public void setHasAlpha(boolean hasAlpha) {
		this.hasAlpha = hasAlpha;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
