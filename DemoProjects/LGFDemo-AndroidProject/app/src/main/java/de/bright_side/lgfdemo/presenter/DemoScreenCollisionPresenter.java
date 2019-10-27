package de.bright_side.lgfdemo.presenter;

import java.util.ArrayList;
import java.util.List;

import de.bright_side.lgf.base.LPlatform;
import de.bright_side.lgf.logic.LScreenLogic;
import de.bright_side.lgf.model.LImage;
import de.bright_side.lgf.model.LInput;
import de.bright_side.lgf.model.LObject;
import de.bright_side.lgf.model.LPolygon;
import de.bright_side.lgf.model.LRenderStatistics;
import de.bright_side.lgf.model.LScreenModel;
import de.bright_side.lgf.model.LVector;
import de.bright_side.lgf.presenter.LScreenPresenter;
import de.bright_side.lgf.util.LMathsUtil;
import de.bright_side.lgf.view.LScreenView;
import de.bright_side.lgfdemo.model.DemoImage;

public class DemoScreenCollisionPresenter implements LScreenPresenter{
	private LScreenModel model;
	private LPlatform platform;
	private LScreenView view;
	private LScreenLogic screenLogic = new LScreenLogic();
	private static final LVector SHAPE_SIZE = new LVector(30, 30);
	private static final double SHAPE_POS_Y = 50;
	private static final double SHAPE_START_POS_X = 40;
	private static final double SHAPE_DISTANCE = 50;
	private static final int NUMBER_OF_STARS = 200;
	private static final LVector STAR_SIZE = new LVector(5, 5);
	private static final int STAR_MIN_SPEED = 10;
	private static final int STAR_VARIABLE_SPEED = 10;
	private static final double DEGREES_IN_CIRLE = 360;
	private LImage yellowStarImage;
	private LImage blueStarImage;
	private LObject circleShape;
	private LObject squareShape;
	private LObject triangleShape;
	
	public DemoScreenCollisionPresenter(LPlatform platform, LScreenView view) {
		this.platform = platform;
		this.view = view;
		try {
			yellowStarImage = platform.getImage(DemoImage.STAR_YELLOW);
			blueStarImage = platform.getImage(DemoImage.STAR_BLUE);
			model = createScreenModel();
			view.setModel(model);
			view.cameraPosToCenter();
		} catch (Exception e) {
			platform.handleInternalError(e);
		}
	}
	
	private LScreenModel createScreenModel() throws Exception {
		LScreenModel result = new LScreenModel();
		result.setBackgroundColor(platform.getCustomColor(100, 100, 200));
		result.setPlayerObjects(createPlayerObjects());
		result.setEnemyObjects(createEnemyObjects());
		result.setUiObjects(createUiObjects());
		return result;
	}
	
	private List<LObject> createEnemyObjects() throws Exception {
		List<LObject> result = new ArrayList<LObject>();
		for (int i = 0; i < NUMBER_OF_STARS; i++) {
			result.add(createStar());
		}
		return result;
	}

	private LObject createStar() throws Exception {
		LObject result = screenLogic.createObject(STAR_SIZE, createNewStarPos());
		result.setImage(yellowStarImage);
		result.setSpeed(STAR_MIN_SPEED + (Math.random() * STAR_VARIABLE_SPEED));
		result.setRotation(Math.random() * DEGREES_IN_CIRLE);
		return result;
	}

	private LVector createNewStarPos() {
		int pos = (int)(Math.random() * 4);
		
		double x = 0;
		double y = 0;
		switch (pos) {
		case 0:
			x = Math.random() * view.getScreenSize().x;
			break;
		case 1:
			x = Math.random() * view.getScreenSize().x;
			y = view.getScreenSize().y;
			break;
		case 2:
			y = Math.random() * view.getScreenSize().y;
			break;
		case 3:
			y = Math.random() * view.getScreenSize().y;
			x = view.getScreenSize().x;
			break;
		}
		
		
		return new LVector(x, y);
	}

	private List<LObject> createPlayerObjects() throws Exception {
		List<LObject> result = new ArrayList<LObject>();
		circleShape = createCircleObject();
		squareShape = createSquareObject();
		triangleShape = createTriangleObject();
		result.add(circleShape);
		result.add(squareShape);
		result.add(triangleShape);
		return result;
	}

	private LObject createSquareObject() throws Exception {
		LObject result = screenLogic.createObject(SHAPE_SIZE, new LVector(SHAPE_START_POS_X, SHAPE_POS_Y));
		result.setImage(platform.getImage(DemoImage.SHAPE_SQUARE));
		return result;
	}

	private LObject createTriangleObject() throws Exception {
		LObject result = screenLogic.createObject(SHAPE_SIZE, new LVector(SHAPE_START_POS_X + SHAPE_DISTANCE, SHAPE_POS_Y));
		result.setImage(platform.getImage(DemoImage.SHAPE_TRIANGLE));
		LPolygon collisionPolygon = new LPolygon();
		List<LVector> points = new ArrayList<LVector>();
		points.add(new LVector(0, 0));
		points.add(new LVector(1, 0.5));
		points.add(new LVector(0, 1));
		collisionPolygon.setPoints(points);
		result.setCollisionPolygon(collisionPolygon);
		return result;
	}
	
	private LObject createCircleObject() throws Exception {
		LObject result = screenLogic.createObject(SHAPE_SIZE, new LVector(SHAPE_START_POS_X + (SHAPE_DISTANCE * 2), SHAPE_POS_Y));
		result.setImage(platform.getImage(DemoImage.SHAPE_CIRCLE));
		return result;
	}
	
	private List<LObject> createUiObjects() throws Exception {
		List<LObject> result = new ArrayList<LObject>();
		result.add(DemoPresenterUtil.createTitleLabel(platform, view, "Collisions Demo"));
		result.addAll(DemoPresenterUtil.createScreenMenuButtons(platform, view));
		return result;
	}
	
	@Override
	public void update(LInput input, double secondsSinceLastUpdate, LRenderStatistics renderStatistics) {
		double maxX = view.getScreenSize().x;
		double maxY = view.getScreenSize().y;
		
		for (LObject i : model.getEnemyObjects()) {
            if (i.getSpeed() != 0) {
            	double amount = i.getSpeed() * secondsSinceLastUpdate;
            	LVector newPos = LMathsUtil.moveInDirection(i.getPos(), i.getRotation(), amount);
            	if ((newPos.x < 0) || (newPos.y < 0) || (newPos.x > maxX) || (newPos.y > maxY)) {
            		//: out of screen
            		newPos = createNewStarPos();
            		i.setRotation(Math.random() * DEGREES_IN_CIRLE);
            		i.setPos(newPos);
            	} else if (collidesWithShapes(i)) {
            		i.setPos(newPos);
        			i.setImage(blueStarImage);
        			i.setSpeed(0);
            	} else {
            		i.setPos(newPos);
            	}
            }
        }

	}

	private boolean collidesWithShapes(LObject object) {
		if (screenLogic.isColliding(object, squareShape, triangleShape)) {
			return true;
		}
		double circleRadiusAndStarRadius = (circleShape.getSize().x / 2) + (object.getSize().x / 2);
		if (LMathsUtil.getDistance(object.getPos(), circleShape.getPos()) < circleRadiusAndStarRadius) {
			return true;
		}
		
		return false;
	}

	@Override
	public void onClose() {
	}

}
