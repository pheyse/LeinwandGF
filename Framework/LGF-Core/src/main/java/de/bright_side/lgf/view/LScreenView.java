package de.bright_side.lgf.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.bright_side.lgf.base.LLogger;
import de.bright_side.lgf.base.LPlatform;
import de.bright_side.lgf.logic.LScreenLogic;
import de.bright_side.lgf.model.LObject;
import de.bright_side.lgf.model.LScreenModel;
import de.bright_side.lgf.model.LVector;
import de.bright_side.lgf.util.LMathsUtil;

public class LScreenView {
    private final LScreenLogic logic;
    private final LVector VECTOR_ONE = new LVector(1, 1);
    private final LVector VECTOR_ZERO = new LVector(0, 0);
    private final LLogger logger;
    private LScreenModel model;
    private LVector screenSize;
	private LVector cameraPos;
	private double fontScaleFactor;
	private Map<LObject, PosAndSize> panelObjectPosConsiderCamera = new HashMap<LObject, PosAndSize>();
	private Map<LObject, PosAndSize> panelObjectPosIgnoreCamera = new HashMap<LObject, PosAndSize>();
	private Map<LObject, PosAndSize> panelObjectPosConsiderCameraInProgress;
	private Map<LObject, PosAndSize> panelObjectPosIgnoreCameraInProgress;
	
	private class PanelDrawParams{
		public LVector offset;
		public LVector sizeFactor;
		public double fontSizeFactor;
		public double opacityFactor;
		@Override
		public String toString() {
			return "PanelDrawParams [offset=" + offset + ", sizeFactor=" + sizeFactor + ", fontSizeFactor="
					+ fontSizeFactor + ", opacityFactor=" + opacityFactor + "]";
		}
	}
	
	private class PosAndSize{
		public LVector pos;
		public LVector size;
	}


    public LScreenView(LPlatform platform, LVector screenSize, double fontScaleFactor) {
        this.screenSize = screenSize;
		this.fontScaleFactor = fontScaleFactor;
        logger = platform.getLogger();
        cameraPos = LMathsUtil.multiply(screenSize, 0.5);
        logic = new LScreenLogic();
    }

    public void draw(LCanvas canvas){
    	boolean log = false;
    	
    	panelObjectPosConsiderCameraInProgress = new HashMap<LObject, PosAndSize>();
    	panelObjectPosIgnoreCameraInProgress = new HashMap<LObject, PosAndSize>();
        
        if (model.getBackgroundColor() != null){
            canvas.fillRect(new LVector(0, 0), screenSize, model.getBackgroundColor(), false);
        }

        for (LObject i: logic.getAllObjects(model, false)){
            drawWithPanelItems(canvas, i, true, log, null);
        }
        if (model.getUiObjects() != null){
        	for (LObject i: model.getUiObjects()){
        		if (log){
        			logger.debug("drawing ui object: " + i);
        		}
        		drawWithPanelItems(canvas, i, false, log, null);
        	}
        }
        
        panelObjectPosConsiderCamera = panelObjectPosConsiderCameraInProgress;
    	panelObjectPosIgnoreCamera = panelObjectPosIgnoreCameraInProgress;
    }
    
