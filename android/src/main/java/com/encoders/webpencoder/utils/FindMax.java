package com.encoders.webpencoder.utils;

import com.encoders.webpencoder.webpData.FrameData;

import java.util.List;

public class FindMax {
    public static int getHeight(List<FrameData> framesData) {
        int maxHeight = 0;
        for (FrameData frame : framesData) {
            maxHeight = Math.max(frame.height, maxHeight);
        }
        return maxHeight;
    }

    public static int getWidth(List<FrameData> frames) {
        int maxWidth = 0;
        for (FrameData frame : frames) {
            maxWidth = Math.max(frame.width, maxWidth);
        }
        return maxWidth;
    }
}

