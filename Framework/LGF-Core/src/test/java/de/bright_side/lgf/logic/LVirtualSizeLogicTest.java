package de.bright_side.lgf.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.bright_side.lgf.base.LConstants.ScaleMode;
import de.bright_side.lgf.logic.LVirtualSizeLogic;
import de.bright_side.lgf.model.LVector;

public class LVirtualSizeLogicTest {
	
	@Test
	public void getVirtualSize_matchXSimple() {
		LVector prefVirtualSize = new LVector(100, 100);
		LVector screenSize = new LVector(1000, 1000);
		double aspectRatio = 1;
		LVector result = new LVirtualSizeLogic().getVirtualSize(prefVirtualSize, screenSize, ScaleMode.MATCH_X, aspectRatio);
		
		assertEquals(100d, result.x);
		assertEquals(100d, result.y);
	}

	@Test
	public void getVirtualSize_matchXRequestTwiceYAndExtraSizeExists() {
		LVector prefVirtualSize = new LVector(100, 200);
		LVector screenSize = new LVector(1000, 2000);
		double aspectRatio = 1;
		LVector result = new LVirtualSizeLogic().getVirtualSize(prefVirtualSize, screenSize, ScaleMode.MATCH_X, aspectRatio);
		
		assertEquals(100d, result.x);
		assertEquals(200d, result.y);
	}

	@Test
	public void getVirtualSize_matchXRequestTwiceYButNoExtraSizeExists() {
		LVector prefVirtualSize = new LVector(100, 200);
		LVector screenSize = new LVector(1000, 1000);
		double aspectRatio = 1;
		LVector result = new LVirtualSizeLogic().getVirtualSize(prefVirtualSize, screenSize, ScaleMode.MATCH_X, aspectRatio);
		
		assertEquals(100d, result.x);
		assertEquals(100d, result.y);
	}
	
	@Test
	public void getVirtualSize_matchXPixelsTwiceAsHighAsTheyAreWide() {
		LVector prefVirtualSize = new LVector(100, 100);
		LVector screenSize = new LVector(1000, 1000);
		double aspectRatio = 2;
		LVirtualSizeLogic virtualSizeLogic = new LVirtualSizeLogic();
		LVector result = virtualSizeLogic.getVirtualSize(prefVirtualSize, screenSize, ScaleMode.MATCH_X, aspectRatio);
		
		assertEquals(100d, result.x);
		assertEquals(50d, result.y);
	}

	@Test
	public void getVirtualSize_matchXPixelsHalfAsHighAsTheyAreWide() {
		LVector prefVirtualSize = new LVector(100, 100);
		LVector screenSize = new LVector(1000, 1000);
		double aspectRatio = 0.5;
		LVector result = new LVirtualSizeLogic().getVirtualSize(prefVirtualSize, screenSize, ScaleMode.MATCH_X, aspectRatio);
		
		assertEquals(100d, result.x);
		assertEquals(200d, result.y);
	}

	@Test
	public void getVirtualSize_matchXScreenTwiceAsHighAsWide() {
		LVector prefVirtualSize = new LVector(100, 100);
		LVector screenSize = new LVector(1000, 2000);
		double aspectRatio = 1;
		LVector result = new LVirtualSizeLogic().getVirtualSize(prefVirtualSize, screenSize, ScaleMode.MATCH_X, aspectRatio);
		
		assertEquals(100d, result.x);
		assertEquals(200d, result.y);
	}
	
	@Test
	public void getVirtualSize_matchXScreenHalfAsHighAsWide() {
		LVector prefVirtualSize = new LVector(100, 100);
		LVector screenSize = new LVector(1000, 500);
		double aspectRatio = 1;
		LVector result = new LVirtualSizeLogic().getVirtualSize(prefVirtualSize, screenSize, ScaleMode.MATCH_X, aspectRatio);
		
		assertEquals(100d, result.x);
		assertEquals(50d, result.y);
	}
	
	@Test
	public void getVirtualSize_matchYSimple() {
		LVector prefVirtualSize = new LVector(100, 100);
		LVector screenSize = new LVector(1000, 1000);
		double aspectRatio = 1;
		LVector result = new LVirtualSizeLogic().getVirtualSize(prefVirtualSize, screenSize, ScaleMode.MATCH_Y, aspectRatio);
		
		assertEquals(100d, result.x);
		assertEquals(100d, result.y);
	}
	
	@Test
	public void getVirtualSize_matchYRequestTwiceXAndExtraSizeExists() {
		LVector prefVirtualSize = new LVector(200, 100);
		LVector screenSize = new LVector(2000, 1000);
		double aspectRatio = 1;
		LVector result = new LVirtualSizeLogic().getVirtualSize(prefVirtualSize, screenSize, ScaleMode.MATCH_Y, aspectRatio);
		
		assertEquals(200d, result.x);
		assertEquals(100d, result.y);
	}
	
