package com.example.video.entity;

import java.io.InputStream;

public class Video {
    private String title;
    private InputStream stream;

    private int chunkLength;

    private long length;

    private String contentType;

    public Video(String title, InputStream stream) {
        this.title = title;
        this.stream = stream;
    }

    public Video() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public InputStream getStream() {
        return stream;
    }

    public void setStream(InputStream stream) {
        this.stream = stream;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getChunkLength() {
        return chunkLength;
    }

    public void setChunkLength(int chunkLength) {
        this.chunkLength = chunkLength;
    }
}
