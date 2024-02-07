package com.imagefiletransformer.constants;

import java.util.HashMap;
import java.util.Map;

public enum ScaleMode {
    CROP("crop"),
    FIT_CENTER("fit_center"),
    STRETCH("stretch");

    private final String mode;

    ScaleMode(String mode) {
        this.mode = mode.toLowerCase();
    }

    public String getMode() {
        return mode;
    }

    public static ScaleMode fromString(String mode) {
        for (ScaleMode scaleMode : values()) {
            if (scaleMode.mode.equalsIgnoreCase(mode)) {
                return scaleMode;
            }
        }
        // Default to FIT_CENTER if not recognized
        return FIT_CENTER;
    }

    public static Map<String, String> asMap() {
        Map<String, String> map = new HashMap<>();
        for (ScaleMode scaleMode : values()) {
            map.put(scaleMode.getMode().toUpperCase(), scaleMode.getMode().toLowerCase());
        }
        return map;
    }
}