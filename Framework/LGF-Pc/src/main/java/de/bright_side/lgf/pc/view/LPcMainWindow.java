package de.bright_side.lgf.pc.view;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import de.bright_side.lgf.model.LInput;
import de.bright_side.lgf.model.LPointer;
import de.bright_side.lgf.model.LRenderStatistics;
import de.bright_side.lgf.model.LVector;
import de.bright_side.lgf.pc.base.LPcInstance;
import de.bright_side.lgf.pc.base.LPcPlatform;
import de.bright_side.lgf.presenter.LScreenPresenter;
import de.bright_side.lgf.util.LErrorListener;
import de.bright_side.lgf.util.LUtil;
import de.bright_side.lgf.view.LScreenView;

public class LPcMainWindow extends JFrame{
	private static final boolean LOGGING_ENABLED = true;
	
	private static final long serialVersionUID = 1881881724769110985L;
	private static final int DECORATION_HEIGHT = 50;
	private static final int DECORATION_WIDTH = 20;
	private static final double MILLIS_PER_SECOND = 1000;
	
	private LVector virtualSize;
	
	private LScreenView gameView;
	private LScreenPresenter gamePresenter;
	private LPcGameViewComponent gameViewComponent;
	private LVector windowContentSize;
	private LPcInstance instance;
	
	public LPcMainWindow(LPcInstance instance, boolean resume) {
		this.instance = instance;
		virtualSize = instance.getVirtualSize();
		windowContentSize = instance.getWindowContentSize();

		try{
    		setTitle(instance.getWindowTitle());
    		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    		getContentPane().setLayout(new BorderLayout());
    		
    		addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent evt) {
					if (gamePresenter != null) {
						gamePresenter.onClose();
					}
					System.exit(0);
				}
			});
    		
			this.addKeyListener(new KeyListener() {
				public void keyPressed(KeyEvent e) {
				}

				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						if (gameViewComponent != null){
							gameViewComponent.setBackPressed(true);
						}
					}
				}

				public void keyTyped(KeyEvent e) {
				}
			});
    		
    	} catch (Exception e){
    		handleError(e);
    	}
	}
	
	public void init(LPcPlatform platform) {
        gameView = new LScreenView(platform, virtualSize, instance.getScaleFactor());
        gamePresenter = instance.createFirstScreenPresenter(platform, gameView);
        gameViewComponent = new LPcGameViewComponent(platform, virtualSize);
        gameViewComponent.setGameView(gameView);
		
		getContentPane().add(gameViewComponent, BorderLayout.CENTER);
		setSize((int)windowContentSize.x + DECORATION_WIDTH, (int)windowContentSize.y + DECORATION_HEIGHT);
		setLocationRelativeTo(null);
		
		startUpdateThread();
		log("init complete");
	}
    
	private void log(String message) {
		if (LOGGING_ENABLED) {
			System.out.println("LPcMainWindow> " + message);
		}
	}

	private void startUpdateThread() {
		final LErrorListener errorListener = new LErrorListener() {
			@Override
			public void onError(Throwable t) {
				handleError(t);
			}
		};
		
		new Thread() {
			public void run() {
		        long desiredTimeBetweenIterations = 16 - 1;
		        long remainingSleepTime = 0;
		        long lastTime = System.nanoTime() / 1_000_000;
		        long updateDuration = 0;
		        long drawDuration = 0;
		        
				while (true) {
					try {
						long currentTime = System.nanoTime() / 1_000_000;
						LInput gameInput = buildGameInput();
						LRenderStatistics statistics = new LRenderStatistics(remainingSleepTime, drawDuration, updateDuration);
						
						LUtil.callTouchedActions(gameInput.getTouchedObjects(), errorListener);
						if (gamePresenter != null){
							gamePresenter.update(gameInput, (currentTime - lastTime) / MILLIS_PER_SECOND, statistics);
						}
						long timeAfterUpdate = System.nanoTime() / 1_000_000;
						updateDuration = timeAfterUpdate - currentTime;
						if (gameViewComponent != null){
							gameViewComponent.update(currentTime - lastTime);
						}
						lastTime = currentTime;
						drawDuration = (System.nanoTime() / 1_000_000) - timeAfterUpdate;
						
			            long passedTime = (System.nanoTime() / 1_000_000) - currentTime;
			            remainingSleepTime = desiredTimeBetweenIterations - passedTime;

		                try {
		                    Thread.sleep(Math.max(remainingSleepTime, 0));
		                } catch (InterruptedException ignored) {
		                }
					} catch (Throwable t) {
						handleError(t);
					}
				}
			}; 
		}.start();
	}
	
	private <T> Map<Integer, T> singleItemMap(T item){
		Map<Integer, T> result = new HashMap<Integer, T>();
		result.put(1, item);
		return result;
	}

	private LInput buildGameInput() {
		LInput result = new LInput();
		result.setBackButtonPressed(gameViewComponent.isBackPressed());
		result.setTouchedObjects(gameViewComponent.getTouchedObjects());

		if (!gameViewComponent.isMouseDown()){
			gameViewComponent.resetEvents();
			return result;
		}
		
		LPointer pointer = new LPointer();
		result.setWasTouched(gameViewComponent.getClickPos() != null);
		
		pointer.setTouchDownPos(gameViewComponent.getTouchDownPos());
		
		if (gameViewComponent.getDragMoveAmount() != null){
			pointer.setDragDistance(gameViewComponent.getDragMoveAmount());
		} else {
			pointer.setDragDistance(new LVector(0,  0));
		}
		pointer.setPos(gameViewComponent.getLastTouchPos());
		result.setPointers(singleItemMap(pointer));
		
		gameViewComponent.resetEvents();
		return result;
	}

	private void handleError(Throwable t) {
		if (t != null) {
			t.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error: " + t.getMessage() + "\nSee console/log for details");
		}
	}

	public void setPresenter(LScreenPresenter presenter) {
		gamePresenter = presenter;
	}

}
