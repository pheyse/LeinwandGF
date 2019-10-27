package de.bright_side.lgfdemo.presenter;

import java.util.ArrayList;
import java.util.List;

import de.bright_side.lgf.base.LPlatform;
import de.bright_side.lgf.logic.LScreenLogic;
import de.bright_side.lgf.model.LImage;
import de.bright_side.lgf.model.LInput;
import de.bright_side.lgf.model.LObject;
import de.bright_side.lgf.model.LRenderStatistics;
import de.bright_side.lgf.model.LScreenModel;
import de.bright_side.lgf.model.LVector;
import de.bright_side.lgf.presenter.LScreenPresenter;
import de.bright_side.lgf.util.LMathsUtil;
import de.bright_side.lgf.view.LScreenView;
import de.bright_side.lgfdemo.model.DemoImage;

public class DemoScreenScrollingPresenter implements LScreenPresenter{
	private static final int NUMBER_OF_TILES_X = 100;
	private static final int NUMBER_OF_TILES_Y = 20;
	private static final int TILE_SIZE = 18;
	private static final double SCROLL_SPEED_X = 15;
	private static final int FOREGROUND_OBJECT_SIZE_MIN = 10;
	private static final int FOREGROUND_OBJECT_SIZE_VAR = 10;
	private static final int FOREGROUND_OBJECT_POS_Y_MIN = 20;
	private static final int FOREGROUND_OBJECT_POS_Y_VAR = 60;
	private static final double FOREGROUND_OBJECT_SPEED_MIN = 4;
	private static final double FOREGROUND_OBJECT_SPEED_VAR = 7;
	private static int MAX_SCROLL_POS_X = (TILE_SIZE * (NUMBER_OF_TILES_X - 10));
	
	private LScreenModel model;
	private LPlatform platform;
	private LScreenView view;
	private LScreenLogic screenLogic = new LScreenLogic();

	public DemoScreenScrollingPresenter(LPlatform platform, LScreenView view) {
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
		result.setBackgroundObjects(createTiles());
		result.setForegroundObjects(createForegroundObjects());
		result.setUiObjects(createUiObjects());
		return result;
	}
	
	private List<LObject> createTiles() throws Exception {
		List<LObject> result = new ArrayList<LObject>();
		
		List<LImage> images = new ArrayList<LImage>();
		images.add(platform.getImage(DemoImage.TILE_1));
		images.add(platform.getImage(DemoImage.TILE_2));
		images.add(platform.getImage(DemoImage.TILE_3));
		images.add(platform.getImage(DemoImage.TILE_4));
		
		for (int y = 0; y <= NUMBER_OF_TILES_Y; y ++) {
			for (int x = 0; x <= NUMBER_OF_TILES_X; x ++) {
				result.add(createTile(x, y, images));
			}
		}
		
		return result;
	}

	private LObject createTile(int x, int y, List<LImage> images) {
		LObject result = screenLogic.createObject(new LVector(TILE_SIZE, TILE_SIZE),  new LVector(x * TILE_SIZE, y * TILE_SIZE));
		int imageIndex = (int)(Math.random() * images.size());
		result.setImage(images.get(imageIndex));
		return result;
	}

	private List<LObject> createForegroundObjects() throws Exception {
		List<LObject> result = new ArrayList<LObject>();
		List<LImage> images = new ArrayList<LImage>();
		images.add(platform.getImage(DemoImage.SHAPE_CIRCLE));
		images.add(platform.getImage(DemoImage.SHAPE_SQUARE));
		images.add(platform.getImage(DemoImage.SHAPE_TRIANGLE));
		images.add(platform.getImage(DemoImage.STAR_BLUE));
		images.add(platform.getImage(DemoImage.STAR_YELLOW));
		
		for (int i = 0; i < NUMBER_OF_TILES_X; i += 3) {
			result.add(createForegroundObject(i, images));
		}
		
		return result;
	}

	private LObject createForegroundObject(int posX, List<LImage> images) {
		int size = FOREGROUND_OBJECT_SIZE_MIN + (int)(Math.random() * FOREGROUND_OBJECT_SIZE_VAR);
		double speed = FOREGROUND_OBJECT_SPEED_MIN + (Math.random() * FOREGROUND_OBJECT_SPEED_VAR);
		int posXOffset = (int) (Math.random() * TILE_SIZE);
		int posY = FOREGROUND_OBJECT_POS_Y_MIN + (int)(Math.random() * FOREGROUND_OBJECT_POS_Y_VAR);
		LObject result = screenLogic.createObject(new LVector(size, size),  new LVector((posX * TILE_SIZE) + posXOffset, posY));
		int imageIndex = (int)(Math.random() * images.size());
		result.setImage(images.get(imageIndex));
		
		result.setRotation(Math.random() * LMathsUtil.DEGREES_IN_CIRCLE);
		result.setSpeed(speed);
		return result;
	}

	private List<LObject> createUiObjects() throws Exception {
		List<LObject> result = new ArrayList<LObject>();
		result.add(DemoPresenterUtil.createTitleLabel(platform, view, "Scrolling Demo"));
		result.addAll(DemoPresenterUtil.createScreenMenuButtons(platform, view));

		return result;
	}

	@Override
	public void update(LInput input, double secondsSinceLastUpdate, LRenderStatistics renderStatistics) {
		List<LObject> objects = screenLogic.getAllObjects(model, false);
		screenLogic.animate(objects, secondsSinceLastUpdate);

		LVector newCameraPos = LMathsUtil.add(view.getCameraPos(), new LVector(SCROLL_SPEED_X * secondsSinceLastUpdate, 0));
		view.setCameraPos(newCameraPos);
		if (newCameraPos.x > MAX_SCROLL_POS_X) {
			view.cameraPosToCenter();
		}
		
		double maxY = view.getScreenSize().y;
		for (LObject i : model.getForegroundObjects()) {
			LVector newPos = LMathsUtil.moveInDirection(i.getPos(), i.getRotation(), secondsSinceLastUpdate * i.getSpeed());
			if ((newPos.y < 0) || (newPos.y > maxY)){
				i.setRotation(LMathsUtil.rotate(i.getRotation(), 180));
			} else {
				i.setPos(newPos);
			}
        }
	}

	@Override
	public void onClose() {
	}

}
