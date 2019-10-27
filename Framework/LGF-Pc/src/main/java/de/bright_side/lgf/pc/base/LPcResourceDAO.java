package de.bright_side.lgf.pc.base;

import java.awt.Font;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import de.bright_side.lgf.model.LAnimationResource;
import de.bright_side.lgf.model.LFontResource;
import de.bright_side.lgf.model.LImageResource;
import de.bright_side.lgf.model.LRawResource;
import de.bright_side.lgf.pc.model.LPcAnimationResourceProperties;

public interface LPcResourceDAO {
	InputStream getRawResourceInputStream(String path);
	InputStream getImageInputStream(String path);
	Map<LFontResource, Font> createFontResourceMap();
	Map<LAnimationResource, LPcAnimationResourceProperties> createAnimationResourceMap();
	Map<LRawResource, List<PCResourceProperties>> createRawResourceMap();
	Map<LImageResource, String> createImageResourceMap();

}