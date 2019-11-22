package de.bright_side.lgfdemo.presenter;

import java.util.ArrayList;
import java.util.List;

import de.bright_side.lgf.base.LPlatform;
import de.bright_side.lgf.logic.LScreenLogic;
import de.bright_side.lgf.model.LColor;
import de.bright_side.lgf.model.LImage;
import de.bright_side.lgf.model.LImageResource;
import de.bright_side.lgf.model.LInput;
import de.bright_side.lgf.model.LObject;
import de.bright_side.lgf.model.LRenderStatistics;
import de.bright_side.lgf.model.LScreenModel;
import de.bright_side.lgf.model.LVector;
import de.bright_side.lgf.presenter.LScreenPresenter;
import de.bright_side.lgf.util.LMathsUtil;
import de.bright_side.lgf.view.LScreenView;
import de.bright_side.lgfdemo.model.DemoFont;
import de.bright_side.lgfdemo.model.DemoImage;


public class DemoScreenPanelPresenter implements LScreenPresenter{
	private static final int NUMBER_OF_TILES_X = 100;
	private static final int NUMBER_OF_TILES_Y = 20;
	private static final int TILE_SIZE = 18;
	private static final double PANEL_SPEED = 0.1;
	private static final double PANEL_MIN_Y = 30;
	private static final double PANEL_MAX_Y = 60;
	private static final LVector PANEL_BASE_SIZE = new LVector(30, 20);
	private static final double PANEL_SIZE_MIN_X = 10;
	private static final double PANEL_SIZE_MAX_X = 50;
	private double panelSizeChangeSpeed = 0.1; 
	private LVector panelSize = PANEL_BASE_SIZE;
	
	private LScreenModel model;
	private LPlatform platform;
	private LScreenView view;
	private LScreenLogic screenLogic = new LScreenLogic();

	public DemoScreenPanelPresenter(LPlatform platform, LScreenView view) {
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
		result.setBackgroundColor(platform.getCustomColor(0, 80, 80));
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

		LColor colorB = platform.getCustomColor(80, 80, 255);
		
		result.add(createPanel("Panel A", 30, 40, null, DemoImage.STAR_YELLOW, 1));
		result.add(createPanel("Panel B", 90, 60, colorB, null, 0.5));
		result.add(createPanel("Panel C", 150, 50, null, DemoImage.SHAPE_CIRCLE, 0.7));
		
		return result;
	}

	private LObject createPanel(String label, int posX, int posY, LColor color, LImageResource image, double opacity) throws Exception {
		LObject result = screenLogic.createObject(PANEL_BASE_SIZE, new LVector(posX, posY));
		result.setOpacity(opacity);
		
		result.setBackgroundColor(color);
		if (image != null) {
			result.setImage(platform.getImage(image));
		}
		
		result.setPanelSize(new LVector(100, 100));
		result.setPanelObjects(new ArrayList<LObject>());
		result.setId("Panel");
		
		LObject subObject1 = screenLogic.createObject(new LVector(100, 10), new LVector(50, 5));
		subObject1.setId("subObject1");
		subObject1.setBackgroundColor(platform.getColorGreen());
		subObject1.setOpacity(0.5);
		result.getPanelObjects().add(subObject1);

		final LObject subObject2 = screenLogic.createObject(new LVector(80, 40), new LVector(50, 80));
		subObject2.setId("subObject2");
		subObject2.setBackgroundColor(platform.getColorYellow());
		subObject2.setOpacity(1);
		subObject2.setTouchable(true);
		subObject2.setTouchAction(() -> subObject2.setBackgroundColor(createRandomLightColor()));
		screenLogic.setText(subObject2, "Click me!", platform.getFont(DemoFont.PLAIN_BOLD), 20, platform.getColorBlue());
		result.getPanelObjects().add(subObject2);
		
		LObject subObject3 = screenLogic.createObject(new LVector(100, 30), new LVector(50, 50));
		subObject3.setId("subObject3");
		subObject3.setOpacity(1);
		screenLogic.setText(subObject3, label, platform.getFont(DemoFont.PLAIN_BOLD), 50, platform.getColorWhite());
		screenLogic.setTextShadow(subObject3, new LVector(1, 1), platform.getColorBlack()); 
		subObject3.setTextOutlineColor(platform.getColorBlack());
		result.getPanelObjects().add(subObject3);
		
		result.setSpeed(PANEL_SPEED);
		
		return result;
	}
	
	private int getRandomBetween(int min, int max) {
		double diff = max - min;
		return min + (int)(Math.random() * diff);
	}

	private LColor createRandomLightColor() {
		int min = 100;
		int max = 255;
		return platform.getCustomColor(getRandomBetween(min, max), getRandomBetween(min, max), getRandomBetween(min, max));
	}

	private List<LObject> createUiObjects() throws Exception {
		List<LObject> result = new ArrayList<LObject>();
		result.add(DemoPresenterUtil.createTitleLabel(platform, view, "Panel Demo"));
		result.addAll(DemoPresenterUtil.createScreenMenuButtons(platform, view));

		return result;
	}

	@Override
	public void update(LInput input, double secondsSinceLastUpdate, LRenderStatistics renderStatistics) {
		panelSize = LMathsUtil.add(panelSize, panelSizeChangeSpeed);
		if (panelSize.getX() >= PANEL_SIZE_MAX_X) {
			panelSizeChangeSpeed = - Math.abs(panelSizeChangeSpeed);
		} else if (panelSize.getX() <= PANEL_SIZE_MIN_X) {
			panelSizeChangeSpeed = Math.abs(panelSizeChangeSpeed);
		}
		
		for (LObject i: model.getForegroundObjects()) {
			LVector newPos = LMathsUtil.add(i.getPos(), new LVector(0, i.getSpeed()));
			if (newPos.y > PANEL_MAX_Y) {
				newPos.y = PANEL_MAX_Y;
				i.setSpeed(- PANEL_SPEED);
			} else if (newPos.y < PANEL_MIN_Y) {
				newPos.y = PANEL_MIN_Y;
				i.setSpeed(PANEL_SPEED);
			}
			i.setPos(newPos);
			i.setSize(panelSize);
			
		}
	}

	@Override
	public void onClose() {
	}

}
