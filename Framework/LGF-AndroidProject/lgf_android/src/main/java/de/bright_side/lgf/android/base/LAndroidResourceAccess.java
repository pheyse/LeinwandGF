package de.bright_side.lgf.android.base;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.bright_side.lgf.android.model.LAndroidAnimationResourceProperties;
import de.bright_side.lgf.base.LPlatform;
import de.bright_side.lgf.model.LAnimationFrame;
import de.bright_side.lgf.model.LAnimationResource;
import de.bright_side.lgf.model.LFont;
import de.bright_side.lgf.model.LFontResource;
import de.bright_side.lgf.model.LImage;
import de.bright_side.lgf.model.LImageResource;
import de.bright_side.lgf.model.LRawResource;


/**
 * Created by me on 20.03.2015.
 */
public abstract class LAndroidResourceAccess {
    private LPlatform platform;
    private final Resources resources;
    private Context context;
    private static Map<LImageResource, Integer> imageResourceMap = null;
    private static Map<LImageResource, LImage> imageCache = new HashMap<LImageResource, LImage>();
    private static Map<LAnimationResource, List<LAnimationFrame>> animationResourceCache = new HashMap<LAnimationResource, List<LAnimationFrame>>();
    private static Map<LRawResource, List<Integer>> rawResourceMap = null;
    private static Map<LFontResource, Typeface> fontsCache = new HashMap<LFontResource, Typeface>();
    private Map<LFontResource, Typeface> fontResourceMap;

    private static Map<LAnimationResource, LAndroidAnimationResourceProperties> animationResourceMap = null;
//    private static Map<LAnimationResource, Double> animationResourceFrameDurationMap = null;
//    private static Map<LAnimationResource, Double> animationResourceRotation = null;

    public LAndroidResourceAccess(LPlatform platform, Resources resources, Context context){
        this.platform = platform;
        this.resources = resources;
        this.context = context;
        animationResourceMap = createAnimationResourceMap(context);
        imageResourceMap = createImageResourceMap(context);
        rawResourceMap = createRawResourceMap(context);
//        animationResourceFrameDurationMap = createAnimationResourceFrameDurationMap();
//        animationResourceRotation = createAnimationResourceRotationMap();
        fontResourceMap = createFontResourceMap(context);
    }



    public abstract Map<LRawResource, List<Integer>> createRawResourceMap(Context context);
    public abstract Map<LImageResource, Integer> createImageResourceMap(Context context);
    public abstract Map<LAnimationResource, LAndroidAnimationResourceProperties> createAnimationResourceMap(Context context);
//    public abstract Map<LAnimationResource, List<Integer>> createImageAnimationResourceMap();
//    public abstract Map<LAnimationResource, Double> createAnimationResourceFrameDurationMap();
//    public abstract Map<LAnimationResource, Double> createAnimationResourceRotationMap();
//    public abstract Typeface readFontObject(LFontResource font);
    public abstract Map<LFontResource, Typeface> createFontResourceMap(Context context);


    public Map<String, LImage> getIdToImageMap() throws Exception {
        Map<String, LImage> result = new TreeMap<>();
        for (LImageResource resource: imageResourceMap.keySet()){
            LImage image = getImage(resource);
            result.put(image.getId(), image);
        }
        for (LAnimationResource animation: animationResourceMap.keySet()) {
            for (LAnimationFrame i: getAnimationFrames(animation)) {
                result.put(i.getImage().getId(), i.getImage());
            }
        }
        return result;
    }


    public String readRawResourceAsUTF8String(LRawResource resource, int index) throws Exception {
        Integer resourceID = rawResourceMap.get(resource).get(index);
        if (resourceID == null){
            throw new Exception("Unknown resource: " + resource);
        }
        return LAndroidUtil.readRawResourceWithIdAsUTF8String(resources, resourceID.intValue());
    }

    public LImage getImage(LImageResource resource) throws Exception {
        if (imageCache.containsKey(resource)){
            return imageCache.get(resource);
        }
        Integer id = imageResourceMap.get(resource);
        if (id == null){
            platform.getLogger().debug("Unknown image resource: " + resource);
            throw new Exception("Unknown image resource: " + resource);
        }
        LImage result = LAndroidUtil.readImage(resources, id,  resource.name(), 0);
        imageCache.put(resource, result);
        return result;
    }

//    public Typeface getFontObject(LFontResource font){
//        Typeface result = fontsCache.get(font);
//        if (result != null){
//            return result;
//        }
//        result = readFontObject(font);
//        fontsCache.put(font, result);
//        return result;
//    }

    public int getElementsPerResource(LRawResource rawResource){
        return rawResourceMap.get(rawResource).size();
    }

    public String getRawResourceName(Context context, LRawResource rawResource, int index) {
        int id = rawResourceMap.get(rawResource).get(index);
        String name = context.getResources().getResourceName(id);
        int pos = name.lastIndexOf("/");
        if (pos > 0){
            name = name.substring(pos + 1);
        }
        if (name.startsWith("level_")){
            name = name.substring("level_".length());
        }

        return name.replace("_", " ");
    }

//    public List<LAnimationFrame> getAnimationFrames(LAnimationResource resource) throws Exception {
//        List<LAnimationFrame> result = animationResourceCache.get(resource);
//        if (result != null){
//            return result;
//        }
//        result = new ArrayList<LAnimationFrame>();
//
//        double duration = animationResourceFrameDurationMap.get(resource);
//        double rotation = 0;
//        if (animationResourceRotation.get(resource) != null){
//            rotation = animationResourceRotation.get(resource);
//        }
//        int index = 1;
//        for (Integer i: animationResourceMap.get(resource)){
////            result.add(new LAnimationFrame(readImage(i.intValue(), "a:" + resource.name() + ":" + index), duration));
//            result.add(new LAnimationFrame(LAndroidUtil.readImage(resources, i.intValue(), createAnimationID(resource, index), rotation), duration));
//            index ++;
//        }
//
//        animationResourceCache.put(resource, result);
//        return result;
//    }

    public List<LAnimationFrame> getAnimationFrames(LAnimationResource resource) throws Exception {
        List<LAnimationFrame> result = animationResourceCache.get(resource);
        if (result != null){
            return result;
        }
        result = new ArrayList<LAnimationFrame>();

        LAndroidAnimationResourceProperties properties = animationResourceMap.get(resource);

        double duration = properties.getFrameDurationInSeconds();
        int amount = properties.getFrameImages().size();
        double rotation = properties.getRotation();

        for (int i = 0; i < amount; i++){
            int resourceID = properties.getFrameImages().get(i);
            result.add(new LAnimationFrame(LAndroidUtil.readImage(resources, resourceID, createAnimationID(resource, i), rotation), duration));
        }

        animationResourceCache.put(resource, result);
        return result;
    }

    public LFont getFont(LFontResource font) throws Exception {
        LFont result = new LFont();
        result.setFontObject(fontResourceMap.get(font));
        result.setId(font.name());
        return result;
    }

//    public LFont getFont(LFontResource font) throws Exception {
//        LFont result = new LFont();
//        result.setFontObject(getFontObject(font));
//        result.setId(font.name());
//        return result;
//    }

    private String createAnimationID(LAnimationResource resource, int index) {
        return "a:" + resource.name() + ":" + index;
    }

    public Map<String, LFont> getIdToFontMap() throws Exception {
        Map<String, LFont> result = new TreeMap<>();
        for (Map.Entry<LFontResource, Typeface> i: fontResourceMap.entrySet()) {
            LFont font = getFont(i.getKey());
            result.put(font.getId(), font);
        }
        return result;
    }


}
