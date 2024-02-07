package com.imagefiletransformer.codec;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.bumptech.glide.gifdecoder.StandardGifDecoder;
import com.bumptech.glide.integration.webp.decoder.WebpDecoder;
import com.bumptech.glide.integration.webp.decoder.WebpDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;

import java.lang.reflect.Field;

public class DecoderProvider {
    public static StandardGifDecoder getGifDecoder(GifDrawable resource) throws IllegalAccessException, NoSuchFieldException {

        // 0. STATE
        Drawable.ConstantState state = resource.getConstantState();
        if(state == null){
            return null;
        }

        // 2. FRAME LOADER
        Field frameLoaderField = state.getClass().getDeclaredField("frameLoader");
        frameLoaderField.setAccessible(true);
        // GIF
        Object frameLoaderObject = frameLoaderField.get(state);
        if(frameLoaderObject == null){
            return null;
        }

        // 2. Decoder Gif
        Field decoderField = frameLoaderObject.getClass().getDeclaredField("gifDecoder");
        decoderField.setAccessible(true);
        //
        Object decoderObject =  decoderField.get(frameLoaderObject);

        // final decoder
        StandardGifDecoder decoder = (StandardGifDecoder) decoderObject;

        return decoder;
    }

    public static WebpDecoder getWebpDecoder(WebpDrawable resource) throws IllegalAccessException, NoSuchFieldException {

        // 0. STATE
        Drawable.ConstantState state = resource.getConstantState();
        if(state == null){
            return null;
        }

        // 1. FRAME LOADER
        Field frameLoaderField = state.getClass().getDeclaredField("frameLoader");
        frameLoaderField.setAccessible(true);
        // webp
        Object frameLoaderObject = frameLoaderField.get(state);
        if(frameLoaderObject == null) {
            return null;
        }

        // 2. Decoder WEBP
        Field decoderField = frameLoaderObject.getClass().getDeclaredField("webpDecoder");
        decoderField.setAccessible(true);
        //
        Object decoderObject = decoderField.get(frameLoaderObject);

        // final decoder
        WebpDecoder decoder = (WebpDecoder) decoderObject;

        return decoder;
    }

}
