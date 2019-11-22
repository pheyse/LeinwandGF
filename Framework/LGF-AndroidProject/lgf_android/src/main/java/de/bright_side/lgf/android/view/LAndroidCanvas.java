package de.bright_side.lgf.android.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.TypedValue;

import de.bright_side.lgf.android.base.LAndroidUtil;
import de.bright_side.lgf.base.LLogger;
import de.bright_side.lgf.model.LColor;
import de.bright_side.lgf.model.LFont;
import de.bright_side.lgf.model.LImage;
import de.bright_side.lgf.model.LVector;
import de.bright_side.lgf.util.LMathsUtil;
import de.bright_side.lgf.view.LCanvas;

public class LAndroidCanvas implements LCanvas {
    private static final double MAX_COLOR_CHANNEL = 255;
    private final LVector scrollOffset;
    private LVector virtualSize;
    private LVector cameraPos;
    private Canvas nativeCanvas = null;
    private LLogger logger = null;
    private Resources resources;
    private final Paint paint = new Paint();
    private LVector nativeCanvasSize;

    public LAndroidCanvas(LLogger logger, LVector virtualSize, LVector cameraPos, Canvas androidCanvas, Resources resources) {
        this.virtualSize = virtualSize;
        this.cameraPos = cameraPos;
        this.nativeCanvas = androidCanvas;
        LVector halfScreenSize = LMathsUtil.multiply(virtualSize, 0.5);
        LVector cameraTopLeft = LMathsUtil.subtract(cameraPos, halfScreenSize);
        scrollOffset = LMathsUtil.multiply(cameraTopLeft, -1);

        this.logger = logger;
        this.resources = resources;
        nativeCanvasSize = new LVector(androidCanvas.getWidth(), androidCanvas.getHeight());
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
        paint.setColor(color.getAsInt());
        paint.setStyle(Paint.Style.FILL);
        nativeCanvas.drawRect(toRectInNativeCanvas(usePos, size), paint);
    }

    @Override
    public void fillRectCentered(LVector pos, LVector size, LColor color, double opacity, boolean considerCameraPos) {
        if (opacity <= 0){
            return;
        }
        int useColor = applyOpacity(color, opacity);
        LVector usePos = applyCameraPos(pos, considerCameraPos);
        paint.setColor(useColor);
        paint.setStyle(Paint.Style.FILL);
        LVector centeredPos = LMathsUtil.add(usePos, -size.x / 2, -size.y / 2);
        nativeCanvas.drawRect(toRectInNativeCanvas(centeredPos, size), paint);
    }

    private int applyOpacity(LColor color, double opacity) {
        int useColor = color.getAsInt();
        if (opacity < 1){
            useColor = Color.argb((int)(Color.alpha(useColor) * opacity), Color.red(useColor), Color.green(useColor), Color.blue(useColor));
        }
        return useColor;
    }

