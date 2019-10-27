package de.bright_side.lgfdemo.presenter;

import java.util.ArrayList;
import java.util.List;

import de.bright_side.lgf.base.LPlatform;
import de.bright_side.lgf.logic.LScreenLogic;
import de.bright_side.lgf.model.LInput;
import de.bright_side.lgf.model.LObject;
import de.bright_side.lgf.model.LRenderStatistics;
import de.bright_side.lgf.model.LScreenModel;
import de.bright_side.lgf.model.LVector;
import de.bright_side.lgf.presenter.LScreenPresenter;
import de.bright_side.lgf.util.LMathsUtil;
import de.bright_side.lgf.view.LScreenView;
import de.bright_side.lgfdemo.model.DemoAnimation;

public class DemoScreenAnimationPresenter implements LScreenPresenter{
	private LScreenModel model;
	private LPlatform platform;
	private LScreenView view;
	private LScreenLogic screenLogic = new LScreenLogic();

	public DemoScreenAnimationPresenter(LPlatform platform, LScreenView view) {
		this.platform = platform;
		this.view = view;
		try {
			model = createScreenModel();
			view.setModel(model);
			view.cameraPosToCenter();
		} catch (Exception e) {
			platform.handleInternalError(e);
		}
	}
	
	private LScreenModel createScreenModel() throws Exception {
		LScreenModel result = new LScreenModel();
		result.setBackgroundColor(platform.getCustomColor(0, 172, 172));
		result.setForegroundObjects(createForegroundObjects());
		result.setUiObjects(createUiObjects());
		return result;
	}
	
	private List<LObject> createForegroundObjects() throws Exception {
		List<LObject> result = new ArrayList<LObject>();
		result.add(createAnimationObject(20, 30, 90, 5));
		result.add(createAnimationObject(20, 40, 90, 10));
		result.add(createAnimationObject(20, 50, 90, 15));
		return result;
	}

	private LObject createAnimationObject(int posX, int posY, int rotation, double speed) throws Exception {
		LObject result = screenLogic.createObject(new LVector(10, 10), new LVector(posX, posY));
		screenLogic.setAnimation(result, platform.getAnimationFrames(DemoAnimation.ANIMATION_A));
		result.setSpeed(speed);
		result.setRotation(rotation);
		return result;
	}

	private List<LObject> createUiObjects() throws Exception {
		List<LObject> result = new ArrayList<LObject>();
		result.add(DemoPresenterUtil.createTitleLabel(platform, view, "Animation Demo"));
		result.addAll(DemoPresenterUtil.createScreenMenuButtons(platform, view));
		return result;
	}

	@Override
	public void update(LInput input, double secondsSinceLastUpdate, LRenderStatistics renderStatistics) {
		List<LObject> objects = screenLogic.getAllObjects(model, false);
		screenLogic.animate(objects, secondsSinceLastUpdate);

		double maxX = view.getScreenSize().x;
		for (LObject i :objects) {
            if (i.getSpeed() != 0) {
            	double amount = i.getSpeed() * secondsSinceLastUpdate;
            	LVector newPos = LMathsUtil.moveInDirection(i.getPos(), i.getRotation(), amount);
            	
            	if (newPos.x > maxX) {
            		newPos.x = 0;
            	}
                i.setPos(newPos);
            }
        }
	}

	@Override
	public void onClose() {
	}

}
