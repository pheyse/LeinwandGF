package de.bright_side.lgfdemo.androidonly;

import android.os.Bundle;

import de.bright_side.lgf.android.base.LAndroidPlatform;
import de.bright_side.lgf.android.view.LAndroidActivity;
import de.bright_side.lgf.base.LConstants;
import de.bright_side.lgf.presenter.LScreenPresenter;
import de.bright_side.lgf.view.LScreenView;
import de.bright_side.lgfdemo.base.DemoConstants;
import de.bright_side.lgfdemo.presenter.DemoScreenStartPresenter;

public class MainActivity extends LAndroidActivity {
    private LAndroidPlatform platform;

    public MainActivity() {
        super(DemoConstants.PREF_VIRTUAL_SIZE, LConstants.ScaleMode.FIT_BOTH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        platform = new DemoPlatform(this, getResources());
        initOnCreate(platform, DemoPlatform.TAG_NAME);
    }

    @Override
    public LScreenPresenter createFirstScreenPresenter(LAndroidPlatform androidPlatform, LScreenView view) {
        return new DemoScreenStartPresenter(platform, view);
    }
}
