package de.bright_side.lgf.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.bright_side.lgf.model.LObject;
import de.bright_side.lgf.model.LVector;

public class LTweenLogicTest {
	private LTweenLogic tweenLogic = new LTweenLogic();
	
	private LObject createObject() {
		LObject object = new LObject();
		object.setPos(new LVector(0, 0));
		object.setSize(new LVector(100, 100));
		object.setTextSize(20);
		object.setRotation(90);
		return object;
	}
	
	@Test
	public void updateTween_moveTweenNotDone() {
		LObject object = createObject();
		
		tweenLogic.addMoveTween(object, new LVector(10, 20), 10);
		boolean result = tweenLogic.updateTweens(object, 5);
		
		assertEquals(false, result);
		assertEquals(5, object.getPos().x);
		assertEquals(10, object.getPos().y);
	}

	@Test
	public void updateTween_moveTweenExactlyDone() {
		LObject object = createObject();
		
		tweenLogic.addMoveTween(object, new LVector(10, 20), 10);
		boolean result = tweenLogic.updateTweens(object, 10);
		
		assertEquals(true, result);
		assertEquals(10, object.getPos().x);
		assertEquals(20, object.getPos().y);
	}

	@Test
	public void updateTween_moveTweenDone() {
		LObject object = createObject();
		
		tweenLogic.addMoveTween(object, new LVector(10, 20), 10);
		boolean result = tweenLogic.updateTweens(object, 20);
		
		assertEquals(true, result);
		assertEquals(10, object.getPos().x);
		assertEquals(20, object.getPos().y);
	}

	@Test
	public void updateTween_sizeTweenNotDone() {
		LObject object = createObject();
		
		tweenLogic.addSizeTween(object, new LVector(10, 20), 10);
		boolean result = tweenLogic.updateTweens(object, 5);
		
		assertEquals(false, result);
		assertEquals(55, object.getSize().x);
		assertEquals(60, object.getSize().y);
	}
	
	@Test
	public void updateTween_sizeTweenExactlyDone() {
		LObject object = createObject();
		
		tweenLogic.addSizeTween(object, new LVector(10, 20), 10);
		boolean result = tweenLogic.updateTweens(object, 10);
		
		assertEquals(true, result);
		assertEquals(10, object.getSize().x);
		assertEquals(20, object.getSize().y);
	}
	
	@Test
	public void updateTween_sizeTweenDone() {
		LObject object = createObject();
		
		tweenLogic.addSizeTween(object, new LVector(10, 20), 10);
		boolean result = tweenLogic.updateTweens(object, 20);
		
		assertEquals(true, result);
		assertEquals(10, object.getSize().x);
		assertEquals(20, object.getSize().y);
	}
	
	@Test
	public void updateTween_rotationForwardClockwiseTweenNotDone() {
		LObject object = createObject();
		
		tweenLogic.addRotationTween(object, 100, true, 10);
		boolean result = tweenLogic.updateTweens(object, 5);
		
		assertEquals(false, result);
		assertEquals(95, object.getRotation());
	}

	@Test
	public void updateTween_rotationForwardCounterClockwiseTweenNotDone() {
		LObject object = createObject();
		
		tweenLogic.addRotationTween(object, 100, false, 10);
		boolean result = tweenLogic.updateTweens(object, 5);
		
		assertEquals(false, result);
		assertEquals((90 - (350 / 2)) + 360, object.getRotation());
	}

	@Test
	public void updateTween_rotationForwardClockwiseTweenDone() {
		LObject object = createObject();
		
		tweenLogic.addRotationTween(object, 100, true, 10);
		boolean result = tweenLogic.updateTweens(object, 11);
		
		assertEquals(true, result);
		assertEquals(100, object.getRotation());
	}

	@Test
	public void updateTween_textSizeTweenNotDone() {
		LObject object = createObject();
		
		tweenLogic.addTextSizeTween(object, 30, 10);
		boolean result = tweenLogic.updateTweens(object, 5);
		
		assertEquals(false, result);
		assertEquals(25, object.getTextSize());
	}

	@Test
	public void updateTween_textSizeTweenDone() {
		LObject object = createObject();
		
		tweenLogic.addTextSizeTween(object, 30, 10);
		boolean result = tweenLogic.updateTweens(object, 11);
		
		assertEquals(true, result);
		assertEquals(30, object.getTextSize());
	}
	

	@Test
	public void updateTween_addAndMoveTweensNotDone() {
		LObject object = createObject();
		
		tweenLogic.addMoveTween(object, new LVector(10, 20), 10);
		tweenLogic.addSizeTween(object, new LVector(10, 20), 10);
		assertEquals(2, object.getTweens().size());
		boolean result = tweenLogic.updateTweens(object, 15);
		
		assertEquals(false, result);
		assertEquals(10, object.getPos().x);
		assertEquals(20, object.getPos().y);
		assertEquals(55, object.getSize().x);
		assertEquals(60, object.getSize().y);
		assertEquals(1, object.getTweens().size());
	}
	
