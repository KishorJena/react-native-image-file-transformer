package com.imagefiletransformer.codec;

import android.graphics.Bitmap;

import com.bumptech.glide.integration.webp.decoder.WebpDecoder;

public class MyWebpDecoder implements MyDecoder {
    private final WebpDecoder webpDecoder;
    public MyWebpDecoder(WebpDecoder webpDecoder) {
        this.webpDecoder = webpDecoder;
    }


    @Override
    public int getFrameCount() {
        return webpDecoder.getFrameCount();
    }


    @Override
    public int getNextDelay() {
        return webpDecoder.getNextDelay();
    }

    @Override
    public Bitmap getNextFrame() {
        return webpDecoder.getNextFrame();
    }

    @Override
    public int getDelay(int index) {
        return webpDecoder.getDelay(index);
    }

    @Override
    public void advance() {
        webpDecoder.advance();
    }

    @Override
    public String getDecoderName() {
        return "webpDecoder";
    }

    @Override
    public Object getDecoder() {
        return this.webpDecoder;
    }

}
