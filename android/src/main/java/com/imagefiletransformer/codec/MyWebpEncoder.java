package com.imagefiletransformer.codec;

import android.graphics.Bitmap;

import com.imagefiletransformer.utility.StaticOptions;
import com.encoders.webpencoder.codec.WebpBitmapEncoder;

import java.io.File;
import java.io.IOException;


public class MyWebpEncoder implements MyEncoder{
    private Integer maxDelay=null;
    private Integer minDelay=null;
    private WebpBitmapEncoder webpBitmapEncoder;

    private int quality;
    public MyWebpEncoder(File file, StaticOptions options) {

        try {
            this.webpBitmapEncoder = new WebpBitmapEncoder(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.webpBitmapEncoder.setLoops(0);
        this.quality = options.QUALITY;
        this.minDelay = options.MIN_DELAY;
        this.maxDelay = options.MAX_DELAY;
    }




    @Override
    public void addFrame(Bitmap frame, int frameDelay) {
        int delay = frameDelay;
        if (this.minDelay != null && delay < this.minDelay) delay = this.minDelay;
        if (this.maxDelay != null && delay > this.maxDelay) delay = this.maxDelay;


        try {
            webpBitmapEncoder.setDuration(delay);
            webpBitmapEncoder.writeFrame(frame, this.quality);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void close() {
        try {
            webpBitmapEncoder.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
