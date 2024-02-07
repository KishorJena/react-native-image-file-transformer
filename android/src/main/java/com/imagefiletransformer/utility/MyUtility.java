package com.imagefiletransformer.utility;

import android.webkit.MimeTypeMap;

import com.bumptech.glide.load.ImageHeaderParser;

public class MyUtility {
    public static boolean isAnimated(ImageHeaderParser.ImageType type) {
        switch (type) {
            case GIF:
            case ANIMATED_AVIF:
            case ANIMATED_WEBP:
                return true;
            default:
                return false;
        }
    }
    public static String getImageExtention(String uri) {
        return MimeTypeMap.getFileExtensionFromUrl(uri);
    }

}
