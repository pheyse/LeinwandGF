package de.bright_side.lgf.pc.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.JComponent;

import de.bright_side.lgf.base.LLogger;
import de.bright_side.lgf.base.LPlatform;
import de.bright_side.lgf.model.LObject;
import de.bright_side.lgf.model.LVector;
import de.bright_side.lgf.pc.base.LPcUtil;
import de.bright_side.lgf.util.LMathsUtil;
import de.bright_side.lgf.view.LScreenView;

public class LPcViewComponent extends JComponent {
	private static final long serialVersionUID = -7513394895347926330L;
	private LLogger logger;

    private LScreenView screenView;
	private LVector virtualSize;
	private LVector lastTouchPos;
	private LVector dragStartPos;
	private LVector moveAmountSinceDragStart;
	private LVector clickPos;
	private LVector touchDownPos;
	private boolean mouseDown = false;
	private List<LObject> touchedObjects;
	private boolean backPressed;
	
	public LPcViewComponent(LPlatform platform, LVector virtualSize) {
		logger = platform.getLogger();
		this.virtualSize = virtualSize;
		
		addMouseMotionListener(createMouseMotionListener());
		addMouseListener(createMouseListener());
	}
	
	private LVector getPos(MouseEvent event) {
        LVector pos = new LVector(event.getX(), event.getY());
        pos = LMathsUtil.divide(pos, getSizeAsVector());
        pos = LMathsUtil.multiply(pos, virtualSize);
        
        return pos;
	}
	
    private LVector getSizeAsVector() {
		return new LVector(getWidth(), getHeight());
	}

	private MouseListener createMouseListener() {
		return new MouseListener() {
			

			@Override
			public void mouseReleased(MouseEvent event) {
				if (event.getButton() == 1) {
					moveAmountSinceDragStart = null;
					mouseDown = false;
				}
			}
			
			@Override
			public void mousePressed(MouseEvent event) {
				LVector pos = getPos(event);
				lastTouchPos = pos;
				dragStartPos = pos;
				if (event.getButton() == 1) {
					mouseDown = true;
					if (touchedObjects == null){
						touchedObjects = screenView.getTouchedObjects(LPcUtil.applyCameraPos(pos, virtualSize, screenView), pos);
					}
					clickPos = pos;
					touchDownPos = pos;
				}
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		};
	}

	private MouseMotionListener createMouseMotionListener() {
		return new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent arg0) {
			}
			
			@Override
			public void mouseDragged(MouseEvent event) {
				LVector pos = getPos(event);
	            moveAmountSinceDragStart = LMathsUtil.subtract(pos, dragStartPos);
	            lastTouchPos = pos;
			}
		};
	}

	public void setVirtualSize(LVector virtualSize) {
        this.virtualSize = virtualSize;
    }

	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
        if (screenView != null){
        	RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        	rh.add(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        	rh.add(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        	rh.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        	((Graphics2D)g).setRenderingHints(rh);
        	
            screenView.draw(new LPcCanvas(logger, virtualSize, (Graphics2D)g, new LVector(getWidth(), getHeight()), screenView.getCameraPos()));
        }

	}

    public void setScreenView(LScreenView screenView) {
        this.screenView = screenView;
    }

	public void update(long millisSinceLastUpdate) {
		repaint();
	}
	
	public LVector getDragMoveAmount() {
		return moveAmountSinceDragStart;
	}
	
	public LVector getClickPos() {
		return clickPos;
	}
	
	public List<LObject> getTouchedObjects() {
		return touchedObjects;
	}
	
	public void resetEvents(){
		clickPos = null;
		touchedObjects = null;
		backPressed = false;
	}

	public void setBackPressed(boolean backPressed) {
		this.backPressed = backPressed;
	}
	
	public boolean isBackPressed() {
		return backPressed;
	}

	public boolean isMouseDown() {
		return mouseDown;
	}

	public void setMouseDown(boolean mouseDown) {
		this.mouseDown = mouseDown;
	}

	public LVector getLastTouchPos() {
		return lastTouchPos;
	}
	
	public LVector getTouchDownPos() {
		return touchDownPos;
	}
}
