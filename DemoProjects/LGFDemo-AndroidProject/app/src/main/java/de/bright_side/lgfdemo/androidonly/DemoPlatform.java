package de.bright_side.lgfdemo.androidonly;

import android.Manifest;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.File;

import de.bright_side.filesystemfacade.nativefs.NativeFS;
import de.bright_side.lgf.android.base.LAndroidPlatform;
import de.bright_side.lgf.android.base.LAndroidResourceAccess;
import de.bright_side.lgf.android.base.LAndroidUtil;
import de.bright_side.lgf.android.view.LActivity;
import de.bright_side.lgf.base.LConstants;
import de.bright_side.lgf.model.LBuyableItem;
import de.bright_side.lgfdemo.BuildConfig;

public class DemoPlatform extends LAndroidPlatform {
    private static final int REQUEST_CODE_PERMISSIONS_REQUEST_WRITE_STORAGE = 1001;
    public static final String TAG_NAME = "LGF_Demo";
    private final LActivity activity;

    public DemoPlatform(LActivity activity, Resources resources) {
        super(activity, new NativeFS(), resources);
        this.activity = activity;
    }

    @Override
    public LAndroidResourceAccess createResourceAccess(Resources resources, LActivity lActivity) {
        return new DemoResourceAccess(this, resources , lActivity.getContext());
    }

    @Override
    public LConstants.InputMode getDefaultInputMode() {
        return LConstants.InputMode.MULTI_POINTER;
    }

    @Override
    public File getPublicApplicationDir(Context context) throws Exception{
        return LAndroidUtil.getPublicApplicationDirOnSDRoot(context, BuildConfig.FLAVOR);
    }

    @Override
    public void handleMissingResourceError(String message) {
        Toast.makeText(activity.getContext(), "Missing resource: " + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showExternalDirPermissionRequest() {
        ActivityCompat.requestPermissions(activity.getActivity()
                , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                , REQUEST_CODE_PERMISSIONS_REQUEST_WRITE_STORAGE);
    }

    @Override
    public void addStatistics(Double fps, Double idleDuration, Double drawDuration, Double updateDuration,
                              Integer levelID, String levelPos, Integer levelRemainingSeconds, String levelState) {
    }

    @Override
    public synchronized void sendStatisticsIfNotBusy() {
    }

    @Override
    public void statisticsSenderFinished(Exception exception) {
    }

    @Override
    public String getAppVersion() {
        return BuildConfig.VERSION_CODE + "-" + BuildConfig.BUILD_TYPE + "(" + BuildConfig.FLAVOR + ")";
    }

    @Override
    public String getSystemType() {
        return "Manufacturer: " + Build.MANUFACTURER + ", Model: " + Build.MODEL + ", Brand: " + android.os.Build.BRAND +", OS: "
                + android.os.Build.VERSION.RELEASE + ", API-Level: " + android.os.Build.VERSION.SDK_INT;
    }

    @Override
    public void handleInternalError(Exception e) {
        Log.e(TAG_NAME, "internal error", e);
    }

    @Override
    public void sendEMail(String eMailAddress) {
    }

    @Override
    public boolean isBought(LBuyableItem item){
        return false;
    }

    @Override
    public void log(Object source, String text) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        String location = "";
        if (source != null){
            location = source.getClass().getSimpleName() + ":";
        }
        getLogger().debug(location + text);
    }

    @Override
    public boolean readIsBoughtFromCache(LBuyableItem item){
        return false;
    }

    @Override
    public void buyItem(LBuyableItem item) {
    }

    @Override
    public void logDebug(String message) {
        LAndroidUtil.logAndGenerateLocation(TAG_NAME, LConstants.LogLevel.DEBUG, message);
    }

    @Override
    public void logError(Throwable t) {
        if (t != null){
            Log.e(TAG_NAME, "error", t);
        }
        LAndroidUtil.logAndGenerateLocation(TAG_NAME, LConstants.LogLevel.ERROR, "" + t);
    }

}