    private void drawWithPanelItems(LCanvas canvas, LObject object, boolean considerCameraPos, boolean log, PanelDrawParams drawParams) {
    	draw(canvas, object, considerCameraPos, log, drawParams);
		if (object.getPanelObjects() == null) {
			return;
		}
		
		PanelDrawParams parentDrawParams = drawParams;
		if (parentDrawParams == null) {
			parentDrawParams = new PanelDrawParams();
			parentDrawParams.sizeFactor = VECTOR_ONE;
			parentDrawParams.offset = VECTOR_ZERO;
			parentDrawParams.opacityFactor = 1;
			parentDrawParams.fontSizeFactor = 1;
		}
		PanelDrawParams childDrawParams = new PanelDrawParams();
		
		LVector panelSize = object.getPanelSize();
		if (panelSize == null) {
			panelSize = object.getSize();
			childDrawParams.sizeFactor = parentDrawParams.sizeFactor;
		} else {
			childDrawParams.sizeFactor = new LVector(parentDrawParams.sizeFactor.x * object.getSize().x / panelSize.x, parentDrawParams.sizeFactor.y * object.getSize().y / panelSize.y);
		}
		childDrawParams.fontSizeFactor = Math.min(childDrawParams.sizeFactor.x, childDrawParams.sizeFactor.y);
		childDrawParams.offset = new LVector(parentDrawParams.offset.x + object.getPos().x - (object.getSize().x / 2), parentDrawParams.offset.y + object.getPos().y - (object.getSize().y / 2));
		childDrawParams.opacityFactor = parentDrawParams.opacityFactor * object.getOpacity();
		
		for (LObject i: object.getPanelObjects()) {
			drawWithPanelItems(canvas, i, false, log, childDrawParams);
		}
    }
    
	private void draw(LCanvas canvas, LObject object, boolean considerCameraPos, boolean log, PanelDrawParams panelDrawParam) {
        if (!object.isVisible()){
            return;
        }
        
        if ((object.getBlinkingInterval() != null) && object.getBlinkingInterval() != 0){
            long interval = object.getBlinkingInterval();
            if (System.currentTimeMillis() % (interval * 2) > interval){
                return;
            }
        }

        LVector usePos = null;
        LVector useSize = null;
        double useOpacity = 0;
        double useFontSizeFactor = 0;
        
        if (panelDrawParam == null) {
        	//: not inside a panel: just use object values w/ adjustment
            usePos = object.getPos();
            useSize = object.getSize();
            useOpacity = object.getOpacity();
            useFontSizeFactor = 1;
        } else {
            usePos = new LVector(panelDrawParam.offset.x + (object.getPos().x * panelDrawParam.sizeFactor.x), panelDrawParam.offset.y + (object.getPos().y * panelDrawParam.sizeFactor.y));
            useSize = new LVector(object.getSize().x * panelDrawParam.sizeFactor.x, object.getSize().y * panelDrawParam.sizeFactor.y);
            useOpacity = panelDrawParam.opacityFactor * object.getOpacity();
            useFontSizeFactor = panelDrawParam.fontSizeFactor;
        }

        if (object.getBackgroundColor() != null){
            canvas.fillRectCentered(usePos, useSize, object.getBackgroundColor(), useOpacity, considerCameraPos);
        }
        if (object.getImage() != null){
            canvas.drawImageCentered(usePos, useSize, object.getImage(), object.getRotation(), useOpacity, considerCameraPos);
        }
        if ((object.getText() != null) && (!object.getText().isEmpty())){
            canvas.drawTextCentered(usePos, object.getText(), object.getTextSize() / fontScaleFactor / 2 * useFontSizeFactor, object.getTextColor()
                    , object.getTextOutlineColor(), object.getTextFont(), object.getTextShadowOffset(), object.getTextShadowColor(), useOpacity, considerCameraPos);
        }
        
        if ((object.isTouchable()) && (object.isVisible())){
        	PosAndSize posAndSize = new PosAndSize();
        	posAndSize.pos = usePos;
        	posAndSize.size = useSize;
        	if (considerCameraPos) {
        		panelObjectPosConsiderCameraInProgress.put(object, posAndSize);
        	} else {
        		panelObjectPosIgnoreCameraInProgress.put(object, posAndSize);
        	}
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
		for (Entry<LObject, PosAndSize> i: panelObjectPosConsiderCamera.entrySet()){
			PosAndSize posAndSize = i.getValue(); 
    		if (LMathsUtil.isInCenteredArea(pos, posAndSize.pos, posAndSize.size)){
    			result.add(i.getKey());
    		}
        }
		for (Entry<LObject, PosAndSize> i: panelObjectPosIgnoreCamera.entrySet()){
			PosAndSize posAndSize = i.getValue(); 
			if (LMathsUtil.isInCenteredArea(posIgnoreCamera, posAndSize.pos, posAndSize.size)){
				result.add(i.getKey());
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
