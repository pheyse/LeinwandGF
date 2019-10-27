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
import de.bright_side.lgfdemo.model.DemoImage;


public class DemoScreenStartPresenter implements LScreenPresenter{
	private static final int STAR_MIN_SPEED = 10;
	private static final int STAR_MAX_SPEED = 20;
	private static final int NUMBER_OF_STARS = 200;
	private LScreenModel model;
	private LPlatform platform;
	private LScreenView view;
	private LScreenLogic screenLogic = new LScreenLogic();

	public DemoScreenStartPresenter(LPlatform platform, LScreenView view) {
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
		result.setBackgroundColor(platform.getCustomColor(0, 0, 80));
		result.setForegroundObjects(createForegroundObjects());
		result.setUiObjects(createUiObjects());
		return result;
	}
	
	private List<LObject> createUiObjects() throws Exception {
		List<LObject> result = new ArrayList<LObject>();
		result.add(DemoPresenterUtil.createTitleLabel(platform, view, "Leinwand GF Demo"));
		result.addAll(DemoPresenterUtil.createScreenMenuButtons(platform, view));
		return result;
	}

	private List<LObject> createForegroundObjects() throws Exception {
		List<LObject> result = new ArrayList<LObject>();
		
		for (int i = 0; i < NUMBER_OF_STARS; i ++) {
			result.add(createStar());
		}
		
		return result;
	}

	private LObject createStar() throws Exception {
		int x = (int)(Math.random() * view.getScreenSize().x);
		int y = (int)(Math.random() * view.getScreenSize().y);
		
		LObject result = screenLogic.createObject(new LVector(5, 5), new LVector(x, y));
		result.setType("star");
		if (Math.random() * 2 > 1) {
			result.setImage(platform.getImage(DemoImage.STAR_YELLOW));
		} else {
			result.setImage(platform.getImage(DemoImage.STAR_BLUE));
		}
		
		result.setRotation(Math.random() * 360);
		result.setSpeed(STAR_MIN_SPEED + Math.random() * (STAR_MAX_SPEED - STAR_MIN_SPEED));
		
		return result;
	}

	@Override
	public void update(LInput input, double secondsSinceLastUpdate, LRenderStatistics renderStatistics) {
		double maxX = view.getScreenSize().x;
		double maxY = view.getScreenSize().y;
		
		for (LObject i :screenLogic.getAllObjects(model, false)) {
            if (i.getSpeed() != 0) {
            	double amount = i.getSpeed() * secondsSinceLastUpdate;
            	LVector newPos = LMathsUtil.moveInDirection(i.getPos(), i.getRotation(), amount);
            	if ((newPos.x < 0) || (newPos.y < 0) || (newPos.x > maxX) || (newPos.y > maxY)) {
            		newPos = new LVector(maxX / 2, maxY / 2);
            	}
                i.setPos(newPos);
            }
        }
	}

	@Override
	public void onClose() {
	}

}
