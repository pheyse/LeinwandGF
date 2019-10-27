package de.bright_side.lgf.pc.base;

import java.awt.Font;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import de.bright_side.lgf.base.LConstants.ScaleMode;
import de.bright_side.lgf.logic.LVirtualSizeLogic;
import de.bright_side.lgf.model.LAnimationResource;
import de.bright_side.lgf.model.LFontResource;
import de.bright_side.lgf.model.LImageResource;
import de.bright_side.lgf.model.LRawResource;
import de.bright_side.lgf.model.LVector;
import de.bright_side.lgf.pc.model.LPcAnimationResourceProperties;
import de.bright_side.lgf.pc.view.LPcMainWindow;
import de.bright_side.lgf.presenter.LScreenPresenter;
import de.bright_side.lgf.view.LScreenView;

public abstract class LPcInstance {
	private LPcMainWindow mainWindow;
	private LPcPlatform platform;
	private LPcResourceDAO resourceDAO;
	private LVector virtualSize;
	private LVector windowContentSize;
	private double scaleFactor;
	
	/**
	 * 
	 * @param resourceDAO
	 * @param prefVirtualSize preferred virtual size
	 * @param scaleMode 
	 * @param aspectRatio 
	 */
	public LPcInstance(LPcResourceDAO resourceDAO, LVector windowContentSize, LVector prefVirtualSize, ScaleMode scaleMode, double aspectRatio) {
		this.resourceDAO = resourceDAO;
		this.windowContentSize = windowContentSize;
		LVirtualSizeLogic virtualSizeLogic = new LVirtualSizeLogic();
		virtualSize = virtualSizeLogic.getVirtualSize(prefVirtualSize, windowContentSize, scaleMode, aspectRatio);
		scaleFactor = virtualSizeLogic.getVirtualSizeScaleFactor(prefVirtualSize, windowContentSize, scaleMode, aspectRatio);
	}

	public abstract void addStatistics(Double fps, Double idleDuration, Double drawDuration, Double updateDuration, Integer levelID, String levelPos, Integer levelRemainingSeconds, String levelState);
	public abstract void sendStatisticsIfNotBusy();
	public abstract void statisticsSenderFinished(Exception exception);
	public abstract String getAppVersion();
	public abstract String getSystemType();
	public abstract LScreenPresenter createFirstScreenPresenter(LPcPlatform platform, LScreenView view);
	
	public void run(boolean resume) {
		mainWindow = new LPcMainWindow(this, resume);
		platform = new LPcPlatform(this, mainWindow);
		LPcResourceAccess resourceAccess = new LPcResourceAccess(this);
		platform.init(resourceAccess);
		mainWindow.init(platform);
		
		mainWindow.setVisible(true);
	}

	public Map<LImageResource, String> createImageResourceMap(){
		return resourceDAO.createImageResourceMap();
	}
	public Map<LRawResource, List<PCResourceProperties>> createRawResourceMap(){
		return resourceDAO.createRawResourceMap();
	}
	
	public Map<LAnimationResource, LPcAnimationResourceProperties> createAnimationResourceMap(){
		return resourceDAO.createAnimationResourceMap();
	}
	
	public Map<LFontResource, Font> createFontResourceMap(){
		return resourceDAO.createFontResourceMap();
	}
	
	public InputStream getImageInputStream(String path) {
		return resourceDAO.getImageInputStream(path);
	}
	public InputStream getRawResourceInputStream(String path) {
		return resourceDAO.getRawResourceInputStream(path);
	}
	
	public LPcPlatform getPlatform() {
		return platform;
	}

	public abstract String getWindowTitle();

	public LVector getVirtualSize() {
		return virtualSize;
	}
	
	public double getScaleFactor() {
		return scaleFactor;
	}
	
	public LVector getWindowContentSize() {
		return windowContentSize;
	}

	
}
