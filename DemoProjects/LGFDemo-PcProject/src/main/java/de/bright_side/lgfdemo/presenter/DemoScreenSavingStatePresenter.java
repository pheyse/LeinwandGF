package de.bright_side.lgfdemo.presenter;

import java.util.ArrayList;
import java.util.List;

import de.bright_side.lgf.base.LPlatform;
import de.bright_side.lgf.base.LPlatform.StorageType;
import de.bright_side.lgf.logic.LScreenLogic;
import de.bright_side.lgf.logic.LTweenLogic;
import de.bright_side.lgf.model.LColor;
import de.bright_side.lgf.model.LImage;
import de.bright_side.lgf.model.LInput;
import de.bright_side.lgf.model.LObject;
import de.bright_side.lgf.model.LRenderStatistics;
import de.bright_side.lgf.model.LScreenModel;
import de.bright_side.lgf.model.LTween;
import de.bright_side.lgf.model.LVector;
import de.bright_side.lgf.presenter.LScreenPresenter;
import de.bright_side.lgf.util.LMathsUtil;
import de.bright_side.lgf.util.LUtil;
import de.bright_side.lgf.view.LScreenView;
import de.bright_side.lgfdemo.model.DemoFont;
import de.bright_side.lgfdemo.model.DemoImage;

public class DemoScreenSavingStatePresenter implements LScreenPresenter{
	private static final int NUMBER_OF_FOREGROUND_OBJECTS = 20;
	private static final int MIN_SPEED = 10;
	private static final int VAR_SPEED = 5;
	private static final double BALL_SIZE = 5;
	private static final String PATH_TO_STATE_DATA = "state.json";
	private LScreenModel model;
	private LPlatform platform;
	private LScreenView view;
	private LScreenLogic screenLogic = new LScreenLogic();
	private LTweenLogic tweenLogic = new LTweenLogic();
	private LImage ballImage;
	private LObject stateInfoLabel;
	private class Sate{
		public List<LObject> objects;
	}

	public DemoScreenSavingStatePresenter(LPlatform platform, LScreenView view) {
		this.platform = platform;
		this.view = view;
		try {
			ballImage = platform.getImage(DemoImage.SHAPE_CIRCLE);
			model = createScreenModel();
			view.setModel(model);
			view.cameraPosToCenter();
		} catch (Exception e) {
			platform.handleInternalError(e);
		}
	}
	
	private LScreenModel createScreenModel() throws Exception {
		LScreenModel result = new LScreenModel();
		result.setBackgroundColor(platform.getCustomColor(250, 250, 86));
		result.setForegroundObjects(createForegroundObjects());
		result.setUiObjects(createUiObjects());
		return result;
	}
	
	private List<LObject> createForegroundObjects() throws Exception {
		List<LObject> result = new ArrayList<LObject>();
		for (int i = 0; i < NUMBER_OF_FOREGROUND_OBJECTS; i++) {
			result.add(createForegroundObject(i));
		}
		return result;
	}

	private LObject createForegroundObject(int index) {
		LObject result = screenLogic.createObject(new LVector(BALL_SIZE, BALL_SIZE), new LVector(Math.random() * view.getScreenSize().x, Math.random() * view.getScreenSize().y));
		result.setImage(ballImage);
		result.setRotation(Math.random() * LMathsUtil.DEGREES_IN_CIRCLE);
		result.setSpeed(MIN_SPEED + (Math.random() * VAR_SPEED));
		result.setId("" + index);
		
		return result;
	}

	private List<LObject> createUiObjects() throws Exception {
		List<LObject> result = new ArrayList<LObject>();
		result.add(DemoPresenterUtil.createTitleLabel(platform, view, "Saving State Demo"));
		result.addAll(DemoPresenterUtil.createScreenMenuButtons(platform, view));
		
		LColor color = platform.getCustomColor(230, 100, 100);
		int startPosX = 40;
		int startPosY = 45;
		
		if (!DemoPresenterUtil.isLandscapeOrientation(view)) {
			startPosY += 30;
		}
		
		int buttonWidth = 60;
		result.add(DemoPresenterUtil.createScreenButton(platform, view, startPosX, 30 + startPosY, buttonWidth, "Save State", color, () -> saveState()));
		result.add(DemoPresenterUtil.createScreenButton(platform, view, startPosX, 15 + startPosY, buttonWidth, "Randomize State", color, () -> randomizeState()));
		result.add(DemoPresenterUtil.createScreenButton(platform, view, startPosX, 0 + startPosY, buttonWidth, "Load State", color, () -> loadState()));

		stateInfoLabel = createStateInfoLabel();
		result.add(stateInfoLabel);
		return result;
	}