	@Test
	public void getVirtualSize_matchYRequestTwiceYButNoExtraSizeExists() {
		LVector prefVirtualSize = new LVector(200, 100);
		LVector screenSize = new LVector(1000, 1000);
		double aspectRatio = 1;
		LVector result = new LVirtualSizeLogic().getVirtualSize(prefVirtualSize, screenSize, ScaleMode.MATCH_Y, aspectRatio);
		
		assertEquals(100d, result.x);
		assertEquals(100d, result.y);
	}
	
	@Test
	public void getVirtualSize_matchYPixelsTwiceAsWideAsTheyAreHigh() {
		LVector prefVirtualSize = new LVector(100, 100);
		LVector screenSize = new LVector(1000, 1000);
		double aspectRatio = 0.5;
		LVirtualSizeLogic virtualSizeLogic = new LVirtualSizeLogic();
		LVector result = virtualSizeLogic.getVirtualSize(prefVirtualSize, screenSize, ScaleMode.MATCH_Y, aspectRatio);
		
		assertEquals(50d, result.x);
		assertEquals(100d, result.y);
	}
	
	@Test
	public void getVirtualSize_matchYPixelsHalfAsWideAsTheyAreHigh() {
		LVector prefVirtualSize = new LVector(100, 100);
		LVector screenSize = new LVector(1000, 1000);
		double aspectRatio = 2;
		LVector result = new LVirtualSizeLogic().getVirtualSize(prefVirtualSize, screenSize, ScaleMode.MATCH_Y, aspectRatio);
		
		assertEquals(200d, result.x);
		assertEquals(100d, result.y);
	}

	@Test
	public void getVirtualSize_matchYScreenTwiceAsHighAsWide() {
		LVector prefVirtualSize = new LVector(100, 100);
		LVector screenSize = new LVector(1000, 2000);
		double aspectRatio = 1;
		LVector result = new LVirtualSizeLogic().getVirtualSize(prefVirtualSize, screenSize, ScaleMode.MATCH_Y, aspectRatio);
		
		assertEquals(50d, result.x);
		assertEquals(100d, result.y);
	}
	
	@Test
	public void getVirtualSize_matchYScreenHalfAsHighAsWide() {
		LVector prefVirtualSize = new LVector(100, 100);
		LVector screenSize = new LVector(1000, 500);
		double aspectRatio = 1;
		LVector result = new LVirtualSizeLogic().getVirtualSize(prefVirtualSize, screenSize, ScaleMode.MATCH_Y, aspectRatio);
		
		assertEquals(200d, result.x);
		assertEquals(100d, result.y);
	}

	
	@Test
	public void getVirtualSize_fitBothSimple() {
		LVector prefVirtualSize = new LVector(100, 100);
		LVector screenSize = new LVector(1000, 1000);
		double aspectRatio = 1;
		LVector result = new LVirtualSizeLogic().getVirtualSize(prefVirtualSize, screenSize, ScaleMode.FIT_BOTH, aspectRatio);
		
		assertEquals(100d, result.x);
		assertEquals(100d, result.y);
	}

	@Test
	public void getVirtualSize_fitBothRequestTwiceYAndExtraSizeExists() {
		LVector prefVirtualSize = new LVector(100, 200);
		LVector screenSize = new LVector(1000, 2000);
		double aspectRatio = 1;
		LVector result = new LVirtualSizeLogic().getVirtualSize(prefVirtualSize, screenSize, ScaleMode.FIT_BOTH, aspectRatio);
		
		assertEquals(100d, result.x);
		assertEquals(200d, result.y);
	}

	@Test
	public void getVirtualSize_fitBothRequestTwiceYButNoExtraSizeExists() {
		LVector prefVirtualSize = new LVector(100, 200);
		LVector screenSize = new LVector(1000, 1000);
		double aspectRatio = 1;
		LVector result = new LVirtualSizeLogic().getVirtualSize(prefVirtualSize, screenSize, ScaleMode.FIT_BOTH, aspectRatio);
		
		assertEquals(200d, result.x);
		assertEquals(200d, result.y);
	}
	
	@Test
	public void getVirtualSize_fitBothPixelsTwiceAsHighAsTheyAreWide() {
		LVector prefVirtualSize = new LVector(100, 100);
		LVector screenSize = new LVector(1000, 1000);
		double aspectRatio = 2;
		LVirtualSizeLogic virtualSizeLogic = new LVirtualSizeLogic();
		LVector result = virtualSizeLogic.getVirtualSize(prefVirtualSize, screenSize, ScaleMode.FIT_BOTH, aspectRatio);
		
		assertEquals(200d, result.x);
		assertEquals(100d, result.y);
	}
	
