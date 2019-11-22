package de.bright_side.lgf.pc.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import de.bright_side.lgf.base.LLogger;
import de.bright_side.lgf.model.LColor;
import de.bright_side.lgf.model.LFont;
import de.bright_side.lgf.model.LImage;
import de.bright_side.lgf.model.LVector;
import de.bright_side.lgf.pc.base.LPcUtil;
import de.bright_side.lgf.util.LMathsUtil;
import de.bright_side.lgf.view.LCanvas;

public class LPcCanvas implements LCanvas {
    private static final double FONT_TEXT_SIZE_FACTOR = 1.1;
	private LVector virtualSize;
    private LVector nativeCanvasSize;
	private Graphics2D graphics;
	/** contains the offset while cameraPos contains the center of what is being displayed, the scrollOffset is the offset that is added to each coordinate before being displayed.*/
	private LVector scrollOffset;
	private final Font DEFAULT_FONT = new Font("sansserif", Font.PLAIN, 24);

    public LPcCanvas(LLogger logger, LVector virtualSize, Graphics2D graphics, LVector nativeCanvasSize, LVector cameraPos) {
        this.virtualSize = virtualSize;
        this.graphics = graphics;
        this.nativeCanvasSize = nativeCanvasSize;
        
        LVector halfScreenSize = LMathsUtil.multiply(virtualSize, 0.5);
        LVector cameraTopLeft = LMathsUtil.subtract(cameraPos, halfScreenSize);
        scrollOffset = LMathsUtil.multiply(cameraTopLeft, -1);
    }

    private LVector applyCameraPos(LVector pos, boolean considerCameraPos) {
    	if (!considerCameraPos){
    		return pos;
    	}
		return LMathsUtil.add(pos,  scrollOffset);
	}

    @Override
    public void fillRect(LVector pos, LVector size, LColor color, boolean considerCameraPos) {
    	LVector usePos = applyCameraPos(pos, considerCameraPos);
    	graphics.setColor(new Color(color.getAsInt()));
    	Rectangle rect = toRectInNativeCanvas(usePos, size);
		graphics.fillRect(rect.x, rect.y, rect.width, rect.height);
    }
    
	@Override
    public void fillRectCentered(LVector pos, LVector size, LColor color, double opacity, boolean considerCameraPos) {
		if (opacity <= 0) {
			return;
		}
    	LVector usePos = applyCameraPos(pos, considerCameraPos);
        Color useColor = applyOpacity(color, opacity);
		graphics.setColor(useColor);
        LVector centeredPos = LMathsUtil.add(usePos, -size.getX() / 2, -size.getY() / 2);
        Rectangle rect = toRectInNativeCanvas(centeredPos, size);
        graphics.fillRect(rect.x, rect.y, rect.width, rect.height);
    }

	private Color applyOpacity(LColor color, double opacity) {
		Color useColor = new Color(color.getAsInt());
        if (opacity < 1) {
        	useColor = new Color(useColor.getRed(), useColor.getGreen(), useColor.getBlue(), (int)(useColor.getAlpha() * opacity));
        }
		return useColor;
	}

	@Override
	public void drawTextCentered(LVector pos, String text, double textSize, LColor color, LColor outlineColorOrNull, LFont fontOrNull
			, LVector shadowOffset, LColor shadowColor, double opacity, boolean considerCameraPos) {
    	if (opacity <= 0) {
    		return;
    	}
		
    	LVector usePos = applyCameraPos(pos, considerCameraPos);
    	
    	Font font = null;
    	if (fontOrNull != null){
    		font = (Font)fontOrNull.getFontObject();
    	} else {
    		font = DEFAULT_FONT;
    	}
    	graphics.setFont(font.deriveFont((float)(textSize * FONT_TEXT_SIZE_FACTOR)));

    	int width = graphics.getFontMetrics().stringWidth(text);
    	
        LVector topLeft = toPointInNativeCanvas(usePos);
        topLeft = LMathsUtil.add(topLeft, -width / 2, 0);
        topLeft.setY(topLeft.getY() + Math.abs(graphics.getFontMetrics().getAscent() / 2));
        
        if (shadowColor != null){
        	graphics.setColor(applyOpacity(shadowColor, opacity));
            LVector shadowOffsetInNativeCanvas = toPointInNativeCanvas(shadowOffset);
            graphics.drawString(text, (int)(topLeft.getX() + shadowOffsetInNativeCanvas.getX()), (int)(topLeft.getY() + shadowOffsetInNativeCanvas.getY()));
        }
        
        if (outlineColorOrNull != null){
        	graphics.setColor(applyOpacity(outlineColorOrNull, opacity));
            for (int textOffsetX = -1; textOffsetX <= 1; textOffsetX ++){
                for (int textOffsetY = -1; textOffsetY <= 1; textOffsetY ++){
                    if ((textOffsetX != 0) || (textOffsetY != 0)){
                        graphics.drawString(text, (int)topLeft.getX() + textOffsetX, (int)topLeft.getY() + textOffsetY);
                    }
                }
            }
        }

        if (color != null){
        	graphics.setColor(applyOpacity(color, opacity));
        } else {
        	graphics.setColor(Color.BLACK);
        }
        graphics.drawString(text, (int)topLeft.getX(), (int)topLeft.getY());
	}


    private LVector toPointInNativeCanvas(LVector point){
        LVector result = LMathsUtil.divide(point, virtualSize);
        return LMathsUtil.multiply(result, nativeCanvasSize);
    }