	@Test
	public void updateTween_addAndMoveTweensExactlyDone() {
		LObject object = createObject();
		
		tweenLogic.addMoveTween(object, new LVector(10, 20), 10);
		tweenLogic.addSizeTween(object, new LVector(10, 20), 5);
		assertEquals(2, object.getTweens().size());
		boolean result = tweenLogic.updateTweens(object, 15);
		
		assertEquals(true, result);
		assertEquals(10, object.getPos().x);
		assertEquals(20, object.getPos().y);
		assertEquals(10, object.getSize().x);
		assertEquals(20, object.getSize().y);
		assertEquals(null, object.getTweens());
	}
	
	@Test
	public void updateTween_addAndMoveTweensDone() {
		LObject object = createObject();
		
		tweenLogic.addMoveTween(object, new LVector(10, 20), 10);
		tweenLogic.addSizeTween(object, new LVector(10, 20), 5);
		assertEquals(2, object.getTweens().size());
		boolean result = tweenLogic.updateTweens(object, 20);
		
		assertEquals(true, result);
		assertEquals(10, object.getPos().x);
		assertEquals(20, object.getPos().y);
		assertEquals(10, object.getSize().x);
		assertEquals(20, object.getSize().y);
		assertEquals(null, object.getTweens());
	}
	
	@Test
	public void updateTween_multipleMoveTweensFirstHalfDone() {
		LObject object = createObject();
		object.setPos(new LVector(200, 100));
		
		tweenLogic.addMoveTween(object, new LVector(300, 150), 10);
		tweenLogic.addMoveTween(object, new LVector(300, 150), 10);
		tweenLogic.addMoveTween(object, new LVector(0, 0), 10);
		assertEquals(3, object.getTweens().size());
		boolean result = tweenLogic.updateTweens(object, 5);
		
		assertEquals(false, result);
		assertEquals(250, object.getPos().x);
		assertEquals(125, object.getPos().y);
	}
	
	@Test
	public void updateTween_multipleMoveTweensSecondHalfDone() {
		LObject object = createObject();
		object.setPos(new LVector(200, 100));
		
		tweenLogic.addMoveTween(object, new LVector(300, 150), 10);
		tweenLogic.addMoveTween(object, new LVector(300, 150), 10);
		tweenLogic.addMoveTween(object, new LVector(0, 0), 10);
		assertEquals(3, object.getTweens().size());
		boolean result = tweenLogic.updateTweens(object, 15);
		
		assertEquals(false, result);
		assertEquals(300, object.getPos().x);
		assertEquals(150, object.getPos().y);
	}
	
	@Test
	public void updateTween_multipleMoveTweensSecondExactlyDone() {
		LObject object = createObject();
		object.setPos(new LVector(200, 100));
		
		tweenLogic.addMoveTween(object, new LVector(300, 150), 10);
		tweenLogic.addMoveTween(object, new LVector(300, 150), 10);
		tweenLogic.addMoveTween(object, new LVector(0, 0), 10);
		assertEquals(3, object.getTweens().size());
		boolean result = tweenLogic.updateTweens(object, 20);
		
		assertEquals(false, result);
		assertEquals(300, object.getPos().x);
		assertEquals(150, object.getPos().y);
	}
	
	@Test
	public void updateTween_multipleMoveTweensThirdHalfDone() {
		LObject object = createObject();
		object.setPos(new LVector(200, 100));
		
		tweenLogic.addMoveTween(object, new LVector(300, 150), 10);
		tweenLogic.addMoveTween(object, new LVector(300, 150), 10);
		tweenLogic.addMoveTween(object, new LVector(0, 0), 10);
		assertEquals(3, object.getTweens().size());
		boolean result = tweenLogic.updateTweens(object, 25);
		
		assertEquals(false, result);
		assertEquals(150, object.getPos().x);
		assertEquals(75, object.getPos().y);
	}
	
	@Test
	public void updateTween_multipleMoveTweensAllDone() {
		LObject object = createObject();
		object.setPos(new LVector(200, 100));
		
		tweenLogic.addMoveTween(object, new LVector(300, 150), 10);
		tweenLogic.addMoveTween(object, new LVector(300, 150), 10);
		tweenLogic.addMoveTween(object, new LVector(0, 0), 10);
		assertEquals(3, object.getTweens().size());
		boolean result = tweenLogic.updateTweens(object, 40);
		
		assertEquals(true, result);
		assertEquals(0, object.getPos().x);
		assertEquals(0, object.getPos().y);
		assertEquals(null, object.getTweens());
	}
	
	
	
	
}
