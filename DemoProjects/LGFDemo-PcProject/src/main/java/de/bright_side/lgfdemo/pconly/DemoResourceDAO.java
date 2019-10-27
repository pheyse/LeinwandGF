package de.bright_side.lgfdemo.pconly;

import java.awt.Font;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.bright_side.lgf.model.LAnimationResource;
import de.bright_side.lgf.model.LFontResource;
import de.bright_side.lgf.model.LImageResource;
import de.bright_side.lgf.model.LRawResource;
import de.bright_side.lgf.pc.base.LPcResourceDAO;
import de.bright_side.lgf.pc.base.PCResourceProperties;
import de.bright_side.lgf.pc.model.LPcAnimationResourceProperties;
import de.bright_side.lgfdemo.model.DemoAnimation;
import de.bright_side.lgfdemo.model.DemoFont;
import de.bright_side.lgfdemo.model.DemoImage;
import de.bright_side.lgfdemo.model.DemoRawResource;

public class DemoResourceDAO implements LPcResourceDAO{

	@Override
	public Map<LImageResource, String> createImageResourceMap() {
		Map<LImageResource, String> result = new TreeMap<LImageResource, String>();
		result.put(DemoImage.STAR_YELLOW, "imgs/star_yellow.png");
		result.put(DemoImage.STAR_BLUE, "imgs/star_blue.png");
		result.put(DemoImage.SHAPE_SQUARE, "imgs/shape_square.png");
		result.put(DemoImage.SHAPE_CIRCLE, "imgs/shape_circle.png");
		result.put(DemoImage.SHAPE_TRIANGLE, "imgs/shape_triangle.png");
		result.put(DemoImage.TILE_1, "imgs/tile_1.png");
		result.put(DemoImage.TILE_2, "imgs/tile_2.png");
		result.put(DemoImage.TILE_3, "imgs/tile_3.png");
		result.put(DemoImage.TILE_4, "imgs/tile_4.png");
		return result;
	}

	@Override
	public Map<LRawResource, List<PCResourceProperties>> createRawResourceMap() {
		Map<LRawResource, List<PCResourceProperties>> result = new HashMap<LRawResource, List<PCResourceProperties>>();
		
		List<PCResourceProperties> propertiesList = new ArrayList<PCResourceProperties>();
		PCResourceProperties item = new PCResourceProperties("info", "raw/info.txt");
		propertiesList.add(item);
		result.put(DemoRawResource.RAW_RESOURCE_TEXT, propertiesList);
		
		return result;
	}

	@Override
	public Map<LAnimationResource, LPcAnimationResourceProperties> createAnimationResourceMap() {
		Map<LAnimationResource, LPcAnimationResourceProperties> result = new HashMap<LAnimationResource, LPcAnimationResourceProperties>();
		LPcAnimationResourceProperties properties = new LPcAnimationResourceProperties();
		properties.setFrameDurationInSeconds(0.1);
		properties.setRotation(0);
		ArrayList<String> paths = new ArrayList<String>();
		paths.addAll(createPaths("imgs", "animation_", ".png", 1, 4));
		paths.addAll(createPaths("imgs", "animation_", ".png", 4, 1));
		properties.setFrameImagePaths(paths);
		result.put(DemoAnimation.ANIMATION_A, properties);
		return result;
	}

	private List<String> createPaths(String path, String namePrefix, String nameSuffix, int first, int last) {
		List<String> result = new ArrayList<String>();
		int inc = 1;
		if (first > last) {
			inc = -1;
		}
		for (int i = first; i != last; i += inc) {
			result.add(path + "/" + namePrefix + i + nameSuffix);
		}
		
		return result;
	}

	@Override
	public Map<LFontResource, Font> createFontResourceMap() {
		Map<LFontResource, Font> result = new TreeMap<LFontResource, Font>();
		List<LFontResource> fonts = Arrays.asList(DemoFont.PLAIN, DemoFont.PLAIN_BOLD, DemoFont.MOONHOUSE);
		for (LFontResource i: fonts) {
			try {
				result.put(i, getFontObject(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	private Font getFontObject(LFontResource font) throws Exception {
		Font result = null;
		if (font == DemoFont.PLAIN) {
			result = new Font("sansserif", Font.PLAIN, 24);
		} else if (font == DemoFont.PLAIN_BOLD) {
			result = new Font("sansserif", Font.PLAIN, 24).deriveFont(Font.BOLD);
		} else if (font == DemoFont.MOONHOUSE) {
			result = loadFont("fonts/moonhouse.ttf");
		}
		return result;
	}
	
	private Font loadFont(String path) throws Exception {
		InputStream inputStream = null;
		try {
			inputStream = getClass().getClassLoader().getResourceAsStream(path);
			if (inputStream == null) {
				throw new Exception("Could not get input stream for internal file '" + path + "'");
			}
			return Font.createFont(Font.TRUETYPE_FONT, inputStream);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	@Override
	public InputStream getImageInputStream(String path) {
		return getClass().getClassLoader().getResourceAsStream(path);
	}

	@Override
	public InputStream getRawResourceInputStream(String path) {
		return getClass().getClassLoader().getResourceAsStream(path);
	}

}
