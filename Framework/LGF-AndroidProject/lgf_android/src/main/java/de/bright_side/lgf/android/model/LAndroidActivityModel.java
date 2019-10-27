package de.bright_side.lgf.android.model;

import android.graphics.PointF;
import android.util.SparseArray;

import java.util.List;
import java.util.Map;

import de.bright_side.lgf.model.LObject;
import de.bright_side.lgf.model.LPointer;
import de.bright_side.lgf.model.LVector;

public class LAndroidActivityModel {
    private LVector dragStartPos;
    private LVector clickPos;
    private boolean backButtonPressed = false;
    private SparseArray<PointF> activePointers;
    private Map<Integer, LPointer> activeGamePointers;
    private List<LObject> touchedObjects = null;
    private boolean newBillingEvent = false;

    public LVector getDragStartPos() {
        return dragStartPos;
    }

    public void setDragStartPos(LVector dragStartPos) {
        this.dragStartPos = dragStartPos;
    }

    public LVector getClickPos() {
        return clickPos;
    }

    public void setClickPos(LVector clickPos) {
        this.clickPos = clickPos;
    }

    public boolean isBackButtonPressed() {
        return backButtonPressed;
    }

    public void setBackButtonPressed(boolean backButtonPressed) {
        this.backButtonPressed = backButtonPressed;
    }

    public SparseArray<PointF> getActivePointers() {
        return activePointers;
    }

    public void setActivePointers(SparseArray<PointF> activePointers) {
        this.activePointers = activePointers;
    }

    public Map<Integer, LPointer> getActiveGamePointers() {
        return activeGamePointers;
    }

    public void setActiveGamePointers(Map<Integer, LPointer> activeGamePointers) {
        this.activeGamePointers = activeGamePointers;
    }

    public List<LObject> getTouchedObjects() {
        return touchedObjects;
    }

    public void setTouchedObjects(List<LObject> touchedObjects) {
        this.touchedObjects = touchedObjects;
    }

    public boolean isNewBillingEvent() {
        return newBillingEvent;
    }

    public void setNewBillingEvent(boolean newBillingEvent) {
        this.newBillingEvent = newBillingEvent;
    }
}
