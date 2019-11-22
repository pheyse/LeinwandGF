package de.bright_side.lgf.android.base;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;


import androidx.core.content.ContextCompat;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import de.bright_side.filesystemfacade.facade.FSFFile;
import de.bright_side.filesystemfacade.facade.FSFSystem;
import de.bright_side.lgf.android.view.LActivity;
import de.bright_side.lgf.base.LLogger;
import de.bright_side.lgf.base.LPlatform;
import de.bright_side.lgf.base.LTextInputResultListener;
import de.bright_side.lgf.model.LAnimationFrame;
import de.bright_side.lgf.model.LAnimationResource;
import de.bright_side.lgf.model.LColor;
import de.bright_side.lgf.model.LFont;
import de.bright_side.lgf.model.LFontResource;
import de.bright_side.lgf.model.LImage;
import de.bright_side.lgf.model.LImageResource;
import de.bright_side.lgf.model.LRawResource;
import de.bright_side.lgf.model.LVector;
import de.bright_side.lgf.presenter.LScreenPresenter;

public abstract class LAndroidPlatform implements LPlatform, LLogger {
    private final LAndroidResourceAccess resourceAccess;
    private LActivity lActivity;
    private FSFSystem fsfSystem;

    public LAndroidPlatform(LActivity lActivity, FSFSystem fsfSystem, Resources resources){
        this.lActivity = lActivity;
        this.fsfSystem = fsfSystem;
        this.resourceAccess = createResourceAccess(resources, lActivity);
    }

    public abstract File getPublicApplicationDir(Context context) throws Exception;
    public abstract LAndroidResourceAccess createResourceAccess(Resources resources, LActivity lActivity);


    public FSFSystem getFsfSystem(){
        return fsfSystem;
    }

    @Override
    public LColor getColorRed() {
        return new LColor(Color.RED);
    }

    @Override
    public LColor getColorYellow() {
        return new LColor(Color.YELLOW);
    }

    @Override
    public LColor getColorBlue() {
        return new LColor(Color.BLUE);
    }

    @Override
    public LColor getColorGreen() {
        return new LColor(Color.GREEN);
    }

    @Override
    public LColor getColorGray() {
        return new LColor(Color.GRAY);
    }

    @Override
    public LColor getColorBlack() {
        return new LColor(Color.BLACK);
    }

    @Override
    public LColor getColorWhite()  {
        return new LColor(Color.WHITE);
    }

    @Override
    public LColor getCustomColor(int red, int green, int blue) {
        return new LColor(Color.argb(255, red, green, blue));
    }

    @Override
    public LColor getCustomColor(int alpha, int red, int green, int blue) {
        return new LColor(Color.argb(alpha, red, green, blue));
    }

