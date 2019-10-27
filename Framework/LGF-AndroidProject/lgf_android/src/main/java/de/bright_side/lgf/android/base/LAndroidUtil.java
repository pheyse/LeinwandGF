package de.bright_side.lgf.android.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

import de.bright_side.lgf.android.view.LAndroidComponent;
import de.bright_side.lgf.base.LConstants;
import de.bright_side.lgf.model.LImage;
import de.bright_side.lgf.model.LVector;
import de.bright_side.lgf.util.LMathsUtil;
import de.bright_side.lgf.view.LScreenView;

public class LAndroidUtil {
    public static String readRawResourceWithIdAsUTF8String(Resources resources, int resourceID) throws Exception {
        InputStream inputStream = null;
        try{
            inputStream = resources.openRawResource(resourceID);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final int BUFFER = 2048;
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = inputStream.read(data, 0, BUFFER)) != -1) {
                outputStream.write(data, 0, count);
            }
            return new String(outputStream.toByteArray(), "UTF-8");
        } catch (Exception e){
            throw new Exception("Could not open resource with id " + resourceID, e);
        } finally {
            if (inputStream != null) inputStream.close();
        }
    }

    public static LImage readImage(Resources resources, int resourceID, String id, double rotation) throws Exception {
        InputStream inputStream = null;
        try{
            inputStream = resources.openRawResource(resourceID);
            Bitmap image = BitmapFactory.decodeStream(inputStream);
            if (rotation != 0){
                image = rotate(image, rotation);
            }
            LImage result = new LImage();
            result.setSize(new LVector(image.getWidth(), image.getHeight()));
            result.setImageObject(image);
            result.setId(id);
            result.setHasAlpha(image.hasAlpha());
            return result;
        } catch (Exception e){
            throw e;
        } finally {
            if (inputStream != null) inputStream.close();
        }
    }

    private static Bitmap rotate(Bitmap bitmapOrg, double rotation) {
        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();
        Matrix matrix = new Matrix();
        matrix.postRotate((int)rotation);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapOrg, width, height, true);
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        return rotatedBitmap;
    }

    public static void logAndGenerateLocation(String tag, LConstants.LogLevel logLevel, String message){
        StackTraceElement[] ste = new Exception().getStackTrace();
        if (ste.length < 3){
            log(tag, logLevel, "?", "?", 0, message);
        }
        else {
            StackTraceElement e = ste[2];
            if (e != null){
                String fileName = e.getFileName();
                String useFileName = null;
                if (fileName == null){
                    useFileName = "" + ste[2];
                    int lastDotIndex = useFileName.lastIndexOf(".");
                    if (lastDotIndex >= 0) useFileName = useFileName.substring(0, lastDotIndex);
                } else if (fileName.length() > 5){
                    useFileName = fileName.substring(0, fileName.length() - 5);
                } else useFileName = fileName;
                log(tag, logLevel, useFileName, e.getMethodName(), e.getLineNumber(), message);
            } else {
                log(tag, logLevel, "???", "???", -1, message);
            }
        }
    }

    private static void log(String tag, LConstants.LogLevel logLevel, String fileName, String methodName, int lineNumber, String message) {
        Log.d(tag, logLevel + ": " + fileName + "." + methodName + ":" + lineNumber + ">" + message);
    }

    public static File getPublicApplicationDirOnSDRoot(Context context, String buildConfigFlavor) throws Exception {
        File rootDir = Environment.getExternalStorageDirectory();
        if (!rootDir.exists()) throw new Exception("Root directory >>" + rootDir + "<< does not exist!");
        String flavorInfo = "";
        if (!buildConfigFlavor.isEmpty()){
            flavorInfo = "_" + buildConfigFlavor;
        }
        String typeInfo = "";
        if (!"release".equals(context)){
            typeInfo = "_" + buildConfigFlavor;
        }
        File dir = new File(rootDir, context.getPackageName() + flavorInfo + typeInfo);
        if (!dir.exists()) dir.mkdirs();
        if (!dir.exists()) throw new Exception("Directory >>" + dir + "<< does not exist and could not be created");
        return dir;
    }

    public static String readEnvironmentInfoString(Activity activity, String buildConfigBuildType, String buildConfigFlavor){
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM_dd  HH:mm");
            Display display = activity.getWindow().getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            PackageInfo packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            String text = "System:";
            text += "\n   OS version: " + android.os.Build.VERSION.RELEASE + ", API Level:" + android.os.Build.VERSION.SDK_INT;
            text += "\n   Device: " + android.os.Build.MANUFACTURER + " '" + android.os.Build.MODEL + "' (" + android.os.Build.BRAND + ")";
            text += "\n   Language/Country: " + Locale.getDefault().getDisplayLanguage() + "/" + activity.getResources().getConfiguration().locale.getDisplayCountry();
            text += "\n   Screen: " + activity.getWindow().getWindowManager().getDefaultDisplay().getWidth() + "x"
                    + activity.getWindow().getWindowManager().getDefaultDisplay().getHeight();
            text += "\n   Hardware Keyboard: " + (activity.getResources().getConfiguration().keyboard != Configuration.KEYBOARD_NOKEYS);
            text += "\n   App version: " + buildConfigBuildType  + ", " + buildConfigFlavor + " v" + packageInfo.versionName + " (" + packageInfo.versionCode + ")";
            text += "\n   Install/Update: " + timeFormat.format(packageInfo.firstInstallTime) + " / " + timeFormat.format(packageInfo.lastUpdateTime);
            return text;
        } catch (Exception e){
            return "Could not read environment info: " + e;
        }
    }

    public static int cielInt(double value){
        double intValue = Double.valueOf(value);
        if (value > intValue) {
            return ((int)intValue) + 1;
        } else {
            return (int)intValue;
        }
    }

    public static LVector applyCameraPos(LVector pos, LVector virtualSize, LScreenView BGFView) {
        LVector halfScreenSize = LMathsUtil.multiply(virtualSize, 0.5);
        LVector cameraTopLeft = LMathsUtil.subtract(BGFView.getCameraPos(), halfScreenSize);
        LVector scrollOffset = LMathsUtil.multiply(cameraTopLeft, -1.0);
        return LMathsUtil.subtract(pos, scrollOffset);
    }

    public static void enterFullScreenMode(LAndroidComponent gameViewComponent) {
        if (gameViewComponent == null){
            throw new RuntimeException("gameViewComponent is null");
        }
        gameViewComponent.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
    }




}
