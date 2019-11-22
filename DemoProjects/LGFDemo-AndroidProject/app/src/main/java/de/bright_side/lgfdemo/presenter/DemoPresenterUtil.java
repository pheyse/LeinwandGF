package de.bright_side.lgfdemo.presenter;

import java.util.ArrayList;
import java.util.List;

import de.bright_side.lgf.base.LPlatform;
import de.bright_side.lgf.logic.LScreenLogic;
import de.bright_side.lgf.model.LAction;
import de.bright_side.lgf.model.LColor;
import de.bright_side.lgf.model.LObject;
import de.bright_side.lgf.model.LVector;
import de.bright_side.lgf.view.LScreenView;
import de.bright_side.lgfdemo.base.DemoConstants;
import de.bright_side.lgfdemo.model.DemoFont;

public class DemoPresenterUtil {
	private static final double BUTTON_WIDTH = 18;
	private static final double BUTTON_HEIGHT = 10;
	private static final double BUTTON_DISTANCE = BUTTON_WIDTH + 3;
	private static final double MENU_BUTTON_POS_FROM_BOTTOM = BUTTON_HEIGHT + 5;
	
	public static LObject createTitleLabel(LPlatform platform, LScreenView view, String text) throws Exception {
		LScreenLogic screenLogic = new LScreenLogic();
		LObject result = screenLogic.createObject(new LVector(30, 30), new LVector(view.getScreenSize().x / 2, 10));
		screenLogic.setText(result, text, platform.getFont(DemoFont.PLAIN_BOLD), DemoConstants.TITLE_FONT_SIZE, platform.getColorWhite());
		screenLogic.setTextShadow(result, new LVector(1.5, 1.5), platform.getColorBlack()); 
		result.setTextOutlineColor(platform.getColorGray());
		return result;
	}
	
	public static List<LObject> createScreenMenuButtons(LPlatform platform, LScreenView view) throws Exception {
		List<LObject> result = new ArrayList<LObject>();
		
		boolean landscape = isLandscapeOrientation(view);
		
		int startPosX = 15;
		int posX = startPosX;
		int posY = (int)MENU_BUTTON_POS_FROM_BOTTOM;
		int useButtonDistance = (int)BUTTON_DISTANCE;
		int useButtonWidth = (int)BUTTON_WIDTH;
		if (!landscape) {
			posY += 20;
			useButtonDistance *= 2;
			useButtonWidth *= 1.8;
		}
		
		
		result.add(createScreenButton(platform, view, posX, posY, useButtonWidth, "Start", () -> platform.setPresenter(new DemoScreenStartPresenter(platform, view))));
		posX += useButtonDistance;
		result.add(createScreenButton(platform, view, posX, posY, useButtonWidth, "Scrolling", () -> platform.setPresenter(new DemoScreenScrollingPresenter(platform, view))));
		posX += useButtonDistance;
		result.add(createScreenButton(platform, view, posX, posY, useButtonWidth, "Collisions", () -> platform.setPresenter(new DemoScreenCollisionPresenter(platform, view))));
		posX += useButtonDistance;
		result.add(createScreenButton(platform, view, posX, posY, useButtonWidth, "Input", () -> platform.setPresenter(new DemoScreenInputPresenter(platform, view))));
		posX += useButtonDistance;
		if (!landscape) {
			posX = startPosX;
			posY -= 20;
		}
		result.add(createScreenButton(platform, view, posX, posY, useButtonWidth, "Tween", () -> platform.setPresenter(new DemoScreenTweenPresenter(platform, view))));
		posX += useButtonDistance;
		result.add(createScreenButton(platform, view, posX, posY, useButtonWidth, "Panel", () -> platform.setPresenter(new DemoScreenPanelPresenter(platform, view))));
		posX += useButtonDistance;
		result.add(createScreenButton(platform, view, posX, posY, useButtonWidth, "Animation", () -> platform.setPresenter(new DemoScreenAnimationPresenter(platform, view))));
		posX += useButtonDistance;
		result.add(createScreenButton(platform, view, posX, posY, useButtonWidth, "States", () -> platform.setPresenter(new DemoScreenSavingStatePresenter(platform, view))));
		posX += useButtonDistance;

		return result;
	}
	
	public static LObject createScreenButton(LPlatform platform, LScreenView view, int posX, int posYFromBottom, int buttonWidth, String text, LAction touchAction) throws Exception {
		return createScreenButton(platform, view, posX, posYFromBottom, buttonWidth, text, platform.getCustomColor(128, 128, 160), touchAction);
	}

	public static LObject createScreenButton(LPlatform platform, LScreenView view, int posX, int posYFromBottom, int buttonWidth, String text, LColor color, LAction touchAction) throws Exception {
		LScreenLogic screenLogic = new LScreenLogic();
		LObject result = screenLogic.createObject(new LVector(buttonWidth, BUTTON_HEIGHT), new LVector(posX, view.getScreenSize().y - posYFromBottom));
		result.setBackgroundColor(color);
		double fontSize = DemoConstants.BUTTON_FONT_SIZE;
		if (!isLandscapeOrientation(view)) {
			fontSize *= 1.8;
		}
		screenLogic.setText(result, text, platform.getFont(DemoFont.PLAIN_BOLD), fontSize, platform.getCustomColor(220, 220, 255));
		screenLogic.setTextShadow(result, new LVector(0.4, 0.4), platform.getColorBlack());
		result.setTouchable(true);
		result.setTouchAction(touchAction);
		return result;
	}
	
	public static boolean isLandscapeOrientation(LScreenView view) {
		return view.getScreenSize().x > view.getScreenSize().y; 
	}

}
