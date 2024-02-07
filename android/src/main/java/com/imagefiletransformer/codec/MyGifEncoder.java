package com.imagefiletransformer.codec;

import android.graphics.Bitmap;

import com.encoders.gifencoder.AnimatedGifEncoder;
import com.imagefiletransformer.utility.StaticOptions;

import java.io.File;

public class MyGifEncoder implements MyEncoder{
    private Integer maxDelay=null;
    private Integer minDelay=null;
    private AnimatedGifEncoder gifEncoder;


    private int quality = 100;
    private int width = 0;
    private int heigth = 0;
    public MyGifEncoder(File outputFile, StaticOptions options) {
        this.gifEncoder= new AnimatedGifEncoder();
        this.gifEncoder.start(outputFile.getPath());
        this.gifEncoder.setRepeat(0);

        this.quality = options.QUALITY;
        this.minDelay = options.MIN_DELAY;
        this.maxDelay = options.MIN_DELAY;

        this.width = options.WIDTH;
        this.heigth = options.HEIGHT;
    }

    @Override
    public void addFrame(Bitmap frame, int frameDelay) {
        int delay = frameDelay;
        if (this.minDelay != null && delay < this.minDelay) delay = this.minDelay;
        if (this.maxDelay != null && delay > this.maxDelay) delay = this.maxDelay;

        // width height
        if(this.width == 0) this.width = frame.getWidth();
        if(this.heigth == 0) this.heigth = frame.getHeight();

        this.gifEncoder.setSize(this.width, this.heigth);
        // FRAME, Delay
        this.gifEncoder.setDelay(delay);
        this.gifEncoder.setQuality(this.quality);

        this.gifEncoder.addFrame(frame);
    }

    @Override
    public void close() {
        this.gifEncoder.finish();
    }
}


//public class MyGifEncoder implements MyEncoder{
//    private Integer maxDelay=null;
//    private Integer minDelay=null;
//    private final GifEncoder gifEncoder;
//
//
//    private int quality = 100;
//    public MyGifEncoder(File outputFile, StaticOptions options) {
//        this.gifEncoder= new GifEncoder();
//        try {
//            this.gifEncoder.init(options.WIDTH, options.HEIGHT, outputFile.getPath());
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        this.quality = options.QUALITY;
//        this.minDelay = options.MIN_DELAY;
//        this.maxDelay = options.MIN_DELAY;
//
//    }
//
//    @Override
//    public void addFrame(Bitmap frame, int frameDelay) {
//        int delay = frameDelay;
//        if (this.minDelay != null && delay < this.minDelay) delay = this.minDelay;
//        if (this.maxDelay != null && delay > this.maxDelay) delay = this.maxDelay;
//
//        // FRAME, Delay
//        this.gifEncoder.encodeFrame(frame, delay);
//    }
//
//    @Override
//    public void close() {
//        this.gifEncoder.close();
//    }
//}
