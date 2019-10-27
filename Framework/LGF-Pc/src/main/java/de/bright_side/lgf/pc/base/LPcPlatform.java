package de.bright_side.lgf.pc.base;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import de.bright_side.filesystemfacade.facade.FSFFile;
import de.bright_side.filesystemfacade.facade.FSFSystem;
import de.bright_side.filesystemfacade.nativefs.NativeFS;
import de.bright_side.lgf.base.LLogger;
import de.bright_side.lgf.base.LPlatform;
import de.bright_side.lgf.base.LTextInputResultListener;
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
import de.bright_side.lgf.pc.view.LPcMainWindow;
import de.bright_side.lgf.pc.view.LPcMessagesWindow;
import de.bright_side.lgf.presenter.LScreenPresenter;


public class LPcPlatform implements LPlatform{
	private LPcLogger logger = new LPcLogger();
	private LPcResourceAccess resourceAccess;
	private LPcMainWindow mainWindow;
	private FSFSystem fsfSystem = new NativeFS();
	private LPcMessagesWindow messagesWindow = new LPcMessagesWindow();
	private LPcInstance instance;
	
	public LPcPlatform(LPcInstance instance, LPcMainWindow mainWindow) {
		this.instance = instance;
		this.mainWindow = mainWindow;
	}
	
	public void init(LPcResourceAccess resourceAccess) {
		this.resourceAccess = resourceAccess;
	}
	
	@Override
	public LColor getColorRed() {
		return new LColor(Color.RED.getRGB());
	}

	@Override
	public LColor getColorYellow() {
		return new LColor(Color.YELLOW.getRGB());
	}

	@Override
	public LColor getColorBlue() {
		return new LColor(Color.BLUE.getRGB());
	}

	@Override
	public LColor getColorGreen() {
		return new LColor(Color.GREEN.getRGB());
	}

	@Override
	public LColor getColorGray() {
		return new LColor(Color.GRAY.getRGB());
	}
	
	@Override
	public LColor getColorBlack() {
		return new LColor(Color.BLACK.getRGB());
	}

	@Override
	public LColor getColorWhite() {
		return new LColor(Color.WHITE.getRGB());
	}
	
	@Override
	public LColor getCustomColor(int red, int green, int blue) {
		return new LColor(new Color(red, green, blue).getRGB());
	}
	
	@Override
	public LColor getCustomColor(int alpha, int red, int green, int blue) {
		return new LColor(new Color(red, green, blue, alpha).getRGB());
	}

	@Override
	public LImage getImage(LImageResource imageResource) throws Exception {
		return resourceAccess.getImage(imageResource);
	}

	@Override
	public LLogger getLogger() {
		return logger;
	}

	@Override
	public String getRawResourceAsString(LRawResource rawResource, int index) throws Exception {
		return resourceAccess.readRawResourceAsUTF8String(rawResource, index);
	}
	
	@Override
	public String getRawResourceName(LRawResource rawResource, int index) throws Exception {
		return resourceAccess.getRawResourceName(rawResource, index);
	}
	
	@Override
	public int getRawResourceNumberOfElements(LRawResource rawResource) throws Exception{
		return resourceAccess.getElementsPerResource(rawResource);
	}
	