    private Rectangle toRectInNativeCanvas(LVector pos, LVector size){
        LVector topLeft = toPointInNativeCanvas(pos);
        LVector adjustedSize = LMathsUtil.divide(size, virtualSize);
        adjustedSize = LMathsUtil.multiply(adjustedSize, nativeCanvasSize);
        LVector bottomRight = LMathsUtil.add(topLeft, adjustedSize);
        return new Rectangle((int)topLeft.getX(), (int)topLeft.getY(), (int)bottomRight.getX() - (int)topLeft.getX(), (int)bottomRight.getY() - (int)topLeft.getY());
    }


    @Override
    public void drawImageCentered(LVector pos, LVector size, LImage image, double rotation, double opacity, boolean considerCameraPos) {
    	if (opacity <= 0) {
    		return;
    	}
    	
    	Composite originalComposite = null;
    	if (opacity < 1) {
    		originalComposite = graphics.getComposite();
    		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)opacity));
    	}
    	
    	LVector usePos = applyCameraPos(pos, considerCameraPos);
    	
        LVector useSize = toPointInNativeCanvas(size);
        LVector posInCanvas = toPointInNativeCanvas(usePos);
        
		BufferedImage bufferedImage = (BufferedImage) image.getImageObject();

		double imageOriginalWidth = bufferedImage.getWidth();
		double imageOriginalHeight = bufferedImage.getHeight();
		
		double scaleFactorX = useSize.getX() / imageOriginalWidth;
		double scaleFactorY = useSize.getY() / imageOriginalHeight;

		
		int drawLocationX = 0;
		int drawLocationY = 0;

		drawLocationX = (int)(posInCanvas.getX() - (imageOriginalWidth * scaleFactorX) / 2d);
		drawLocationY = (int)(posInCanvas.getY() - (imageOriginalHeight * scaleFactorY) / 2d);
		if (rotation == 0) {
			graphics.drawImage(bufferedImage, drawLocationX, drawLocationY, LPcUtil.cielInt(scaleFactorX * imageOriginalWidth), LPcUtil.cielInt(scaleFactorY * imageOriginalHeight), null);
			resetOpacity(originalComposite);
			return;
		}
		
		if (Math.abs(imageOriginalWidth - imageOriginalHeight) > 10){ 
			//: for some reason this code creates an offset in drawing if the image is more or less square, but works well if it is not square 
			bufferedImage = scaleImage(bufferedImage, (int)useSize.getX(), (int)useSize.getY());
			drawLocationX = (int)(posInCanvas.getX() - (bufferedImage.getWidth() * scaleFactorX) / 2d);
			drawLocationY = (int)(posInCanvas.getY() - (bufferedImage.getHeight() * scaleFactorY) / 2d);
			drawWithRotation(drawLocationX, drawLocationY, bufferedImage, rotation);
			resetOpacity(originalComposite);
			return;
		}
		

		//: this code works well if the image is square, but cuts of parts of the image if it is not
		double locationX = (imageOriginalWidth * scaleFactorX) / 2;
		double locationY = (imageOriginalHeight * scaleFactorY) / 2;
		double rotationRequired = Math.toRadians(rotation);
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		tx.concatenate(AffineTransform.getScaleInstance(scaleFactorX, scaleFactorY));
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		graphics.drawImage(op.filter(bufferedImage, null), drawLocationX, drawLocationY, null);
		resetOpacity(originalComposite);
    }

    
	private void resetOpacity(Composite originalComposite) {
		if (originalComposite != null) {
			graphics.setComposite(originalComposite);
		}
	}

	private BufferedImage scaleImage(BufferedImage original, int newWidth, int newHeight) {
		BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = resized.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(original, 0, 0, newWidth, newHeight, 0, 0, original.getWidth(), original.getHeight(), null);
		g.dispose();
		return resized;
	}

	private void drawWithRotation(int posX, int posY, BufferedImage bufferedImage, double rotation) {
		//: https://stackoverflow.com/questions/8721312/java-image-cut-off
		double locationX = bufferedImage.getWidth() / 2;
		double locationY = bufferedImage.getHeight() / 2;

		double diff = Math.abs(bufferedImage.getWidth() - bufferedImage.getHeight());

		//: to correct the set of origin point and the overflow
		double rotationRequired = Math.toRadians(rotation);
		double unitX = Math.abs(Math.cos(rotationRequired));
		double unitY = Math.abs(Math.sin(rotationRequired));

		double correctUx = unitX;
		double correctUy = unitY;

		//: if the height is greater than the width, so you have to 'change' the axis to correct the overflow
		if(bufferedImage.getWidth() < bufferedImage.getHeight()){
		    correctUx = unitY;
		    correctUy = unitX;
		}

		int posAffineTransformOpX = (int)(posX-(int)(locationX)-(int)(correctUx*diff));
		int posAffineTransformOpY = (int)(posY-(int)(locationY)-(int)(correctUy*diff));

		//: translate the image center to same diff that dislocates the origin, to correct its point set
		AffineTransform objTrans = new AffineTransform();
		objTrans.translate(correctUx*diff, correctUy*diff);
		objTrans.rotate(rotationRequired, locationX, locationY);

		AffineTransformOp op = new AffineTransformOp(objTrans, AffineTransformOp.TYPE_BILINEAR);
		
		//: Drawing the rotated image at the required drawing locations
		graphics.drawImage(op.filter(bufferedImage, null), posAffineTransformOpX, posAffineTransformOpY, null);
	}

	@SuppressWarnings("unused")
	private BufferedImage createSquaredBufferedImageToPreventRotationCutOff(BufferedImage originalImage, double originalWidth, double originalHeight) {
		if (originalWidth == originalHeight) {
			return originalImage;
		}
		
		int size = (int)Math.max(originalWidth, originalHeight);
		
		BufferedImage result = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D)result.getGraphics();
		g.setColor(Color.RED);
		result.getGraphics().fillRect(0, 0, size, size);
		
		return result;
	}



}
