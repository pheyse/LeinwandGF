package de.bright_side.lgf.logic;

import java.util.ArrayList;
import java.util.List;

import de.bright_side.lgf.model.LAction;
import de.bright_side.lgf.model.LObject;
import de.bright_side.lgf.model.LTween;
import de.bright_side.lgf.model.LVector;
import de.bright_side.lgf.util.LMathsUtil;

public class LTweenLogic {
	private List<LTween> getTweens(LObject object) {
		List<LTween> tweens = object.getTweens();
		if (tweens == null) {
			tweens = new ArrayList<LTween>();
		}
		return tweens;
	}

	public void setMoveTween(LTween tween, LVector startPos, LVector destPos, double durationInSeconds) {
		LVector posChange = LMathsUtil.subtract(destPos, startPos);
		posChange = LMathsUtil.divide(posChange, durationInSeconds);
		tween.setDurationInSeconds(durationInSeconds);
		tween.setPosChange(posChange);
	}

	public void addMoveTween(LObject object, LVector destPos, double durationInSeconds) {
		LTween tween = new LTween();
		LVector startPos = getPosAfterAllTweens(object);
		setMoveTween(tween, startPos, destPos, durationInSeconds);
		addTween(object, tween);
	}

	private LVector getPosAfterAllTweens(LObject object) {
		LVector result = object.getPos();
		List<LTween> tweens = object.getTweens();
		if (tweens == null) {
			return result;
		}
		for (LTween i: tweens) {
			if (i.getPosChange() != null) {
				result = LMathsUtil.add(result, LMathsUtil.multiply(i.getPosChange(), i.getDurationInSeconds()));
			}
		}
		
		return result;
	}

	public void setSizeTween(LTween tween, LObject object, LVector destSize, double durationInSeconds) {
		LVector sizeChange = LMathsUtil.subtract(destSize, object.getSize());
		sizeChange = LMathsUtil.divide(sizeChange, durationInSeconds);
		tween.setDurationInSeconds(durationInSeconds);
		tween.setSizeChange(sizeChange);
	}

	public void addSizeTween(LObject object, LVector destSize, double durationInSeconds) {
		LTween tween = new LTween();
		setSizeTween(tween, object, destSize, durationInSeconds);
		addTween(object, tween);
	}
	
	public void setTextSizeTween(LTween tween, LObject object, double destSize, double durationInSeconds) {
		double sizeChange = destSize - object.getTextSize();
		sizeChange = sizeChange / durationInSeconds;
		tween.setDurationInSeconds(durationInSeconds);
		tween.setTextSizeChange(sizeChange);
	}
	
	public void addTextSizeTween(LObject object, double destSize, double durationInSeconds) {
		LTween tween = new LTween();
		setTextSizeTween(tween, object, destSize, durationInSeconds);
		addTween(object, tween);
	}
	
	public void setRotationTween(LTween tween, LObject object, double destRotation, boolean clockwise, double durationInSeconds) {
		double useDestRotation = destRotation;
		if (useDestRotation < 0) {
			useDestRotation = 0;
		} else if (useDestRotation > 360) {
			useDestRotation = 360;
		}
		
		double rotationChange = useDestRotation - object.getRotation();
		if (!clockwise) {
			if (rotationChange < 0) {
				rotationChange += 360;
			} else if (rotationChange > 0) {
				rotationChange -= 360;
			}
		}
		
		rotationChange /= durationInSeconds;
		tween.setRotationChange(rotationChange);
		tween.setDurationInSeconds(durationInSeconds);
	}
	
	public void addRotationTween(LObject object, double destRotation, boolean clockwise, double durationInSeconds) {
		LTween tween = new LTween();
		setRotationTween(tween, object, destRotation, clockwise, durationInSeconds);
		addTween(object, tween);
	}
	
	public void addTween(LObject object, LTween tween) {
		List<LTween> tweens = getTweens(object);
		tweens.add(tween);
		object.setTweens(tweens);
	}
	
	public void updateTweens(List<LObject> objects, double secondsSinceLastUpdate) {
		for (LObject i: objects) {
			updateTweens(i, secondsSinceLastUpdate);
		}
	}
	
	/**
	 * 
	 * @param object
	 * @return true if the tweens have been completed in this iteration
	 */
	public boolean updateTweens(LObject object, double secondsSinceLastUpdate) {
		List<LTween> tweens = object.getTweens();
		if (tweens == null){
			return false;
		}
		if (tweens.isEmpty()) {
			object.setTweens(null);
			return false;
		}
		
		double restSeconds = secondsSinceLastUpdate;
		while (restSeconds > 0) {
			LTween tween = tweens.get(0);
			double tweenDuration = tween.getDurationInSeconds(); 
			if (tweenDuration > restSeconds) {
				//: tween rest length longer than rest seconds of update
				tween.setDurationInSeconds(tweenDuration - restSeconds);
				performTween(object, tween, restSeconds);
				return false; //: all seconds used
			} else {
				performTween(object, tween, tweenDuration);
				performTweenAction(object, tween);
				tweens.remove(0);
				if (tweens.isEmpty()) {
					object.setTweens(null);
					return true;
				}
				restSeconds -= tweenDuration;
			}
		}
		return false;
	}

	private void performTweenAction(LObject object, LTween tween) {
		LAction completionAction = tween.getCompletionAction();
		if (completionAction != null) {
			completionAction.onAction();
		}
	}

	private void performTween(LObject object, LTween tween, double seconds) {
		if (tween.getPosChange() != null) {
			LVector posChange = LMathsUtil.multiply(tween.getPosChange(), seconds);
			object.setPos(LMathsUtil.add(object.getPos(), posChange));
		}

		if (tween.getSizeChange() != null) {
			LVector sizeChange = LMathsUtil.multiply(tween.getSizeChange(), seconds);
			object.setSize(LMathsUtil.add(object.getSize(), sizeChange));
		}

		if (tween.getRotationChange() != 0) {
			double rotationChange = tween.getRotationChange() * seconds;
			object.setRotation(LMathsUtil.rotate(object.getRotation(), rotationChange));
		}
		
		if (tween.getTextSizeChange() != 0) {
			double textSizeChange = tween.getTextSizeChange() * seconds;
			object.setTextSize(object.getTextSize() + textSizeChange);
		}
	}
	
}
