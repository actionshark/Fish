package com.sunhongkao.fish.engine;

import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


public class PixelTextureSource implements IBitmapTextureAtlasSource {
    private int[] mPixels;
    private int mX;
    private int mY;
    private int mWidth;
    private int mHeight;


    public PixelTextureSource(int[] pixels, int x, int y, int width, int height) {
        mPixels = pixels;
        mX = x;
        mY = y;
        mWidth = width;
        mHeight = height;
    }

    @Override
    public int getTextureHeight() {
        return mHeight;
    }

    @Override
    public int getTextureWidth() {
        return mWidth;
    }

    @Override
    public int getTextureX() {
        return mX;
    }

    @Override
    public int getTextureY() {
        return mY;
    }

    @Override
    public void setTextureHeight(int height) {
        mHeight = height;
    }

    @Override
    public void setTextureWidth(int width) {
        mWidth = width;
    }

    @Override
    public void setTextureX(int x) {
        mX = x;
    }

    @Override
    public void setTextureY(int y) {
        mY = y;
    }

    @Override
    public IBitmapTextureAtlasSource deepCopy() {
        int[] pixels = new int[mWidth * mHeight];
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = mPixels[i];
        }

        return new PixelTextureSource(pixels, mX, mY, mWidth, mHeight);
    }

    @Override
    public Bitmap onLoadBitmap(Config config) {
        return Bitmap.createBitmap(mPixels, mWidth, mHeight, config);
    }
}