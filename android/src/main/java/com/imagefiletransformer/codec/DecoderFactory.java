package com.imagefiletransformer.codec;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.bumptech.glide.gifdecoder.StandardGifDecoder;
import com.bumptech.glide.integration.webp.decoder.WebpDecoder;
import com.bumptech.glide.integration.webp.decoder.WebpDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;

import java.lang.reflect.Field;

public class DecoderFactory {
    private static String TAG = DecoderFactory.class.getSimpleName();

    private MyDecoder decoder;

    public static MyDecoder getDecoder(Object resource) throws NoSuchFieldException, IllegalAccessException {
        if(resource instanceof GifDrawable){
            return new MyGifDecoder(getGifDecoder((GifDrawable)resource));
        }else if (resource instanceof WebpDrawable){
            return new MyWebpDecoder(getWebpDecoder((WebpDrawable) resource));
        }else{
            Log.w(TAG, "Throwing Error ... getConstantState");
            throw new RuntimeException("Could not set the getConstantState");
        }
    }
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
