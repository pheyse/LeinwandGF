package de.bright_side.lgf.base;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import de.bright_side.lgf.base.LConstants.InputMode;
import de.bright_side.lgf.model.LAnimationFrame;
import de.bright_side.lgf.model.LAnimationResource;
import de.bright_side.lgf.model.LBuyableItem;
import de.bright_side.lgf.model.LColor;
import de.bright_side.lgf.model.LFont;
import de.bright_side.lgf.model.LFontResource;
import de.bright_side.lgf.model.LImage;
import de.bright_side.lgf.model.LImageResource;
import de.bright_side.lgf.model.LRawResource;
import de.bright_side.lgf.model.LVector;
import de.bright_side.lgf.presenter.LScreenPresenter;

public interface LPlatform {
    void log(Object source, String text);

    void buyItem(LBuyableItem item);
    boolean isBought(LBuyableItem item);
    boolean readIsBoughtFromCache(LBuyableItem item);

    enum StorageType {INTERNAL, EXTERNAL}

    LColor getColorRed();
    LColor getColorYellow();
    LColor getColorBlue();
    LColor getColorGreen();
    LColor getColorGray();
    LColor getColorBlack();
    LColor getColorWhite();
    LColor getCustomColor(int red, int green, int blue);
    LColor getCustomColor(int alpha, int red, int green, int blue);

    LImage getImage(LImageResource imageResource) throws Exception;
    List<LAnimationFrame> getAnimationFrames(LAnimationResource animationResource) throws Exception;
    LFont getFont(LFontResource font) throws Exception;

    String getRawResourceAsString(LRawResource rawResource, int index) throws Exception;
    int getRawResourceNumberOfElements(LRawResource rawResource) throws Exception;
    String getRawResourceName(LRawResource rawResource, int index) throws Exception;
    
    LLogger getLogger();

    void setPresenter(LScreenPresenter presenter);

    LImage createImageFromColorMap(Map<LVector, LColor> colorsMap, LVector size, LColor defaultColor);

    <K> K readObjectOrNull(StorageType directoryType, String relativePath, Class<K> classType) throws Exception;
    <K> void writeObject(StorageType directoryType, String relativePath, K object) throws Exception;
    void writeStringToFile(StorageType directoryType, String relativePath, String string) throws Exception;
    void deleteFile(StorageType directoryType, String relativePath) throws Exception;
    InputStream getInputStreamOrNull(StorageType directoryType, String relativePath);

    void getTextInput(String title, String message, String defaultValue, LTextInputResultListener listener);
    void showMessageDialog(String title, String message);
    InputMode getDefaultInputMode();

    String getStringOrNull(StorageType directoryType, String relativePath);

    String getAbsolutePath(StorageType directoryType, String relativePath);
	void handleMissingResourceError(String message);
	void quit();
	Map<String, LImage> getIdToImageMap() throws Exception;
	Map<String, LFont> getIdToFontMap() throws Exception;

    boolean isExternalDirPermissionGranted();
    void showExternalDirPermissionRequest();

    void addStatistics(Double fps, Double idleDuration, Double drawDuration, Double updateDuration, Integer levelID, String levelPos, Integer levelRemainingSeconds, String levelState);
    void sendStatisticsIfNotBusy();
	void statisticsSenderFinished(Exception exception);
	String getAppVersion();
	String getSystemType();

    void handleInternalError(Exception e);

    void sendEMail(String eMailAddress);
	
}
