package com.sunhongkao.fish.stage;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.shape.IOnTouchListener;
import org.andengine.input.touch.TouchEvent;

import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.AsText;
import com.sunhongkao.fish.stage.StageGift.GiftListener;
import com.sunhongkao.fish.R;


public class PropBox implements IOnTouchListener {
    public static final int TEXT_MAX = -1;
    public static final int TEXT_BLANK = -2;

    public static final int STATE_READY = 1;
    public static final int STATE_OPENING = 2;
    public static final int STATE_OPEN = 3;
    public static final int STATE_CLOSING = 4;
    public static final int STATE_CLOSED = 5;


    private StageItem mMark;
    private StageGift mProp;
    private StageGift mBox;
    private AsText mText;

    private int mPoint;
    private int mState = STATE_READY;
    private boolean mClickable = true;

    private final List<PropListener> mListeners = new ArrayList<>();


    public PropBox(StageItem mark, StageGift prop, StageGift box, AsText text) {
        mMark = mark;
        mProp = prop;
        mBox = box;
        mText = text;
    }

    public void setStop(int stop) {
        mProp.setStop(stop);
    }

    public void setResId(int resId) {
        setResIds(resId, 1);
    }

    public void setResIds(int resId) {
        setResIds(resId, 10);
    }

    public void setResIds(int resId, int num) {
        mProp.setResIds(resId, num);
    }

    public void setPoint(int point) {
        mPoint = point;

        if (mPoint == TEXT_MAX) {
            mText.setText("MAX");
        } else if (mPoint == TEXT_BLANK) {
            mText.setText("");
        } else {
            mText.setText(mPoint + "");
        }
    }

    public int getPoint() {
        return mPoint;
    }

    public int getCurIndex() {
        return mProp.getCurIndex();
    }

    public void setCurIndex(int index) {
        mProp.setCurIndex(index);
    }

    public int getState() {
        return mState;
    }

    public void open() {
        open(true);
    }

    public void open(boolean resume) {
        if (mState != STATE_READY) {
            return;
        }

        mState = STATE_OPENING;
        AsEngine.it().playSound(R.raw.sd_buy);

        mBox.setGiftListener(new GiftListener() {
            @Override
            public void onStep(StageGift gift) {
                if (gift.getCurIndex() == gift.getTileCount() - 1) {
                    mState = STATE_OPEN;
                    mBox.setGiftListener(null);
                    mBox.setOnTouchListener(PropBox.this);
                }
            }
        });

        mBox.resume();
        if (resume) {
            mProp.resume();
        }
    }

    public void upgrade() {
        if (mState != STATE_OPEN) {
            return;
        }

        mState = STATE_OPENING;
        AsEngine.it().playSound(R.raw.sd_buy);

        mBox.setGiftListener(new GiftListener() {
            @Override
            public void onStep(StageGift gift) {
                if (gift.getCurIndex() == gift.getTileCount() / 2 - 1) {
                    mProp.resume();
                } else if (gift.getCurIndex() == gift.getTileCount() - 1) {
                    mState = STATE_OPEN;
                    mBox.setGiftListener(null);
                }
            }
        });
        mBox.resume();
    }

    public void mark(boolean enable) {
        if (enable) {
            mMark.attachBefore(mProp);
        } else {
            mMark.detachSelf();
        }
    }

    public void close() {
        if (mState != STATE_OPEN) {
            return;
        }

        mState = STATE_CLOSING;
        AsEngine.it().playSound(R.raw.sd_buy);
        setPoint(TEXT_MAX);

        mBox.setGiftListener(new GiftListener() {
            @Override
            public void onStep(StageGift gift) {
                if (gift.getCurIndex() == gift.getTileCount() / 2 - 1) {
                    mState = STATE_CLOSED;
                    mBox.setOnTouchListener(null);
                    mBox.setGiftListener(null);
                    mBox.pause();
                }
            }
        });
        mBox.resume();
    }

    public void enableClick(boolean enable) {
        mClickable = enable;
    }

    @Override
    public boolean onTouched(ITouchArea area, TouchEvent event, float x, float y) {
        if (mState == STATE_OPEN && event.getAction() == TouchEvent.ACTION_UP
                && mClickable && (mMark.hasParent() || MoneyMgr.spend(mPoint))) {

            if (mMark.hasParent()) {
                MoneyMgr.save(mPoint);
            }

            for (int i = 0; i < mListeners.size(); i++) {
                mListeners.get(i).onPropEvent(this);
            }
        }

        return true;
    }

    public void addListener(PropListener listener) {
        mListeners.add(listener);
    }

    public void removeListener(PropListener listener) {
        mListeners.remove(listener);
    }


    public interface PropListener {
        void onPropEvent(PropBox propBox);
    }
}