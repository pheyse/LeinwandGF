package de.bright_side.lgf.logic;

import de.bright_side.lgf.base.LConstants.ScaleMode;
import de.bright_side.lgf.model.LVector;
import de.bright_side.lgf.util.LMathsUtil;

public class LVirtualSizeLogic {
	public double getAspectRatio(int pixelsPerCmX, int pixelsPerCmY) {
		return ((double)pixelsPerCmX) / ((double)pixelsPerCmY);
	}
	
	public LVector getVirtualSize(LVector prefVirtualSize, LVector screenSize, ScaleMode mode, double aspectRatio) {
		if ((mode == null) || (mode == ScaleMode.NONE)){
			return prefVirtualSize;
		}
		
		double factor = getVirtualSizeScaleFactor(prefVirtualSize, screenSize, mode, aspectRatio);
		LVector screenSizeInUnits = new LVector(screenSize.x, screenSize.y / aspectRatio);
		return LMathsUtil.multiply(screenSizeInUnits, factor);
	}

	public double getVirtualSizeScaleFactor(LVector prefVirtualSize, LVector screenSize, ScaleMode mode, double aspectRatio) {
		if ((mode == null) || (mode == ScaleMode.NONE)){
			return 1;
		}
		
		LVector screenSizeInUnits = new LVector(screenSize.x, screenSize.y / aspectRatio);
		LVector factorToFit = LMathsUtil.divide(prefVirtualSize, screenSizeInUnits);
		
		switch (mode) {
		case MATCH_X:
			return factorToFit.x;
		case MATCH_Y:
			return factorToFit.y;
		default:
			return Math.max(factorToFit.x, factorToFit.y);
		}
	}
	
}
