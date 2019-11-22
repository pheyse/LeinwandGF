package de.bright_side.lgf.android.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.HashMap;

import de.bright_side.lgf.android.base.LAndroidPlatform;
import de.bright_side.lgf.android.model.LAndroidActivityModel;
import de.bright_side.lgf.android.base.LAndroidActivityPresenter;
import de.bright_side.lgf.android.base.LAndroidUtil;
import de.bright_side.lgf.base.LConstants;
import de.bright_side.lgf.logic.LVirtualSizeLogic;
import de.bright_side.lgf.model.LPointer;
import de.bright_side.lgf.model.LVector;
import de.bright_side.lgf.presenter.LScreenPresenter;
import de.bright_side.lgf.view.LScreenView;

public abstract class LAndroidActivity extends Activity implements LActivity {
    private static final double ANDROID_ADJUSTMENT = 2;
    private final LVector prefVirtualSize;
    private LVector virtualSize;
    private double scaleFactor;
    private final LConstants.ScaleMode scaleMode;
    private LAndroidComponent viewComponent;
    private LAndroidActivityPresenter presenter;
    private LScreenView screenView;
    private LAndroidActivityModel model;
    private static final boolean LOGGING_ENABLED = true;

    protected LAndroidActivity(LVector prefVirtualSize, LConstants.ScaleMode scaleMode) {
        super();
        this.prefVirtualSize = prefVirtualSize;
        this.scaleMode = scaleMode;
    }

    private void calcVirtualSize() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        double aspectRatio = metrics.xdpi / metrics.ydpi;
        LVector contentSize = new LVector(metrics.widthPixels, metrics.heightPixels);
        LVirtualSizeLogic virtualSizeLogic = new LVirtualSizeLogic();
        this.virtualSize = virtualSizeLogic.getVirtualSize(prefVirtualSize, contentSize, scaleMode, aspectRatio);
        this.scaleFactor = virtualSizeLogic.getVirtualSizeScaleFactor(prefVirtualSize, contentSize, scaleMode, aspectRatio);
    }

    public abstract LScreenPresenter createFirstScreenPresenter(LAndroidPlatform platform, LScreenView gameView);

    @Override
    protected void onPause() {
        super.onPause();
        viewComponent.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LAndroidUtil.enterFullScreenMode(viewComponent);
        viewComponent.resume();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void setScreenPresenter(LScreenPresenter presenter) {
        viewComponent.setScreenPresenter(presenter);
    }

    public void setPresenter(LAndroidActivityPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LVector size = viewComponent.getSizeAsVector();
        if (size != null){
            presenter.onTouchEvent(event, screenView, size);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        if (model != null){
            model.setBackButtonPressed(true);
        }
    }

    public void initOnCreate(LAndroidPlatform platform, String tag){
        calcVirtualSize();
        model = new LAndroidActivityModel();
        presenter = new LAndroidActivityPresenter(model, virtualSize);



        model.setActiveInternalPointers(new SparseArray<PointF>());
        model.setActivePointers(new HashMap<Integer, LPointer>());

        viewComponent = new LAndroidComponent(this);
        viewComponent.setVirtualSize(virtualSize);
        viewComponent.setPlatform(platform);
        viewComponent.setActivityPresenter(presenter);

        LAndroidUtil.enterFullScreenMode(viewComponent);

        setContentView(viewComponent);

        try{
            screenView = new LScreenView(platform, virtualSize, scaleFactor * ANDROID_ADJUSTMENT);
            viewComponent.setScreenView(screenView);
            LScreenPresenter firstScreenPresenter = createFirstScreenPresenter(platform, screenView);
            if (firstScreenPresenter == null){
                throw new RuntimeException("the method createFirstScreenPresenter that is overwritten by the developer who uses LGF returned null");
            }
            viewComponent.setScreenPresenter(firstScreenPresenter);

        } catch (Exception e){
            Log.e(tag, "error", e);
            e.printStackTrace();
            Toast.makeText(this, "Could not initialize: " + e, Toast.LENGTH_LONG).show();
        }
    }

    public LAndroidActivityModel getModel(){
        return model;
    }

    private void log(String message) {
        if (LOGGING_ENABLED){
            Log.d("LGF:LAndroidActivity", message);
        }
    }

}
