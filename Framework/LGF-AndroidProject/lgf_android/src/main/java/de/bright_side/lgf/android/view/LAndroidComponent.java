package de.bright_side.lgf.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import de.bright_side.lgf.android.base.LAndroidActivityPresenter;
import de.bright_side.lgf.base.LLogger;
import de.bright_side.lgf.base.LPlatform;
import de.bright_side.lgf.model.LInput;
import de.bright_side.lgf.model.LRenderStatistics;
import de.bright_side.lgf.model.LVector;
import de.bright_side.lgf.presenter.LScreenPresenter;
import de.bright_side.lgf.util.LErrorListener;
import de.bright_side.lgf.util.LUtil;
import de.bright_side.lgf.view.LScreenView;

public class LAndroidComponent extends SurfaceView implements Runnable {
    private static final double MILLIS_PER_SECOND = 1000;

    private LLogger logger;
    volatile boolean playing;
    private Thread mainThread = null;
    private LScreenView screenView;
    private LVector virtualSize;
    private LAndroidActivityPresenter activityPresenter;

    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private LScreenPresenter screenPresenter;
    private long lastUpdateTime;
    private long idleTime;
    private LVector androidCanvasSize;

    private long desiredTimeBetweenIterations = 16;

    private LErrorListener errorListener = createErrorListener();

    public LAndroidComponent(Context context) {
        super(context);
        surfaceHolder = getHolder();
    }

    public void setPlatform(LPlatform platform) {
        logger = platform.getLogger();
    }

    public void setActivityPresenter(LAndroidActivityPresenter activityPresenter) {
        this.activityPresenter = activityPresenter;
    }

    @Override
    public void run() {
        long lastUpdateDuration = 0;
        long lastDrawDuration = 0;
        lastUpdateTime = getCurrentTime();
        while (playing) {
            try{
                long startTime = System.nanoTime() / 1_000_000;

                draw();
                long timeAfterDraw = System.nanoTime() / 1_000_000;
                lastDrawDuration = timeAfterDraw - startTime;

                update(lastDrawDuration, lastUpdateDuration);
                long endTime = System.nanoTime() / 1_000_000;
                lastUpdateDuration = endTime - timeAfterDraw;


                long passedTime = endTime - startTime;
                idleTime = desiredTimeBetweenIterations - passedTime;

                try {
                    mainThread.sleep(Math.max(idleTime, 0));
                } catch (InterruptedException ignored) {
                }
            } catch (Throwable t){
                t.printStackTrace();
                try {
                    mainThread.sleep(500);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    public void setVirtualSize(LVector virtualSize) {
        this.virtualSize = virtualSize;
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                canvas = surfaceHolder.lockHardwareCanvas();
            } else {
                canvas = surfaceHolder.lockCanvas();
            }
            canvas.drawColor(Color.BLACK);
            if (screenView != null){
                screenView.draw(new LAndroidCanvas(logger, virtualSize, screenView.getCameraPos(), canvas, getResources()));
                androidCanvasSize = new LVector(canvas.getWidth(), canvas.getHeight());
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        } else {
            Log.d("LAndroidComponent", "Surface invalid");
        }
    }

    public void setScreenView(LScreenView screenView) {
        this.screenView = screenView;
    }

    public LVector getSizeAsVector() {
        return androidCanvasSize;
    }

    public LVector getVirtualSize() {
        return virtualSize;
    }

    private long getCurrentTime(){
        return System.nanoTime() / 1_000_000;
    }

    private void update(long drawDuration, long updateDuration) {
        long currentTime = getCurrentTime();
        if (screenPresenter != null){
            LInput LInput = activityPresenter.buildInput();
            LRenderStatistics statistics = new LRenderStatistics(idleTime, drawDuration, updateDuration);
            LUtil.callTouchedActions(LInput.getTouchedObjects(), errorListener);

            screenPresenter.update(LInput, (currentTime - lastUpdateTime) / MILLIS_PER_SECOND, statistics);
        }
        lastUpdateTime = currentTime;
    }

    public void pause() {
        playing = false;
        screenPresenter.onClose();
        try {
            mainThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        playing = true;
        mainThread = new Thread(this);
        mainThread.start();
    }

    public void setScreenPresenter(LScreenPresenter screenPresenter) {
        this.screenPresenter = screenPresenter;
    }

    private LErrorListener createErrorListener() {
        return new LErrorListener() {
            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                if (LAndroidComponent.this.activityPresenter != null){
                    t.printStackTrace();
                    Log.e("LAndroidComponent", "error", t);
                    Toast.makeText(getContext(), "Error: " + t, Toast.LENGTH_LONG).show();
                }
            }
        };
    }


}