package de.bright_side.lgf.view;

import java.util.ArrayList;
import java.util.List;

import de.bright_side.lgf.base.LLogger;
import de.bright_side.lgf.base.LPlatform;
import de.bright_side.lgf.logic.LScreenLogic;
import de.bright_side.lgf.model.LObject;
import de.bright_side.lgf.model.LScreenModel;
import de.bright_side.lgf.model.LVector;
import de.bright_side.lgf.util.LMathsUtil;

public class LScreenView {
    private final LScreenLogic logic;
    private final LLogger logger;
    private LScreenModel model;
    private LVector screenSize;
	private LVector cameraPos;
	private double fontScaleFactor;

    public LScreenView(LPlatform platform, LVector screenSize, double fontScaleFactor) {
        this.screenSize = screenSize;
		this.fontScaleFactor = fontScaleFactor;
        logger = platform.getLogger();
        cameraPos = LMathsUtil.multiply(screenSize, 0.5);
        logic = new LScreenLogic();
    }

    public void draw(LCanvas canvas){
    	boolean log = false;
        
        if (model.getBackgroundColor() != null){
            canvas.fillRect(new LVector(0, 0), screenSize, model.getBackgroundColor(), false);
        }

        for (LObject i: logic.getAllObjects(model, false)){
            draw(canvas, i, true, log);
        }
        if (model.getUiObjects() != null){
        	for (LObject i: model.getUiObjects()){
        		if (log){
        			logger.debug("drawing ui object: " + i);
        		}
        		draw(canvas, i, false, log);
        	}
        }
    }

    private void draw(LCanvas canvas, LObject object, boolean considerCameraPos, boolean log) {
        if (!object.isVisible()){
            return;
        }

        if ((object.getBlinkingInterval() != null) && object.getBlinkingInterval() != 0){
            long interval = object.getBlinkingInterval();
            if (System.currentTimeMillis() % (interval * 2) > interval){
                return;
            }
        }

        if (object.getBackgroundColor() != null){
            canvas.fillRectCentered(object.getPos(), object.getSize(), object.getBackgroundColor(), considerCameraPos);
        }
        if (object.getImage() != null){
            canvas.drawImageCentered(object.getPos(), object.getSize(), object.getImage(), object.getRotation(), considerCameraPos);
        }
        if ((object.getText() != null) && (!object.getText().isEmpty())){
            canvas.drawTextCentered(object.getPos(), object.getText(), object.getTextSize() / fontScaleFactor / 2, object.getTextColor()
                    , object.getTextOutlineColor(), object.getTextFont(), object.getTextShadowOffset(), object.getTextShadowColor(), considerCameraPos);
        }
    }

    public void setModel(LScreenModel model) {
        this.model = model;
    }

    public List<LObject> getTouchedObjects(LVector pos, LVector posIgnoreCamera) {
        List<LObject> result = new ArrayList<LObject>();
        for (LObject i: logic.getAllObjects(model, false)){
            if ((i.isVisible()) && (i.isTouchable())){
                if (LMathsUtil.isInCenteredArea(pos, i.getPos(), i.getSize())){
                    result.add(i);
                }
            }
        }
        if (model.getUiObjects() != null){
            for (LObject i: model.getUiObjects()){
                if ((i.isVisible()) && (i.isTouchable())){
                    if (LMathsUtil.isInCenteredArea(posIgnoreCamera, i.getPos(), i.getSize())){
                    	result.add(i);
                    }
                }
            }
        }
        return result;
    }


    public LVector getCameraPos() {
		return cameraPos;
	}
    
    public void setCameraPos(LVector cameraPos) {
		this.cameraPos = cameraPos;
	}

    public LVector getScreenSize() {
        return screenSize;
    }

    public void cameraPosToCenter() {
        cameraPos = LMathsUtil.divide(screenSize, 2);
    }
    
}
