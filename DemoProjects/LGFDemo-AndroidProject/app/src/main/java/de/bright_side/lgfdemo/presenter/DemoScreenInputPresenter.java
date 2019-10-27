package de.bright_side.lgfdemo.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.bright_side.lgf.base.LPlatform;
import de.bright_side.lgf.logic.LScreenLogic;
import de.bright_side.lgf.model.LImage;
import de.bright_side.lgf.model.LInput;
import de.bright_side.lgf.model.LObject;
import de.bright_side.lgf.model.LPointer;
import de.bright_side.lgf.model.LRenderStatistics;
import de.bright_side.lgf.model.LScreenModel;
import de.bright_side.lgf.model.LVector;
import de.bright_side.lgf.presenter.LScreenPresenter;
import de.bright_side.lgf.util.LMathsUtil;
import de.bright_side.lgf.view.LScreenView;
import de.bright_side.lgfdemo.model.DemoFont;
import de.bright_side.lgfdemo.model.DemoImage;

public class DemoScreenInputPresenter implements LScreenPresenter{
	private LScreenModel model;
	private LPlatform platform;
	private LScreenView view;
	private LScreenLogic screenLogic = new LScreenLogic();
	private static final LVector SHAPE_SIZE = new LVector(30, 30);
	private static final double SHAPE_POS_Y = 50;
	private static final double SHAPE_START_POS_X = 40;
	private LObject baseStar;
	private LObject squareShape;
	private LImage yellowStarImage;
	private LImage blueStarImage;
	private List<LObject> starTrail;
	
	private static final int NUMBER_OF_STARS_IN_TRAIL = 40;

	public DemoScreenInputPresenter(LPlatform platform, LScreenView view) {
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
		result.setBackgroundColor(platform.getCustomColor(128, 163, 0));
		result.setPlayerObjects(createPlayerObjects());
		result.setUiObjects(createUiObjects());
		return result;
	}
	
	private List<LObject> createPlayerObjects() throws Exception {
		List<LObject> result = new ArrayList<LObject>();
		baseStar = createStarObject();
		squareShape = createSquareObject();
		starTrail = createStarTrail();
		result.add(baseStar);
		result.addAll(starTrail);
		result.add(createStarDragMeLabel(baseStar));
		result.add(squareShape);
		return result;
	}

	private List<LObject> createStarTrail() {
		List<LObject> result = new ArrayList<LObject>();
		for (int i = 0; i < NUMBER_OF_STARS_IN_TRAIL; i++) {
			LObject item = screenLogic.createObject(baseStar.getSize(), baseStar.getPos());
			item.setImage(blueStarImage);
			item.setVisible(false);
			result.add(item);
		}

		return result;
	}

	private LObject createStarDragMeLabel(LObject labeldObject) throws Exception {
		LObject result = screenLogic.createObject(SHAPE_SIZE, LMathsUtil.add(labeldObject.getPos(), 0, labeldObject.getSize().y));
		addDragText(result);		
		return result;
	}

	private LObject createSquareObject() throws Exception {
		LObject result = screenLogic.createObject(SHAPE_SIZE, new LVector(SHAPE_START_POS_X, SHAPE_POS_Y));
		result.setImage(platform.getImage(DemoImage.SHAPE_SQUARE));
		addDragText(result);
		return result;
	}

	private LObject createStarObject() throws Exception {
		LObject result = screenLogic.createObject(new LVector(10, 10), new LVector(120, SHAPE_POS_Y));
		result.setImage(yellowStarImage);
		return result;
	}

	private void addDragText(LObject result) throws Exception {
		screenLogic.setText(result, "Drag me!", platform.getFont(DemoFont.PLAIN), 10, platform.getColorRed());
		result.setTextOutlineColor(platform.getColorWhite());
	}
	
	private List<LObject> createUiObjects() throws Exception {
		List<LObject> result = new ArrayList<LObject>();
		result.add(DemoPresenterUtil.createTitleLabel(platform, view, "Input Demo"));
		result.addAll(DemoPresenterUtil.createScreenMenuButtons(platform, view));
		return result;
	}
	
	@Override
	public void update(LInput input, double secondsSinceLastUpdate, LRenderStatistics renderStatistics) {
		LVector topLeft = LMathsUtil.multiply(squareShape.getSize(), 0.5);
		LVector bottomRightLeft = LMathsUtil.subtract(view.getScreenSize(), topLeft);

		Map<Integer, LPointer> pointers = input.getPointers();
		boolean draggingStar = false;
		if (pointers != null) {
			for (Entry<Integer, LPointer> i: pointers.entrySet()) {
				LPointer pointer = i.getValue();
				if (LMathsUtil.isInCenteredArea(pointer.getPos(), squareShape.getPos(), squareShape.getSize())) {
					LVector newPos = pointer.getPos();
					newPos = LMathsUtil.putInRange(newPos, topLeft, bottomRightLeft);
					squareShape.setPos(newPos);
				}
				
				if (LMathsUtil.isInCenteredArea(pointer.getTouchDownPos(), baseStar.getPos(), baseStar.getSize())) {
					updateStarTrail(baseStar.getPos(), pointer.getPos());
					draggingStar = true;
				}
			}
		}
		if (!draggingStar) {
			hideStarTrail();
		}
	}

	private void updateStarTrail(LVector startPos, LVector endPos) {
		int amount = starTrail.size();
		LVector pos = startPos;
		LVector distance = LMathsUtil.subtract(endPos, startPos);
		LVector movementPerItem = LMathsUtil.divide(distance, amount);
		for (int i = 0; i < amount; i++) {
			starTrail.get(i).setVisible(true);
			starTrail.get(i).setPos(pos);
			pos = LMathsUtil.add(pos, movementPerItem);
		}
	}

	private void hideStarTrail() {
		for (LObject i: starTrail) {
			i.setVisible(false);
		}
	}

	@Override
	public void onClose() {
	}

}
