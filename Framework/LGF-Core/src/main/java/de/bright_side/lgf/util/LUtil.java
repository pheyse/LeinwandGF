package de.bright_side.lgf.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.bright_side.lgf.model.LAnimationFrame;
import de.bright_side.lgf.model.LFont;
import de.bright_side.lgf.model.LImage;
import de.bright_side.lgf.model.LObject;

public class LUtil {
    public static void callTouchedActions(List<LObject> touchedObjects, LErrorListener errorListener) {
        if (touchedObjects == null){
            return;
        }
        for (LObject i: touchedObjects){
            if (i.getTouchAction() != null){
            	try {
            		i.getTouchAction().onAction();	
            	} catch (Throwable t) {
            		if (errorListener != null) {
            			errorListener.onError(t);
            		}
            	}
            }
        }
    }

	@SuppressWarnings("unused")
	private static void log(String message) {
		System.out.println("LUtil> " + message);
	}

    public static List<LObject> copyWithoutResourceReferences(List<LObject> objects){
        List<LObject> result = new ArrayList<>();
        for (LObject i: objects){
            result.add(copyWithoutResourceReferences(i));
        }
        return result;
    }

    public static void setResourceReferences(List<LObject> objects, Map<String, LImage> images, Map<String, LFont> fonts) throws Exception{
        for (LObject i: objects){
            setResourceReferences(i, images, fonts);
        }
    }

    private static List<LAnimationFrame> copyGameAnimationListWithoutResourceReferences(List<LAnimationFrame> animationFrames) {
    	if (animationFrames == null) {
    		return null;
    	}
		List<LAnimationFrame> result = new ArrayList<>();
		for (LAnimationFrame i: animationFrames) {
			result.add(new LAnimationFrame(copyWithoutResourceReferences(i.getImage()), i.getDurationInSeconds()));
		}
		return result;
	}

	private static LFont copyWithoutResourceReferences(LFont textFont) {
		if (textFont == null) {
			return null;
		}
		LFont result = new LFont();
		result.setId(textFont.getId());
		return result;
	}

	private static LImage copyWithoutResourceReferences(LImage image) {
		if (image == null) {
			return null;
		}
    	LImage result = new LImage();
    	result.setId(image.getId());
    	result.setHasAlpha(image.isHasAlpha());
		return result;
	}

    public static LObject copyWithoutResourceReferences(LObject object){
    	LObject result = new LObject();
    	result.setType(object.getType());
    	result.setId(object.getId());
    	result.setPos(object.getPos());
    	result.setImage(copyWithoutResourceReferences(object.getImage()));
    	result.setBackgroundColor(object.getBackgroundColor());
    	result.setSize(object.getSize());
    	result.setText(object.getText());
    	result.setSpeed(object.getSpeed());
    	result.setRotation(object.getRotation());
    	result.setCollisionPolygon(object.getCollisionPolygon());
    	result.setVisible(object.isVisible());
    	result.setBlinkingInterval(object.getBlinkingInterval());
    	result.setTextFont(copyWithoutResourceReferences(object.getTextFont()));
    	result.setTextColor(object.getTextColor());
    	result.setTextOutlineColor(object.getTextOutlineColor());
    	result.setTextShadowColor(object.getTextShadowColor());
    	result.setTextShadowOffset(object.getTextShadowOffset());
    	result.setTextSize(object.getTextSize());
    	result.setPath(object.getPath());
    	result.setNextPathItem(object.getNextPathItem());
    	result.setMaximumRotationPerSecond(object.getMaximumRotationPerSecond());
    	result.setAnimationFrames(copyGameAnimationListWithoutResourceReferences(object.getAnimationFrames()));
    	result.setAnimationFrameIndex(object.getAnimationFrameIndex());
    	result.setRemainingSecondsInAnimationFrame(object.getRemainingSecondsInAnimationFrame());
    	result.setTouchable(object.isTouchable());
    	result.setTouchAction(object.getTouchAction());
    	return result;
    }
	
	public static void setResourceReferences(LObject object, Map<String, LImage> images, Map<String, LFont> fonts) throws Exception{
		if (object.getImage() != null) {
			LImage imageObject = images.get(object.getImage().getId());
			if (imageObject == null) {
				throw new Exception("Unkonwn image with id '" + object.getImage().getId() + "'. Known ids: " + images.keySet());
			}
			object.getImage().setImageObject(imageObject.getImageObject());
		}
		if (object.getTextFont() != null) {
			LFont fontObject = fonts.get(object.getTextFont().getId());
			if (fontObject == null) {
				throw new Exception("Unkonwn font with id '" + object.getTextFont().getId());
			}
			object.getTextFont().setFontObject(fontObject.getFontObject());
		}

		if (object.getAnimationFrames() != null) {
			for (LAnimationFrame frame: object.getAnimationFrames()) {
				if (frame != null) {
					LImage image = frame.getImage();
					if (image != null) {
						LImage imageObject = images.get(image.getId());
						if (imageObject == null) {
							throw new Exception("Unkonwn image with id '" + image.getId());
						}
						image.setImageObject(imageObject.getImageObject());
					}
				}
			}
		}
    }


}