	@Override
	public LImage createImageFromColorMap(Map<LVector, LColor> colorsMap, LVector size, LColor defaultColor) {
		BufferedImage image = new BufferedImage((int)size.getX(), (int)size.getY(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = image.createGraphics();
		g.setColor(new Color(defaultColor.getAsInt()));
		g.fillRect(0, 0, (int)size.getX(), (int)size.getY());
		for (Entry<LVector, LColor> i: colorsMap.entrySet()){
			g.setColor(new Color(i.getValue().getAsInt()));
			LVector pos = i.getKey();
			g.drawRect((int)pos.getX(), (int)pos.getY(), 1, 1);
		}
		
		LImage result = new LImage();
		result.setHasAlpha(false);
		result.setImageObject(image);
		result.setId("colorMapImage");
		result.setSize(size);
		
		return result;
	}

	@Override
	public void setPresenter(LScreenPresenter presenter) {
		mainWindow.setPresenter(presenter);
	}
	
	private FSFFile getFile(StorageType directoryType, String relativePath, boolean createParentDir) throws Exception{
		File dataDir = new File("data");
		if (directoryType == StorageType.INTERNAL){
			dataDir = new File(dataDir, "internal");
		} else if (directoryType == StorageType.EXTERNAL){
			dataDir = new File(dataDir, "external");
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
	public void getTextInput(String title, String message, String defaultValue, LTextInputResultListener listener) {
		Object result = JOptionPane.showInputDialog(mainWindow, message, title, JOptionPane.PLAIN_MESSAGE, null, null, defaultValue);
		if (result == null){
			listener.onTextInputResult(null);
		} else {
			listener.onTextInputResult(result.toString());
		}
	}

	@Override
	public void showMessageDialog(String title, String message) {
		JOptionPane.showMessageDialog(mainWindow, message, title, JOptionPane.PLAIN_MESSAGE);
	}


	@Override
	public LFont getFont(LFontResource font) throws Exception {
		if (resourceAccess == null) {
			throw new Exception("resourceAccess not defined");
		}
		return resourceAccess.getFont(font);
	}

	@Override
	public List<LAnimationFrame> getAnimationFrames(LAnimationResource animationResource) throws Exception {
		return resourceAccess.getAnimationFrames(animationResource);
	}

	@Override
	public InputMode getDefaultInputMode() {
		return InputMode.SINGLE_POINTER;
	}

	@Override
	public InputStream getInputStreamOrNull(StorageType directoryType, String relativePath) {
		try {
			FSFFile file = getFile(directoryType, relativePath, true);
			if (!file.exists()){
				return null;
			}
			return file.getInputStream();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String getAbsolutePath(StorageType directoryType, String relativePath) {
		try {
			return getFile(directoryType, relativePath, true).getAbsolutePath();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void handleMissingResourceError(String message) {
		showMessageInMessageWindow(message);
	}
	
	private void showMessageInMessageWindow(String message) {
		messagesWindow.addText(message + "\n");
		messagesWindow.showWindow();
		
	}

	@Override
	public void quit() {
		System.exit(0);
	}

	@Override
	public Map<String, LImage> getIdToImageMap() throws Exception {
		return resourceAccess.getIdToImageMap();
	}

	@Override
	public Map<String, LFont> getIdToFontMap() throws Exception{
		return resourceAccess.getIdToFontMap();
	}

	@Override
	public boolean isExternalDirPermissionGranted() {
		return true;
	}

	@Override
	public void showExternalDirPermissionRequest() {
		showMessageInMessageWindow("Not implemented: showExternalDirPermissionRequest()");
	}

    @Override
    public String getStringOrNull(StorageType directoryType, String relativePath) {
        try{
            FSFFile file = getFile(directoryType, relativePath, true);
            logger.debug("file = '" + file.getAbsolutePath() + "'");
            if (!file.exists()){
                return null;
            }
            return file.readString();
        } catch (Exception e){
            return null;
        }
    }

	@Override
	public void handleInternalError(Exception e) {
		if (e != null) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error: " + e.getMessage() + "\nSee console/log for details");
		}
	}

	@Override
	public boolean isBought(LBuyableItem item) {
		return false; 
	}

	@Override
	public void log(Object source, String message) {
		messagesWindow.addText("" + source + ":" + message + "\n");
		messagesWindow.showWindow();
	}

	@Override
	public boolean readIsBoughtFromCache(LBuyableItem item) {
		return isBought(item);
	}

	@Override
	public void buyItem(LBuyableItem item) {
		JOptionPane.showMessageDialog(null, "Buy level pack");
	}

	@Override
	public void sendEMail(String eMailAddress) {
		JOptionPane.showMessageDialog(null, "Send e-mail");
	}

	@Override
	public void addStatistics(Double fps, Double idleDuration, Double drawDuration, Double updateDuration, Integer levelID, String levelPos, Integer levelRemainingSeconds, String levelState) {
		instance.addStatistics(fps, idleDuration, drawDuration, updateDuration, levelID, levelPos, levelRemainingSeconds, levelState);
	}

	@Override
	public void sendStatisticsIfNotBusy() {
		instance.sendStatisticsIfNotBusy();
	}

	@Override
	public void statisticsSenderFinished(Exception exception) {
		instance.statisticsSenderFinished(exception);
	}

	@Override
	public String getAppVersion() {
		return instance.getAppVersion();
	}

	@Override
	public String getSystemType() {
		return instance.getSystemType();
	}


}
