package com.imagefiletransformer.codec;

import android.graphics.Bitmap;

import com.bumptech.glide.gifdecoder.StandardGifDecoder;

public class MyGifDecoder implements MyDecoder {
    private final StandardGifDecoder gifDecoder;
    public MyGifDecoder(StandardGifDecoder gifDecoder) {
        this.gifDecoder = gifDecoder;
    }

    @Override
    public int getFrameCount() {
        return gifDecoder.getFrameCount();
    }

    @Override
    public int getNextDelay() {
        return gifDecoder.getNextDelay();
    }

    @Override
    public Bitmap getNextFrame() {
        return gifDecoder.getNextFrame();
    }

    @Override
    public int getDelay(int index) {
        return gifDecoder.getDelay(index);
    }

    @Override
    public void advance() {
        gifDecoder.advance();
    }

    @Override
    public String getDecoderName() {
        return "gifDecoder";
    }

    @Override
    public Object getDecoder() {
        return this.gifDecoder;
    }

}
