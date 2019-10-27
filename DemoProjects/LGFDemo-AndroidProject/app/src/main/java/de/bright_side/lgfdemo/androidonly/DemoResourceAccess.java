package de.bright_side.lgfdemo.androidonly;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.bright_side.lgf.android.base.LAndroidResourceAccess;
import de.bright_side.lgf.android.model.LAndroidAnimationResourceProperties;
import de.bright_side.lgf.model.LAnimationResource;
import de.bright_side.lgf.model.LFontResource;
import de.bright_side.lgf.model.LImageResource;
import de.bright_side.lgf.model.LRawResource;
import de.bright_side.lgfdemo.R;
import de.bright_side.lgfdemo.model.DemoAnimation;
import de.bright_side.lgfdemo.model.DemoFont;
import de.bright_side.lgfdemo.model.DemoImage;
import de.bright_side.lgfdemo.model.DemoRawResource;

class DemoResourceAccess extends LAndroidResourceAccess {
    public DemoResourceAccess(DemoPlatform platform, Resources resources, Context context) {
        super(platform, resources, context);
    }

    @Override
    public Map<LRawResource, List<Integer>> createRawResourceMap(Context context) {
        Map<LRawResource, List<Integer>> result = new HashMap<LRawResource, List<Integer>>();
        List<Integer> items = new ArrayList<>();
        items.add(R.raw.info);
        result.put(DemoRawResource.RAW_RESOURCE_TEXT, items);
        return result;
    }

    @Override
    public Map<LImageResource, Integer> createImageResourceMap(Context context) {
        Map<LImageResource, Integer> result = new HashMap<LImageResource, Integer>();
        result.put(DemoImage.STAR_YELLOW, R.drawable.star_yellow);
        result.put(DemoImage.STAR_BLUE, R.drawable.star_blue);
        result.put(DemoImage.SHAPE_CIRCLE, R.drawable.shape_circle);
        result.put(DemoImage.SHAPE_SQUARE, R.drawable.shape_square);
        result.put(DemoImage.SHAPE_TRIANGLE, R.drawable.shape_triangle);
        result.put(DemoImage.TILE_1, R.drawable.tile_1);
        result.put(DemoImage.TILE_2, R.drawable.tile_2);
        result.put(DemoImage.TILE_3, R.drawable.tile_3);
        result.put(DemoImage.TILE_4, R.drawable.tile_4);
        return result;
    }

    @Override
    public Map<LAnimationResource, LAndroidAnimationResourceProperties> createAnimationResourceMap(Context context) {
        Map<LAnimationResource, LAndroidAnimationResourceProperties> result = new HashMap<LAnimationResource, LAndroidAnimationResourceProperties>();
        LAndroidAnimationResourceProperties properties = new LAndroidAnimationResourceProperties();
        properties.setFrameDurationInSeconds(0.1);
        properties.setRotation(0);
        List<Integer> resourceIDs = new ArrayList<>();
        resourceIDs.add(R.drawable.animation_1);
        resourceIDs.add(R.drawable.animation_2);
        resourceIDs.add(R.drawable.animation_3);
        resourceIDs.add(R.drawable.animation_4);
        properties.setFrameImages(resourceIDs);
        result.put(DemoAnimation.ANIMATION_A, properties);
        return result;
    }

    @Override
    public Map<LFontResource, Typeface> createFontResourceMap(Context context) {
        Map<LFontResource, Typeface> result = new TreeMap<LFontResource, Typeface>();
        List<LFontResource> fonts = Arrays.asList(DemoFont.PLAIN, DemoFont.PLAIN_BOLD, DemoFont.MOONHOUSE);
        for (LFontResource i: fonts) {
            try {
                result.put(i, getFontObject(context, i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private Typeface getFontObject(Context context, LFontResource font) {
        Typeface result = null;
        if (context == null){
            throw new RuntimeException("context is null!");
        }
        AssetManager am = context.getAssets();
        if (font == DemoFont.PLAIN) {
            result = Typeface.SANS_SERIF;
        } else if (font == DemoFont.PLAIN_BOLD) {
            result = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        } else if (font == DemoFont.MOONHOUSE){
            result = Typeface.createFromAsset(am, "fonts/moonhouse.ttf");
        }

        return result;
    }


}