    @Override
    public void drawTextCentered(LVector pos, String text, double textSize, LColor color, LColor outlineColorOrNull,
                                 LFont fontOrNull, LVector shadowOffset, LColor shadowColor, double opacity, boolean considerCameraPos) {
        if (opacity <= 0){
            return;
        }
        LVector usePos = applyCameraPos(pos, considerCameraPos);
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float)textSize, resources.getDisplayMetrics()));

        if (fontOrNull != null){
            paint.setTypeface((Typeface)fontOrNull.getFontObject());
        } else {
            paint.setTypeface(Typeface.DEFAULT);
        }

        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        int top = Math.abs(paint.getFontMetricsInt().top);

        LVector topLeft = toPointInNativeCanvas(usePos);
        topLeft = LMathsUtil.add(topLeft, -bounds.exactCenterX(), (top / 2) - (paint.descent() / 2));

        int x = (int)(topLeft.x);
        int y = (int)(topLeft.y);

        if ((shadowColor != null) && (shadowColor.getAsInt() != Color.TRANSPARENT)){
            paint.setColor(applyOpacity(shadowColor, opacity));
            LVector shadowOffsetInNativeCanvas = toPointInNativeCanvas(shadowOffset);
            nativeCanvas.drawText(text, (int)(x + shadowOffsetInNativeCanvas.x), (int)(y + shadowOffsetInNativeCanvas.y), paint);
        }

        if ((outlineColorOrNull != null) && (outlineColorOrNull.getAsInt() != Color.TRANSPARENT)){
            paint.setColor(applyOpacity(outlineColorOrNull, opacity));
            for (int textOffsetX = -1; textOffsetX <= 1; textOffsetX ++){
                for (int textOffsetY = -1; textOffsetY <= 1; textOffsetY ++){
                    if ((textOffsetX != 0) || (textOffsetY != 0)){
                        nativeCanvas.drawText(text, x + textOffsetX, y + textOffsetY, paint);
                    }
                }
            }
        }

        if (color != null){
            paint.setColor(applyOpacity(color, opacity));
        } else {
            paint.setColor(Color.BLACK);
        }
        nativeCanvas.drawText(text, x, y, paint);
    }

    private LVector toPointInNativeCanvas(LVector point){
        return new LVector(point.x / virtualSize.x * nativeCanvasSize.x, point.y / virtualSize.y * nativeCanvasSize.y);
    }

    private LVector applyCameraPosAndToPointInNativeCanvas(LVector point, boolean considerCameraPos){
        if (!considerCameraPos){
            return new LVector(point.x / virtualSize.x * nativeCanvasSize.x, point.y / virtualSize.y * nativeCanvasSize.y);
        }
        return new LVector((point.x + scrollOffset.x) / virtualSize.x * nativeCanvasSize.x
                            , (point.y + scrollOffset.y) / virtualSize.y * nativeCanvasSize.y);
    }

    private Rect toRectInNativeCanvas(LVector pos, LVector size){
        LVector topLeft = toPointInNativeCanvas(pos);
        LVector adjustedSize = LMathsUtil.divide(size, virtualSize);
        adjustedSize = LMathsUtil.multiply(adjustedSize, nativeCanvasSize);
        LVector bottomRight = LMathsUtil.add(topLeft, adjustedSize);
        return new Rect((int)topLeft.x, (int)topLeft.y, (int)bottomRight.x, (int)bottomRight.y);
    }


    final static boolean optimize = true;

    @Override
    public void drawImageCentered(LVector pos, LVector size, LImage image, double rotation, double opacity, boolean considerCameraPos) {
        if (opacity <= 0){
            return;
        }
        LVector posInCanvas = applyCameraPosAndToPointInNativeCanvas(pos, considerCameraPos);
        LVector useSize = toPointInNativeCanvas(size);
        if (optimize){
            if (offScreen(posInCanvas, useSize, rotation)){
                return;
            }
        }

        Bitmap bitmap = (Bitmap) image.getImageObject();

        double newWidth = useSize.x;
        double newHeight = useSize.y;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Paint opacityPaint = null;
        if (opacity < 1){
            opacityPaint = new Paint();
            opacityPaint.setAlpha((int)(MAX_COLOR_CHANNEL * opacity));
        }
        if (rotation != 0){
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            matrix.postTranslate(-width * scaleWidth / 2, -height * scaleHeight / 2);
            matrix.postRotate((int)rotation);
            matrix.postTranslate((int)posInCanvas.x, (int)posInCanvas.y);
            nativeCanvas.drawBitmap(bitmap, matrix, opacityPaint);
            matrix.reset();
        } else {
            LVector sizeInCanvas = toPointInNativeCanvas(size);
            LVector halfSizeInCanvas = LMathsUtil.multiply(sizeInCanvas, 0.5);
            LVector topLeftInCanvas = LMathsUtil.subtract(posInCanvas, halfSizeInCanvas);
            LVector bottomRightInCanvas = LMathsUtil.add(posInCanvas, halfSizeInCanvas);
            Rect destRect = new Rect((int)topLeftInCanvas.x, (int)topLeftInCanvas.y, (int)bottomRightInCanvas.x, (int)bottomRightInCanvas.y);
            if (image.isHasAlpha()){
                nativeCanvas.drawBitmap(bitmap, null, destRect, opacityPaint);
            } else {
                if (image.getPreRenderedObject() == null){
                    image.setPreRenderedObject(createPreRenderedImage(image, sizeInCanvas));
                }
                nativeCanvas.drawBitmap((Bitmap)image.getPreRenderedObject(), null, destRect, opacityPaint);
            }
        }
    }

    private boolean offScreen(LVector posInCanvas, LVector size, double rotation) {
        if (rotation != 0){
            return false;
        }

        if (  (posInCanvas.x + (size.x / 2) < 0)
            ||(posInCanvas.y + (size.y / 2) < 0)
            ||(posInCanvas.x - (size.x / 2) > nativeCanvasSize.x)
            ||(posInCanvas.y - (size.y / 2) > nativeCanvasSize.y)
                ){
            return true;
        }

        return false;
    }

    private Bitmap createPreRenderedImage(LImage image, LVector sizeInCanvas){
        Bitmap bitmap = (Bitmap) image.getImageObject();
        Bitmap result = Bitmap.createScaledBitmap(bitmap, LAndroidUtil.cielInt(sizeInCanvas.x), LAndroidUtil.cielInt(sizeInCanvas.y), true);
        result = convert(result, Bitmap.Config.ARGB_8888);
        return result;
    }

    private Bitmap convert(Bitmap bitmap, Bitmap.Config config) {
        Bitmap convertedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), config);
        Canvas canvas = new Canvas(convertedBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return convertedBitmap;
    }

}
