package com.imagefiletransformer.utility;

import com.facebook.react.bridge.ReadableMap;
import com.imagefiletransformer.constants.FormatType;
import com.imagefiletransformer.constants.ScaleMode;

import java.util.HashMap;
import java.util.Map;

public class StaticOptions {
    String KEY_WIDTH = "width";
    String KEY_HEIGHT = "height";
    String KEY_SCALE_MODE = "scaleMode";
    String KEY_QUALITY = "quality";
    String KEY_TARGET_FORMAT = "targetFormat";
    // extra key
    String KEY_MIN_DELAY = "minDelay";
    String KEY_MAX_DELAY = "maxDelay";


    private final String TAG = "ReactNative";
    public final String type;
    public int WIDTH = 0;
    public int HEIGHT = 0;
    public int QUALITY = 100;
    public String SCALE_MODE = ScaleMode.FIT_CENTER.getMode();
    public String TARGET_FORMAT = FormatType.JPEG.getType();

    // extra
    public Integer MIN_DELAY = null;
    public Integer MAX_DELAY = null;

    public StaticOptions(String type, ReadableMap options) {
        this.type = type;

        if(options.hasKey(KEY_WIDTH)) this.WIDTH = options.getInt(KEY_WIDTH);
        if(options.hasKey(KEY_HEIGHT)) this.HEIGHT = options.getInt(KEY_HEIGHT);
        if(options.hasKey(KEY_QUALITY)) this.QUALITY = options.getInt(KEY_QUALITY);
        if(options.hasKey(KEY_SCALE_MODE)) this.SCALE_MODE = options.getString(KEY_SCALE_MODE);

        if(options.hasKey(KEY_TARGET_FORMAT)) {
            this.TARGET_FORMAT = options.getString(KEY_TARGET_FORMAT);
        }

        if("animated".equalsIgnoreCase(type)){
            FormatType formatType = FormatType.fromString(this.TARGET_FORMAT);
            switch (formatType){
                case AWEBP:
                case GIF:
                    this.TARGET_FORMAT = formatType.getType();
                    break;
                default:
                    this.TARGET_FORMAT = FormatType.GIF.getType();

            }
        }else{
            FormatType formatType = FormatType.fromString(this.TARGET_FORMAT);
            switch (formatType){
                case PNG:
                case JPEG:
                case WEBP:
                    this.TARGET_FORMAT = formatType.getType();
                    break;
                default:
                    this.TARGET_FORMAT = FormatType.JPEG.getType();
            }
        }

        // extra init
        if(options.hasKey(KEY_MIN_DELAY)) {
            this.MIN_DELAY = options.getInt(KEY_MIN_DELAY);
        }
        if(options.hasKey(KEY_MAX_DELAY)) {
            this.MAX_DELAY = options.getInt(KEY_MIN_DELAY);
        }

    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(KEY_WIDTH, this.WIDTH);
        map.put(KEY_HEIGHT, this.HEIGHT);
        map.put(KEY_QUALITY, this.QUALITY);
        map.put(KEY_SCALE_MODE, this.SCALE_MODE);
        map.put(KEY_TARGET_FORMAT, this.TARGET_FORMAT);
        return map;
    }
}
