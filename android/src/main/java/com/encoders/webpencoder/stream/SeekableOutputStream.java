package com.encoders.webpencoder.stream;

import java.io.IOException;


public interface SeekableOutputStream {

    void setPosition(int position) throws IOException;
    void write(byte[] bytes, int length) throws IOException;
    void close() throws IOException;
    byte[] getBytes() throws IOException;


}
