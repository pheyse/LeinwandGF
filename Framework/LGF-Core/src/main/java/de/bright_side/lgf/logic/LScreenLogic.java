package de.bright_side.lgf.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import de.bright_side.lgf.model.LAnimationFrame;
import de.bright_side.lgf.model.LColor;
import de.bright_side.lgf.model.LFont;
import de.bright_side.lgf.model.LObject;
import de.bright_side.lgf.model.LPolygon;
import de.bright_side.lgf.model.LScreenModel;
import de.bright_side.lgf.model.LVector;
import de.bright_side.lgf.util.LMathsUtil;

public class LScreenLogic {
    private static final double MAX_DEGREES = 360;

    public LPolygon createCollisionPolygon(LObject object){
        LPolygon polygon = object.getCollisionPolygon();
        if (polygon == null){
            polygon = createRectanglePolygon();
        }

        LPolygon result = adjustPolygonToPosRotationAndSize(object, polygon);
        return result;
    }

    private LPolygon adjustPolygonToPosRotationAndSize(LObject object, LPolygon polygon) {
        LPolygon result = LMathsUtil.multiply(polygon, object.getSize());
        LVector negativeHalfSize = LMathsUtil.multiply(object.getSize(), -.5);
        result = LMathsUtil.add(result, object.getPos());
        result = LMathsUtil.add(result, negativeHalfSize);
        if (object.getRotation() != 0){
            result = LMathsUtil.rotate(result, object.getPos(), object.getRotation());
        }
        return result;
    }

    private LPolygon createRectanglePolygon() {
        LPolygon result = new LPolygon();
        List<LVector> resultPoints = new ArrayList<>();
        result.setPoints(resultPoints);

        resultPoints.add(new LVector(0, 0));
        resultPoints.add(new LVector(1, 0));
        resultPoints.add(new LVector(1, 1));
        resultPoints.add(new LVector(0, 1));
        resultPoints.add(new LVector(0, 0));


        return result;
    }


    public List<LObject> getAllObjects(LScreenModel model, boolean includeUIObjects){
        List<LObject> result = new ArrayList<>();
        if (model.getBackgroundObjects() != null){
            result.addAll(model.getBackgroundObjects());
        }
        if (model.getEnemyObjects() != null){
            result.addAll(model.getEnemyObjects());
        }
        if (model.getPlayerObjects() != null){
            result.addAll(model.getPlayerObjects());
        }
        if (model.getForegroundObjects() != null){
        	result.addAll(model.getForegroundObjects());
        }
        if (includeUIObjects){
            if (model.getUiObjects() != null){
            	result.addAll(model.getUiObjects());
            }
        }
        return result;
    }

    public List<LObject> getAllObjectsExcludeEnemies(LScreenModel model){
    	List<LObject> result = new ArrayList<>();
    	if (model.getBackgroundObjects() != null){
    		result.addAll(model.getBackgroundObjects());
    	}
    	if (model.getPlayerObjects() != null){
    		result.addAll(model.getPlayerObjects());
    	}
    	if (model.getBackgroundObjects() != null){
    		result.addAll(model.getForegroundObjects());
    	}
    	return  result;
    }

    public List<LObject> getAllCollidingObjects(LObject objectA, LObject... listOfObjects){
        return  getAllCollidingObjects(objectA, Arrays.asList(listOfObjects));
    }

    public boolean isColliding(LObject objectA, LObject... listOfObjects){
        return !getAllCollidingObjects(objectA, Arrays.asList(listOfObjects)).isEmpty();
    }

    public List<LObject> getAllCollidingObjects(LObject objectA, List<LObject> listOfObjects){
        List<LObject> result = new ArrayList<>();
        if (objectA == null){
            return result;
        }
        boolean optimize = true;
        boolean tooFarAway = false;

        double objectARadius = 0;
        if (optimize){
            objectARadius = Math.max(objectA.getSize().getX(), objectA.getSize().getY());
        }

        LPolygon objectACollisionPolygon = null;
        for (LObject i: listOfObjects){
            if (i.isVisible()){
                double objectBRadius = 0;
                if (optimize){
                    objectBRadius = Math.max(i.getSize().getX(), i.getSize().getY());

                    double maxDistance = objectARadius + objectBRadius;
                    double objectAPosX = objectA.getPos().getX();
                    double objectAPosY = objectA.getPos().getY();
                    double objectBPosX = i.getPos().getX();
                    double objectBPosY = i.getPos().getY();

                    if ((Math.abs(objectAPosX - objectBPosX) > maxDistance) || (Math.abs(objectAPosY - objectBPosY) > maxDistance)){
                        tooFarAway = true;
                    } else {
                        tooFarAway = LMathsUtil.getDistance(objectA.getPos(), i.getPos()) > maxDistance;
                    }

                }
                if (!tooFarAway){
                    if (objectACollisionPolygon == null){
                        objectACollisionPolygon = createCollisionPolygon(objectA);
                    }
                    LPolygon enemyCollisionPolygon = createCollisionPolygon(i);
                    if (LMathsUtil.isIntersecting(objectACollisionPolygon, enemyCollisionPolygon)){
                        result.add(i);
                    }
                }
            }
        }
        return result;
    }

