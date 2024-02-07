package com.imagefiletransformer.codec;

import android.graphics.Bitmap;

public interface MyEncoder {
    void addFrame(Bitmap frame, int delay);

    void close();
}