	@Test
	public void getVirtualSize_fitBothPixelsHalfAsHighAsTheyAreWide() {
		LVector prefVirtualSize = new LVector(100, 100);
		LVector screenSize = new LVector(1000, 1000);
		double aspectRatio = 0.5;
		LVector result = new LVirtualSizeLogic().getVirtualSize(prefVirtualSize, screenSize, ScaleMode.FIT_BOTH, aspectRatio);
		
		assertEquals(100d, result.x);
		assertEquals(200d, result.y);
	}

	@Test
	public void getVirtualSize_fitBothScreenTwiceAsHighAsWide() {
		LVector prefVirtualSize = new LVector(100, 100);
		LVector screenSize = new LVector(1000, 2000);
		double aspectRatio = 1;
		LVector result = new LVirtualSizeLogic().getVirtualSize(prefVirtualSize, screenSize, ScaleMode.FIT_BOTH, aspectRatio);
		
		assertEquals(100d, result.x);
		assertEquals(200d, result.y);
	}
	
	@Test
	public void getVirtualSize_fitBothScreenHalfAsHighAsWide() {
		LVector prefVirtualSize = new LVector(100, 100);
		LVector screenSize = new LVector(1000, 500);
		double aspectRatio = 1;
		LVector result = new LVirtualSizeLogic().getVirtualSize(prefVirtualSize, screenSize, ScaleMode.FIT_BOTH, aspectRatio);
		
		assertEquals(200d, result.x);
		assertEquals(100d, result.y);
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Test
	public void getVirtualSize_noneSimple() {
		LVector prefVirtualSize = new LVector(100, 100);
		LVector screenSize = new LVector(1000, 1000);
		double aspectRatio = 1;
		LVector result = new LVirtualSizeLogic().getVirtualSize(prefVirtualSize, screenSize, ScaleMode.NONE, aspectRatio);
		
		assertEquals(100d, result.x);
		assertEquals(100d, result.y);
	}

	@Test
	public void getVirtualSize_noneRequestTwiceYAndExtraSizeExists() {
		LVector prefVirtualSize = new LVector(100, 200);
		LVector screenSize = new LVector(1000, 2000);
		double aspectRatio = 1;
		LVector result = new LVirtualSizeLogic().getVirtualSize(prefVirtualSize, screenSize, ScaleMode.NONE, aspectRatio);
		
		assertEquals(100d, result.x);
		assertEquals(200d, result.y);
	}

	@Test
	public void getVirtualSize_noneRequestTwiceYButNoExtraSizeExists() {
		LVector prefVirtualSize = new LVector(100, 200);
		LVector screenSize = new LVector(1000, 1000);
		double aspectRatio = 1;
		LVector result = new LVirtualSizeLogic().getVirtualSize(prefVirtualSize, screenSize, ScaleMode.NONE, aspectRatio);
		
		assertEquals(100d, result.x);
		assertEquals(200d, result.y);
	}
	
	@Test
	public void getVirtualSize_nonePixelsTwiceAsHighAsTheyAreWide() {
		LVector prefVirtualSize = new LVector(100, 100);
		LVector screenSize = new LVector(1000, 1000);
		double aspectRatio = 2;
		LVirtualSizeLogic virtualSizeLogic = new LVirtualSizeLogic();
		LVector result = virtualSizeLogic.getVirtualSize(prefVirtualSize, screenSize, ScaleMode.NONE, aspectRatio);
		
		assertEquals(100d, result.x);
		assertEquals(100d, result.y);
	}

	@Test
	public void getVirtualSize_nonePixelsHalfAsHighAsTheyAreWide() {
		LVector prefVirtualSize = new LVector(100, 100);
		LVector screenSize = new LVector(1000, 1000);
		double aspectRatio = 0.5;
		LVector result = new LVirtualSizeLogic().getVirtualSize(prefVirtualSize, screenSize, ScaleMode.NONE, aspectRatio);
		
		assertEquals(100d, result.x);
		assertEquals(100d, result.y);
	}

	@Test
	public void getVirtualSize_noneScreenTwiceAsHighAsWide() {
		LVector prefVirtualSize = new LVector(100, 100);
		LVector screenSize = new LVector(1000, 2000);
		double aspectRatio = 1;
		LVector result = new LVirtualSizeLogic().getVirtualSize(prefVirtualSize, screenSize, ScaleMode.NONE, aspectRatio);
		
		assertEquals(100d, result.x);
		assertEquals(100d, result.y);
	}
	
	@Test
	public void getVirtualSize_noneScreenHalfAsHighAsWide() {
		LVector prefVirtualSize = new LVector(100, 100);
		LVector screenSize = new LVector(1000, 500);
		double aspectRatio = 1;
		LVector result = new LVirtualSizeLogic().getVirtualSize(prefVirtualSize, screenSize, ScaleMode.NONE, aspectRatio);
		
		assertEquals(100d, result.x);
		assertEquals(100d, result.y);
	}

	
}