	private LObject createStateInfoLabel() throws Exception {
		LObject result = screenLogic.createObject(new LVector(10, 10), LMathsUtil.divide(view.getScreenSize(), 2));
		screenLogic.setText(result, "Test", platform.getFont(DemoFont.PLAIN_BOLD), 20, platform.getColorBlack());
		result.setVisible(false);
		return result;
	}

	private void loadState() {
		try {
			Sate state = platform.readObjectOrNull(StorageType.INTERNAL, PATH_TO_STATE_DATA, Sate.class);
			if (state == null) {
				showStateInfo("There is no saved state");
				return;
			}
			List<LObject> objects = state.objects;
			for (LObject i: objects) {
				i.setImage(ballImage);
			}
			
			model.setForegroundObjects(objects);
			showStateInfo("Loaded state");
		} catch (Exception e) {
			platform.handleInternalError(e);
		}
	}

	private void randomizeState() {
		try {
			model.setForegroundObjects(createForegroundObjects());
			showStateInfo("Randomized state");
		} catch (Exception e) {
			platform.handleInternalError(e);
		}
	}

	private void showStateInfo(String text) {
		stateInfoLabel.setVisible(true);
		stateInfoLabel.setText(text);
		LTween tween = new LTween();
		tween.setDurationInSeconds(3);
		tween.setCompletionAction(() -> stateInfoLabel.setVisible(false));
		tweenLogic.addTween(stateInfoLabel, tween);
	}

	private void saveState() {
		try {
			Sate state = new Sate();
			state.objects = LUtil.copyWithoutResourceReferences(model.getForegroundObjects());
			platform.writeObject(StorageType.INTERNAL, PATH_TO_STATE_DATA, state);
			showStateInfo("Saved state");
		} catch (Exception e) {
			platform.handleInternalError(e);
		}
	}
	
	

	@Override
	public void update(LInput input, double secondsSinceLastUpdate, LRenderStatistics renderStatistics) {
		List<LObject> objects = screenLogic.getAllObjects(model, false);
		screenLogic.animate(objects, secondsSinceLastUpdate);

		double maxX = view.getScreenSize().x;
		double maxY = view.getScreenSize().y;
		for (LObject i :objects) {
            if (i.getSpeed() != 0) {
            	double amount = i.getSpeed() * secondsSinceLastUpdate;
            	LVector newPos = LMathsUtil.moveInDirection(i.getPos(), i.getRotation(), amount);
            	
            	boolean bounce = false;
            	
            	if (newPos.x < 0) {
            		newPos.x = 0;
            		bounce = true;
            	} else if (newPos.x > maxX) {
        			newPos.x = maxX;
        			bounce = true;
            	} else if (newPos.y < 0) {
    				newPos.y = 0;
    				bounce = true;
    			} else if (newPos.y > maxY) {
    				newPos.y = maxY;
    				bounce = true;
            	}

            	for (LObject other: objects) {
            		if (!other.getId().equals(i.getId())) {
            			if (LMathsUtil.getDistance(newPos, other.getPos()) < BALL_SIZE) {
            				bounce = true;
            				newPos = i.getPos();
            			}
            		}
            	}
            	
            	
            	if (bounce) {
            		i.setRotation(Math.random() * LMathsUtil.DEGREES_IN_CIRCLE);
            	}
            	
                i.setPos(newPos);
            }
        }
		
		tweenLogic.updateTweens(stateInfoLabel, secondsSinceLastUpdate);
	}

	@Override
	public void onClose() {
	}

}