    @Override
    public LImage createImageFromColorMap(Map<LVector, LColor> colorsMap, LVector size, LColor defaultColor) {
        Bitmap bitmap = Bitmap.createBitmap((int)size.getX(), (int)size.getY(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(defaultColor.getAsInt());
        canvas.drawRect(0f, 0f, (float)size.getX(), (float)size.getY(), paint);
        for (Map.Entry<LVector, LColor> i: colorsMap.entrySet()){
            paint.setColor(i.getValue().getAsInt());
            LVector pos = i.getKey();
            canvas.drawRect((int)pos.getX(), (int)pos.getY(), (int)pos.getX() + 1, (int)pos.getY() + 1, paint);
        }

        LImage result = new LImage();
        result.setHasAlpha(false);
        result.setSize(size);
        result.setId("colorMapImage");
        result.setImageObject(bitmap);
        return result;
    }

    public void getTextInput(final String title, final String message, final String defaultValue, final LTextInputResultListener listener) {
        lActivity.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alert = new AlertDialog.Builder(lActivity.getContext());
                alert.setTitle(title);
                alert.setMessage(message);
                final EditText input = new EditText(lActivity.getContext());
                alert.setView(input);
                input.setText(defaultValue == null ? "" : defaultValue);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        listener.onTextInputResult(input.getText().toString());
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        listener.onTextInputResult(null);
                    }
                });
                alert.show();
            }
        });
    }

    @Override
    public void showMessageDialog(final String title, final String message) {
        lActivity.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alert = new AlertDialog.Builder(lActivity.getContext());
                alert.setTitle(title);
                alert.setMessage(message);
                alert.setPositiveButton("Ok", null);
                alert.show();
            }
        });
    }

    public FSFFile getFile(StorageType directoryType, String relativePath, boolean createParentDir) throws Exception {
        File dataDir = null;
        if (directoryType == StorageType.INTERNAL){
            dataDir = lActivity.getContext().getFilesDir();
        } else if (directoryType == StorageType.EXTERNAL){
            dataDir = getPublicApplicationDir(lActivity.getContext());
        }
        String useRelativePath = relativePath.replace("\\", "/");
        if (useRelativePath.startsWith("/")){
            useRelativePath = useRelativePath.substring(1);
        }
        FSFFile file = fsfSystem.createByPath(dataDir.getAbsolutePath() + "/" + useRelativePath);
        if (createParentDir){
            file.getParentFile().mkdirs();
        }
        return file;
    }

    @Override
    public <K> K readObjectOrNull(StorageType directoryType, String relativePath, Class<K> classType) throws Exception {
        FSFFile file = getFile(directoryType, relativePath, true);
        if (!file.exists()){
            return null;
        }
        return file.readObject(classType);
    }

    @Override
    public <K> void writeObject(StorageType directoryType, String relativePath, K object) throws Exception {
        getFile(directoryType, relativePath, true).writeObject(object);
    }

    @Override
    public void writeStringToFile(StorageType directoryType, String relativePath, String string) throws Exception {
        try (BufferedOutputStream out = new BufferedOutputStream(getFile(directoryType, relativePath, true).getOutputStream(false))){
            out.write(string.getBytes("UTF-8"));
        } catch (Exception e){
            throw new Exception("Could not write file '" + relativePath + "'", e);
        }
    }

    @Override
    public void deleteFile(StorageType directoryType, String relativePath) throws Exception {
        FSFFile file = getFile(directoryType, relativePath, false);
        if (file.exists()){
            file.delete();
        }
    }

    @Override
    public InputStream getInputStreamOrNull(StorageType directoryType, String relativePath) {
        try{
            FSFFile file = getFile(directoryType, relativePath, true);
            if (!file.exists()){
                return null;
            }
            return file.getInputStream();
        } catch (Exception e){
            return null;
        }
    }

    @Override
    public String getStringOrNull(StorageType directoryType, String relativePath) {
        try{
            FSFFile file = getFile(directoryType, relativePath, true);
            if (!file.exists()){
                return null;
            }
            return file.readString();
        } catch (Exception e){
            return null;
        }
    }


    @Override
    public String getAbsolutePath(StorageType directoryType, String relativePath) {
        try {
            return getFile(directoryType, relativePath, true).getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public LAndroidResourceAccess getResourceAccess() {
        return resourceAccess;
    }


    @Override
    public LImage getImage(LImageResource imageResource) throws Exception {
        return resourceAccess.getImage(imageResource);
    }

    @Override
    public List<LAnimationFrame> getAnimationFrames(LAnimationResource animationResource) throws Exception {
        return resourceAccess.getAnimationFrames(animationResource);
    }

    @Override
    public LFont getFont(LFontResource font) throws Exception {
        return resourceAccess.getFont(font);
    }

    @Override
    public String getRawResourceAsString(LRawResource rawResource, int index) throws Exception {
        return resourceAccess.readRawResourceAsUTF8String(rawResource, index);
    }

    @Override
    public int getRawResourceNumberOfElements(LRawResource rawResource) throws Exception {
        return resourceAccess.getElementsPerResource(rawResource);
    }

    @Override
    public String getRawResourceName(LRawResource rawResource, int index) throws Exception {
        return resourceAccess.getRawResourceName(lActivity.getContext(), rawResource, index);
    }

    @Override
    public Map<String, LImage> getIdToImageMap() throws Exception {
        return resourceAccess.getIdToImageMap();
    }

    @Override
    public Map<String, LFont> getIdToFontMap() throws Exception {
        return resourceAccess.getIdToFontMap();
    }

    @Override
    public void quit() {
        lActivity.getActivity().finish();
    }

    @Override
    public boolean isExternalDirPermissionGranted() {
        return ContextCompat.checkSelfPermission(lActivity.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void setPresenter(LScreenPresenter presenter) {
        lActivity.setScreenPresenter(presenter);
    }

    public static void toast(final Context context, final String message){
        toast(context, message, Toast.LENGTH_SHORT);
    }

    public static void toast(final Context context, final String message, final int length){
        new AsyncTask<Object, Object, Object>(){
            @Override
            protected Object doInBackground(Object... params) {
                return null;
            }

            @Override
            protected void onPostExecute(Object result) {
                Toast.makeText(context, message, length);
            }
        }.execute();
    }

    @Override
    public void debug(String message) {
        logDebug(message);
    }

    @Override
    public void error(Throwable t) {
        logError(t);
    }

    public abstract void logDebug(String message);
    public abstract void logError(Throwable t);

    @Override
    public LLogger getLogger() {
        return this;
    }
}
