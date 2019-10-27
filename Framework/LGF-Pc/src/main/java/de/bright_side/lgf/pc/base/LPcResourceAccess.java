package de.bright_side.lgf.pc.base;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import de.bright_side.lgf.model.LAnimationFrame;
import de.bright_side.lgf.model.LAnimationResource;
import de.bright_side.lgf.model.LFont;
import de.bright_side.lgf.model.LFontResource;
import de.bright_side.lgf.model.LImage;
import de.bright_side.lgf.model.LImageResource;
import de.bright_side.lgf.model.LRawResource;
import de.bright_side.lgf.model.LVector;
import de.bright_side.lgf.pc.model.LPcAnimationResourceProperties;

public class LPcResourceAccess {
	private static final boolean LOGGING_ENABLED = true;
	
    private Map<LImageResource, String> imageResourceMap;
    private Map<LRawResource, List<PCResourceProperties>> rawResourceMap;
    private Map<LAnimationResource, LPcAnimationResourceProperties> animationResourceMap;
    private Map<LFontResource, Font> fontResourceMap;

    private Map<LAnimationResource, List<LAnimationFrame>> animationResourceCache = new HashMap<LAnimationResource, List<LAnimationFrame>>();
    private Map<LImageResource, LImage> imageCache = new HashMap<LImageResource, LImage>();
	private LPcInstance instance;
    
	public LPcResourceAccess(LPcInstance instance) {
		this.instance = instance;
		
		imageResourceMap = instance.createImageResourceMap();
		rawResourceMap = instance.createRawResourceMap();
		animationResourceMap = instance.createAnimationResourceMap();
		fontResourceMap = instance.createFontResourceMap();
		
		log("consctructor: all resources created");
	}
	

	private void log(String message) {
		if (LOGGING_ENABLED) {
			System.out.println("LPcResourceAccess> " + message);
		}
	}


	public int getElementsPerResource(LRawResource rawResource){
		return rawResourceMap.get(rawResource).size();
	}
	

	public LImage getImage(LImageResource resource) throws Exception {
        if (imageCache.containsKey(resource)){
            return imageCache.get(resource);
        }

		String path = imageResourceMap.get(resource);
		if (path == null){
			throw new Exception("Unknown image resource: " + resource);
		}
		LImage result = readImage(path, resource.name(), 0);
		imageCache.put(resource, result);
		return result;
	}
	
	private LImage readImage(String path, String id, double rotation) throws Exception{
        InputStream inputStream = null;
        try{
            inputStream = instance.getImageInputStream(path);
            if (inputStream == null){
                throw new Exception("Could not get input stream for internal file '" + path + "'");
            }
            LImage result = new LImage();
            BufferedImage nativeImage = ImageIO.read(inputStream);
            
            if (rotation != 0) {
            	nativeImage = rotate(nativeImage, rotation);
            }
            
            result.setImageObject(nativeImage);
            result.setSize(new LVector(nativeImage.getWidth(), nativeImage.getHeight()));
            result.setId(id);
            return result;
        } catch (Exception e){
            throw new Exception("Could not read image file", e);
        } finally {
            if (inputStream != null){
                inputStream.close();
            }
        }
	}
	
	private BufferedImage rotate(BufferedImage src, double rotation) {
		AffineTransform affineTransform = AffineTransform.getRotateInstance(Math.toRadians(rotation), src.getWidth() / 2, src.getHeight() / 2);
		BufferedImage rotatedImage = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
		Graphics2D g = (Graphics2D) rotatedImage.getGraphics();
		g.setTransform(affineTransform);
		g.drawImage(src,  0,  0,  null);
		return rotatedImage;
	}

	public String getRawResourceName(LRawResource rawResource, int index) {
		return rawResourceMap.get(rawResource).get(index).getName();
	}

	public String readRawResourceAsUTF8String(LRawResource rawResource, int index) throws Exception{
		InputStream inputStream = null;
		PCResourceProperties properties = rawResourceMap.get(rawResource).get(index);
		
		String path = properties.getPath();
		try{
			inputStream = instance.getRawResourceInputStream(path);
			if (inputStream == null){
				throw new Exception("Could not get input stream for internal file '" + path + "'");
			}
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			final int BUFFER = 2048;
			int count;
			byte data[] = new byte[BUFFER];
			while ((count = inputStream.read(data, 0, BUFFER)) != -1) {
				outputStream.write(data, 0, count);
			}
			return new String(outputStream.toByteArray(), "UTF-8");
		} finally {
			if (inputStream != null){
				inputStream.close();
			}
		}
	}
	
	public LFont getFont(LFontResource font) throws Exception {
		LFont result = new LFont();
		result.setFontObject(fontResourceMap.get(font));
		result.setId(font.name());
		return result;
	}

	
    public List<LAnimationFrame> getAnimationFrames(LAnimationResource resource) throws Exception {
        List<LAnimationFrame> result = animationResourceCache.get(resource);
        if (result != null){
            return result;
        }
        result = new ArrayList<LAnimationFrame>();
        
        LPcAnimationResourceProperties properties = animationResourceMap.get(resource);

        double duration = properties.getFrameDurationInSeconds();
        int amount = properties.getFrameImagePaths().size();
        double rotation = properties.getRotation();
        
        for (int i = 0; i < amount; i++){
        	String path = properties.getFrameImagePaths().get(i);
            result.add(new LAnimationFrame(readImage(path, createAnimationID(resource, i), rotation), duration));
        }

        animationResourceCache.put(resource, result);
        return result;
    }
    
	private String createAnimationID(LAnimationResource resource, int index) {
		return "a:" + resource.name() + ":" + index;
	}

	
	public Map<String, LImage> getIdToImageMap() throws Exception {
		Map<String, LImage> result = new TreeMap<>();
		for (Entry<LImageResource, String> i: imageResourceMap.entrySet()) {
			LImage image = getImage(i.getKey());
			result.put(image.getId(), image);
		}
		for (LAnimationResource animation: animationResourceMap.keySet()) {
			for (LAnimationFrame i: getAnimationFrames(animation)) {
				result.put(i.getImage().getId(), i.getImage());
			}
		}
		return result;
	}

	public Map<String, LFont> getIdToFontMap() throws Exception {
		Map<String, LFont> result = new TreeMap<>();
		for (Entry<LFontResource, Font> i: fontResourceMap.entrySet()) {
			LFont font = new LFont();
			font.setFontObject(i.getValue());
			font.setId(i.getKey().name());
			result.put(font.getId(), font);
		}
		return result;
	}

	

	
	
	

}
