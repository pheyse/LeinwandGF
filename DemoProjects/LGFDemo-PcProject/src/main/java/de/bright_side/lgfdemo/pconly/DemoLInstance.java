package de.bright_side.lgfdemo.pconly;

import de.bright_side.lgf.base.LConstants.ScaleMode;
import de.bright_side.lgf.model.LVector;
import de.bright_side.lgf.pc.base.LPcInstance;
import de.bright_side.lgf.pc.base.LPcPlatform;
import de.bright_side.lgf.presenter.LScreenPresenter;
import de.bright_side.lgf.view.LScreenView;
import de.bright_side.lgfdemo.base.DemoConstants;
import de.bright_side.lgfdemo.presenter.DemoScreenStartPresenter;

public class DemoLInstance extends LPcInstance{
	private static final LVector WINDOW_CONTENT_SIZE_LANDSCAPE = new LVector(DemoConstants.PREF_VIRTUAL_SIZE.x * 4, DemoConstants.PREF_VIRTUAL_SIZE.y * 4);
	@SuppressWarnings("unused")
	private static final LVector WINDOW_CONTENT_SIZE_PORTRAIT = new LVector(DemoConstants.PREF_VIRTUAL_SIZE.x * 2.5, DemoConstants.PREF_VIRTUAL_SIZE.y * 8);

	private static final LVector WINDOW_CONTENT_SIZE = WINDOW_CONTENT_SIZE_LANDSCAPE;

	
	public DemoLInstance() {
		super(new DemoResourceDAO(), WINDOW_CONTENT_SIZE, DemoConstants.PREF_VIRTUAL_SIZE, ScaleMode.FIT_BOTH, 1);
	}
	
	
	@Override
	public void addStatistics(Double fps, Double idleDuration, Double drawDuration, Double updateDuration,
			Integer levelID, String levelPos, Integer levelRemainingSeconds, String levelState) {
	}

	@Override
	public void sendStatisticsIfNotBusy() {
	}

	@Override
	public void statisticsSenderFinished(Exception exception) {
	}

	@Override
	public String getAppVersion() {
		return "Desktop-Version";
	}

	@Override
	public String getSystemType() {
		return "Java Desktop";
	}
	
    public static void main(String[] args) {
    	boolean resume = (args.length == 1) && (args[0].equals("resume"));
    	new DemoLInstance().run(resume);
    }

	@Override
	public LScreenPresenter createFirstScreenPresenter(LPcPlatform platform, LScreenView view) {
		return new DemoScreenStartPresenter(platform, view);
	}

	@Override
	public String getWindowTitle() {
		return "Leinwand Graphics Framework: PC Demo";
	}

}
