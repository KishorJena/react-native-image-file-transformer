package com.imagefiletransformer.codec;

import com.imagefiletransformer.utility.StaticOptions;

import java.io.File;

public class EncoderFactory {
    public static MyEncoder getEncoder(File outputFile, StaticOptions options) {
        String type = options.TARGET_FORMAT;

        if ("gif".equalsIgnoreCase(type)) {
            return new MyGifEncoder(outputFile, options);
        } else if ("awebp".equalsIgnoreCase(type) || "webp".equalsIgnoreCase(type)) {
            return new MyWebpEncoder(outputFile, options);
        } else {
            throw new IllegalArgumentException("Unsupported encoding type: " + type);
        }
    }

}
