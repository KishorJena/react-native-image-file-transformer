package com.imagefiletransformer.codec;

import android.graphics.Bitmap;

public interface MyDecoder {

    int getFrameCount();

    int getNextDelay();

    Bitmap getNextFrame();

    int getDelay(int index);

    void advance();

    String getDecoderName();
    Object getDecoder();
}
