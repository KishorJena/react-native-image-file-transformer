package com.imagefiletransformer.constants;

import java.util.HashMap;
import java.util.Map;

public enum FormatType {
    JPEG("jpeg"),
    PNG("png"),
    WEBP("webp"),
    AWEBP("awebp"),
    GIF("gif"),
    UNKNOWN("unknown");
    private final String format;

    FormatType(String format) {
        this.format = format.toLowerCase();
    }
    public String getType() {
        return format;
    }

    public static FormatType fromString(String format) {
        for (FormatType imageFormat : values()) {
            if (imageFormat.format.equalsIgnoreCase(format)) {
                return imageFormat;
            }
        }
        return UNKNOWN;
    }

    public static Map<String, String> asMap() {
        Map<String, String> map = new HashMap<>();
        for (FormatType imageFormat : values()) {
            map.put(imageFormat.getType().toUpperCase(), imageFormat.getType().toLowerCase());
        }
        return map;
    }

}