    /**
     * @param object object for which to calculate the needed rotation
     * @param dest destination
     * @return rotation and considering the fact that since the maximum degrees is 360, the neededRotation may be 361 or -1 if that is closer to the
     * current rotation. At the end the needed rotation is then put in the 0-360 range again
     */
    protected double getNeededRotationClosedToCurrentRotationAndOutOfRange(LObject object, LVector dest){
        double neededRotation = LMathsUtil.getAngleInDegrees(object.getPos(), dest);
        if (Math.abs(neededRotation + MAX_DEGREES - object.getRotation()) < Math.abs(neededRotation - object.getRotation())) {
            neededRotation += MAX_DEGREES;
        } else if (Math.abs(neededRotation - MAX_DEGREES - object.getRotation()) < Math.abs(neededRotation - object.getRotation())) {
            neededRotation -= MAX_DEGREES;
        }
        return neededRotation;
    }

    public void updateObjectToFollowPath(LObject object, double distanceToConsiderPathPointReached, double maximumSteerRotation) {
        List<LVector> path = object.getPath();
        if (path == null){
        	return;
        }
        LVector nextPos = path.get(object.getNextPathItem());
        //: path item reached? Go to next path item
        if (LMathsUtil.getDistance(object.getPos(), nextPos) <= distanceToConsiderPathPointReached) {
            object.setNextPathItem(object.getNextPathItem() + 1);
            if (object.getNextPathItem() >= object.getPath().size()) {
                object.setNextPathItem(0);
            }
            nextPos = path.get(object.getNextPathItem());
        }

        double neededRotation = getNeededRotationClosedToCurrentRotationAndOutOfRange(object, nextPos);

        double newRotation = object.getRotation();
        if (Math.abs(neededRotation - object.getRotation()) <= maximumSteerRotation) {
            //: if the needed rotation can be reached by the maximumRotationUpdatePerSecond value, do so (otherwise it would "over-steer")
            newRotation = neededRotation;
        } else if (object.getRotation() < neededRotation) {
            newRotation += maximumSteerRotation;
        } else {
            newRotation -= maximumSteerRotation;
        }

        object.setRotation(LMathsUtil.rotate(newRotation, 0)); //: use this method to put angle in range 0-360
    }
    
    public void animate(Collection<LObject> objects, double secondsSinceLastUpdate) {
		for (LObject i : objects) {
        	animate(i, secondsSinceLastUpdate);
		}
    }

    public void animate(LObject object, double secondsSinceLastUpdate) {
    	if (object.getAnimationFrames() == null) return;
    	double remainingTime = object.getRemainingSecondsInAnimationFrame() - secondsSinceLastUpdate;

    	if (remainingTime > 0) {
    		object.setRemainingSecondsInAnimationFrame(remainingTime);
    		return;
    	}
		int nextFrameIndex = object.getAnimationFrameIndex() + 1;
		List<LAnimationFrame> animationFrames = object.getAnimationFrames();
		if (nextFrameIndex >= animationFrames.size()) {
			nextFrameIndex = 0;
		}

		object.setAnimationFrameIndex(nextFrameIndex);
		LAnimationFrame animationFrame = animationFrames.get(nextFrameIndex);
		if (animationFrame == null) {
			return;
		}
        double remainingSecondsInAnimationFrame = remainingTime + animationFrame.getDurationInSeconds();

        object.setRemainingSecondsInAnimationFrame(remainingSecondsInAnimationFrame);
		object.setImage(animationFrame.getImage());
    }

    public LObject createObject(LVector size, LVector pos) {
        LObject result = new LObject();
        result.setVisible(true);
        result.setSize(size);
        result.setPos(pos);
		return result;
    }
    
    public LObject setText(LObject object, String text, LFont textFont, double textSize, LColor textColor) {
		object.setText(text);
		object.setTextColor(textColor);
		object.setTextSize(textSize);
		object.setTextFont(textFont);
    	return object;
    }
    
    public LObject setTextShadow(LObject object, LVector shadowOffset, LColor shadowColor) {
    	object.setTextShadowOffset(shadowOffset);
    	object.setTextShadowColor(shadowColor);
    	return object;
    }
    
    public LObject setAnimation(LObject result, List<LAnimationFrame> animationFrames) {
        result.setAnimationFrames(animationFrames);
        int frameIndex = (int)(Math.random() * animationFrames.size());
        result.setAnimationFrameIndex(frameIndex);
        result.setRemainingSecondsInAnimationFrame(Math.random() * animationFrames.get(frameIndex).getDurationInSeconds());
        result.setImage(animationFrames.get(frameIndex).getImage());
        return result;
    }

}
