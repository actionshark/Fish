package com.sunhongkao.fish.stage;

import org.andengine.entity.sprite.TiledSprite;


public class StageGift extends StageItem {
    public static final int STOP_FIRST = 1;
    public static final int STOP_LAST = 2;
    public static final int STOP_REPEAT = 3;
    public static final int STOP_STEP = 4;

    private int mStop = STOP_REPEAT;
    private boolean mRunning = false;
    private GiftListener mListener;


    public void resume() {
        mRunning = true;
    }

    public void pause() {
        mRunning = false;
    }

    public void setStop(int stop) {
        mStop = stop;
    }

    public void setCurIndex(int index) {
        TiledSprite sprite = mSprite;

        if (sprite != null) {
            sprite.setCurrentTileIndex(index);
        }
    }

    public int getCurIndex() {
        TiledSprite sprite = mSprite;

        if (sprite != null) {
            return sprite.getCurrentTileIndex();
        }

        return 0;
    }

    public int getTileCount() {
        TiledSprite sprite = mSprite;

        if (sprite != null) {
            return sprite.getTileCount();
        }

        return 0;
    }

    @Override
    public void onUpdate() {
        if (!mRunning) {
            return;
        }

        TiledSprite sprite = mSprite;
        if (sprite == null) {
            return;
        }

        super.onUpdate();

        switch (mStop) {
            case STOP_FIRST:
                if (sprite.getCurrentTileIndex() == 0) {
                    mRunning = false;
                }
                break;

            case STOP_LAST:
                if (sprite.getCurrentTileIndex() + 1 == sprite.getTileCount()) {
                    mRunning = false;
                }
                break;

            case STOP_STEP:
                mRunning = false;
                break;
        }

        GiftListener listener = mListener;
        if (listener != null) {
            listener.onStep(this);
        }
    }

    public void setGiftListener(GiftListener listener) {
        mListener = listener;
    }


    public interface GiftListener {
        public void onStep(StageGift gift);
    }
}