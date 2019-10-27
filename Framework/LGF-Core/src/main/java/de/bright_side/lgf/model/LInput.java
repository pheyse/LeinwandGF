package de.bright_side.lgf.model;

import java.util.List;
import java.util.Map;

public class LInput {
	private Map<Integer, LPointer> pointers;
	
	private List<LObject> touchedObjects;
	private boolean backButtonPressed;
	private boolean billingEventOccurred;

	private boolean wasTouched;
	
	public boolean isWasTouched() {
		return wasTouched;
	}
	public void setWasTouched(boolean wasTouched) {
		this.wasTouched = wasTouched;
	}

	public List<LObject> getTouchedObjects() {
		return touchedObjects;
	}

	public void setTouchedObjects(List<LObject> touchedObjects) {
		this.touchedObjects = touchedObjects;
	}

	public boolean isBackButtonPressed() {
		return backButtonPressed;
	}

	public void setBackButtonPressed(boolean backButtonPressed) {
		this.backButtonPressed = backButtonPressed;
	}
	public Map<Integer, LPointer> getPointers() {
		return pointers;
	}
	public void setPointers(Map<Integer, LPointer> pointers) {
		this.pointers = pointers;
	}

	public boolean isBillingEventOccurred() {
		return billingEventOccurred;
	}

	public void setBillingEventOccurred(boolean billingEventOccurred) {
		this.billingEventOccurred = billingEventOccurred;
	}

}
