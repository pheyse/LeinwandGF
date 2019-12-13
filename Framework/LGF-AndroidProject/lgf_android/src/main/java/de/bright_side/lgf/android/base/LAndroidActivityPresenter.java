package de.bright_side.lgf.android.base;

import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.bright_side.lgf.android.model.LAndroidActivityModel;
import de.bright_side.lgf.model.LInput;
import de.bright_side.lgf.model.LObject;
import de.bright_side.lgf.model.LPointer;
import de.bright_side.lgf.model.LVector;
import de.bright_side.lgf.util.LMathsUtil;
import de.bright_side.lgf.view.LScreenView;

public class LAndroidActivityPresenter {
    private LAndroidActivityModel model;
    private LVector virtualSize = null;

    public LAndroidActivityPresenter(LAndroidActivityModel model, LVector virtualSize) {
        this.model = model;
        this.virtualSize = virtualSize;
    }

    private LVector toPosInVirtualScreen(LVector gameViewComponentSize, double x, double y){
        LVector result = new LVector(x, y);
        result = LMathsUtil.divide(result, gameViewComponentSize);
        result = LMathsUtil.multiply(result, virtualSize);
        return result;
    }

    public void onTouchEvent(MotionEvent event, LScreenView gameView, LVector gameViewComponentSize) {
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        int maskedAction = event.getActionMasked(); //: masked = not specific to a pointer

        switch (maskedAction) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                PointF f = new PointF();
                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);
                model.getActiveInternalPointers().put(pointerId, f);

                //: update pointers
                LVector posInVirtualScreen = toPosInVirtualScreen(gameViewComponentSize, f.x, f.y);
                model.setClickPos(posInVirtualScreen);
                LPointer LPointer = new LPointer();
                LPointer.setTouchDownPos(posInVirtualScreen);
                LPointer.setPos(posInVirtualScreen);
                LPointer.setDragDistance(new LVector(0, 0));
                model.getActivePointers().put(pointerId, LPointer);

                //: set touched objects
                List<LObject> newTouchedObjects = gameView.getTouchedObjects(LAndroidUtil.applyCameraPos(posInVirtualScreen, virtualSize, gameView), posInVirtualScreen);
                if ((newTouchedObjects != null) && (!newTouchedObjects.isEmpty())){
                    if (model.getTouchedObjects() == null) {
                        model.setTouchedObjects(new ArrayList<LObject>());
                    }
                    for (LObject i: newTouchedObjects){
                        if (!model.getTouchedObjects().contains(i)){
                            if (i == null){
                                throw new RuntimeException("Cannot add game object null");
                            }
                            model.getTouchedObjects().add(i);
                        }
                    }
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: { // a pointer was moved
                for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                    PointF point = model.getActiveInternalPointers().get(event.getPointerId(i));
                    if (point != null) {
                        point.x = event.getX(i);
                        point.y = event.getY(i);
                    }

                    LPointer pointer = model.getActivePointers().get(event.getPointerId(i));
                    if (pointer != null){
                        LVector posInVirtualScreen = toPosInVirtualScreen(gameViewComponentSize, event.getX(i), event.getY(i));
                        LVector distance = LMathsUtil.subtract(pointer.getTouchDownPos(), posInVirtualScreen);
                        pointer.setDragDistance(LMathsUtil.multiply(distance, new LVector(-1, -1)));
                        pointer.setMovement(LMathsUtil.subtract(pointer.getPos(), posInVirtualScreen));
                        pointer.setPos(posInVirtualScreen);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                model.getActiveInternalPointers().remove(pointerId);
                model.getActivePointers().remove(pointerId);
                break;
            }
        }
    }

    public synchronized LInput buildInput() {
        synchronized (model){
            LInput result = new LInput();
            result.setWasTouched(model.getClickPos() != null);
            result.setPointers(copy(model.getActivePointers()));
            result.setBackButtonPressed(model.isBackButtonPressed());
            result.setTouchedObjects(copy(model.getTouchedObjects()));
            result.setBillingEventOccurred(model.isNewBillingEvent());

            model.setNewBillingEvent(false);
            model.setClickPos(null);
            model.setBackButtonPressed(false);
            model.setTouchedObjects(null);
            return result;
        }
    }

    private List<LObject> copy(List<LObject> objects) {
        if (objects == null){
            return null;
        }
        //: in this case it is ok, not to copy all the objects but just the map
        return new ArrayList<>(objects);
    }

    private Map<Integer, LPointer> copy(Map<Integer, LPointer> gamePointers) {
        Map<Integer, LPointer> result = new HashMap<>();

        for (Map.Entry<Integer, LPointer> i: gamePointers.entrySet()){
            result.put(i.getKey(), copy(i.getValue()));
        }

        return result;
    }

    private LPointer copy(LPointer pointer) {
        LPointer result = new LPointer();
        result.setPos(new LVector(pointer.getPos().x, pointer.getPos().y));
        result.setTouchDownPos(new LVector(pointer.getTouchDownPos().x, pointer.getTouchDownPos().y));
        LVector dragDistance = pointer.getDragDistance();
        if (dragDistance != null){
            result.setDragDistance(new LVector(dragDistance.x, dragDistance.y));
        }
        LVector movement = pointer.getMovement();
        if (movement != null){
            result.setMovement(new LVector(movement.x, movement.y));
        }
        return result;
    }


}
