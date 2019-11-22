package de.bright_side.lgfdemo.presenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.bright_side.lgf.base.LPlatform;
import de.bright_side.lgf.logic.LScreenLogic;
import de.bright_side.lgf.logic.LTweenLogic;
import de.bright_side.lgf.model.LImage;
import de.bright_side.lgf.model.LInput;
import de.bright_side.lgf.model.LObject;
import de.bright_side.lgf.model.LRenderStatistics;
import de.bright_side.lgf.model.LScreenModel;
import de.bright_side.lgf.model.LTween;
import de.bright_side.lgf.model.LVector;
import de.bright_side.lgf.presenter.LScreenPresenter;
import de.bright_side.lgf.view.LScreenView;
import de.bright_side.lgfdemo.model.DemoFont;
import de.bright_side.lgfdemo.model.DemoImage;

public class DemoScreenTweenPresenter implements LScreenPresenter{
	private LScreenModel model;
	private LPlatform platform;
	private LScreenView view;
	private LScreenLogic screenLogic = new LScreenLogic();
	private LTweenLogic tweenLogic = new LTweenLogic();
	private LImage blueStarImage;

	public DemoScreenTweenPresenter(LPlatform platform, LScreenView view) {
		this.platform = platform;
		this.view = view;
		try {
			blueStarImage = platform.getImage(DemoImage.STAR_BLUE);
			model = createScreenModel();
			view.setModel(model);
			view.cameraPosToCenter();
			startTween();
		} catch (Exception e) {
			platform.handleInternalError(e);
		}
	}
	
	private LScreenModel createScreenModel() throws Exception {
		LScreenModel result = new LScreenModel();
		result.setBackgroundColor(platform.getCustomColor(211, 164, 22));
		result.setForegroundObjects(createForegroundObjects());
		result.setUiObjects(createUiObjects());
		return result;
	}
	
	private List<LObject> createForegroundObjects() throws Exception {
		List<LObject> result = new ArrayList<LObject>();
		return result;
	}

	private List<LObject> createUiObjects() throws Exception {
		List<LObject> result = new ArrayList<LObject>();
		result.add(DemoPresenterUtil.createTitleLabel(platform, view, "Tween Demo"));
		result.addAll(DemoPresenterUtil.createScreenMenuButtons(platform, view));

		return result;
	}

	private void startTween() {
		if (model.getForegroundObjects() == null) {
			model.setForegroundObjects(new ArrayList<LObject>());
		}
		if (model.getBackgroundObjects() == null) {
			model.setBackgroundObjects(new ArrayList<LObject>());
		}
		try {
			model.getForegroundObjects().addAll(createTweenObjects());
			model.getBackgroundObjects().add(createMessageTween("Hello! time: " + new Date()));
		} catch (Exception e) {
			platform.handleInternalError(e);
		}
	}

	private List<LObject> createTweenObjects() throws Exception {
		List<LObject> result = new ArrayList<LObject>();
		result.add(createMovingBox());
		result.add(createMovingGrowingStar());
		return result;
	}

	private LObject createMessageTween(String message) throws Exception {
		double posX = view.getScreenSize().x / 2;
		LVector messageHiddenPos = new LVector(posX, view.getScreenSize().y + 20);
		LVector messageVisiblePos = new LVector(posX, view.getScreenSize().y - 4.5);
		LObject result = screenLogic.createObject(new LVector(view.getScreenSize().x, 10), messageHiddenPos);
		result.setBackgroundColor(platform.getColorGray());
		screenLogic.setText(result, message, platform.getFont(DemoFont.PLAIN), 10, platform.getColorBlack());
		
		double durationInSeconds = 1;
		tweenLogic.addMoveTween(result, messageVisiblePos, durationInSeconds); //: move up
		tweenLogic.addMoveTween(result, messageVisiblePos, durationInSeconds * 2); //: stay
		tweenLogic.addMoveTween(result, messageHiddenPos, durationInSeconds); //: move down

		LTween tween = new LTween();
		tween.setCompletionAction(() -> model.setBackgroundObjects(new ArrayList<LObject>())); 
		tweenLogic.addTween(result, tween); //: remove
		
		return result;
	}

	private LObject createMovingBox() throws Exception {
		LObject result = screenLogic.createObject(new LVector(30, 10), new LVector(20, 20));
		result.setOpacity(0);
		result.setBackgroundColor(platform.getCustomColor(200, 200, 0));
		screenLogic.setText(result, "Tween", platform.getFont(DemoFont.MOONHOUSE), 10, platform.getColorBlack());
		
		tweenLogic.addOpacityTween(result, 1, 1);
		tweenLogic.addMoveTween(result, new LVector(150, 40), 4);
		
		return result;
	}

	private LObject createMovingGrowingStar() throws Exception {
		LObject result = screenLogic.createObject(new LVector(5, 5), new LVector(20, 40));
		result.setImage(platform.getImage(DemoImage.STAR_YELLOW));
		
		double durationInSeconds = 5;
		LTween tween = new LTween();
		tweenLogic.setMoveTween(tween, result.getPos(), new LVector(150, 60), durationInSeconds);
		tweenLogic.setSizeTween(tween, result.getSize(), new LVector(20, 20), durationInSeconds);
		tween.setCompletionAction(() -> result.setImage(blueStarImage));
		tweenLogic.setRotationTween(tween, result, 359, true, durationInSeconds);
		tweenLogic.addTween(result, tween);

		tweenLogic.addSizeTween(result, new LVector(10, 10), 1);
		
		return result;
	}
	
	@Override
	public void update(LInput input, double secondsSinceLastUpdate, LRenderStatistics renderStatistics) {
		List<LObject> objects = screenLogic.getAllObjects(model, false);
		tweenLogic.updateTweens(objects, secondsSinceLastUpdate);
	}

	@Override
	public void onClose() {
	}

}
