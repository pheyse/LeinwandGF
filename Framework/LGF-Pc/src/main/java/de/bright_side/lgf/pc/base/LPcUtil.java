package de.bright_side.lgf.pc.base;

import de.bright_side.lgf.model.LVector;
import de.bright_side.lgf.util.LMathsUtil;
import de.bright_side.lgf.view.LScreenView;

public class LPcUtil {
    public static int cielInt(double value) {
        int intValue = (int)value;
        if (value > intValue) {
        	return intValue + 1;
        } else {
            return intValue;
        }
    }
    
    public static LVector applyCameraPos(LVector pos, LVector virtualSize, LScreenView view) {
        LVector halfScreenSize = LMathsUtil.multiply(virtualSize, 0.5);
        LVector cameraTopLeft = LMathsUtil.subtract(view.getCameraPos(), halfScreenSize);
        LVector scrollOffset = LMathsUtil.multiply(cameraTopLeft, -1.0);
        return LMathsUtil.subtract(pos, scrollOffset);
    }

}
