package com.sunhongkao.fish.stage;

import org.andengine.util.math.MathUtils;

import com.sunhongkao.fish.engine.SeekAction;
import com.sunhongkao.fish.engine.Util;
import com.sunhongkao.fish.engine.SeekAction.Seeker;
import com.sunhongkao.fish.scene.RoundScene;


public abstract class Actor extends StageItem implements Seeker {
    public static final float SIZE_TINY = 32;
    public static final float SIZE_SMALL = 45;
    public static final float SIZE_DOWN = 58;
    public static final float SIZE_NORMAL = 71;
    public static final float SIZE_UP = 84;
    public static final float SIZE_BIG = 97;
    public static final float SIZE_LARGE = 110;


    protected final SeekAction mSeekAction = new SeekAction(this);

    protected boolean mTurned = false;

    protected int mActLine = 0;
    protected int mActCnt = 0;


    public Actor() {
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (shouldActCnt() && ++mActCnt > mActLine && act()) {
            mActCnt = 0;
        }

        mSeekAction.onUpdate();
    }

    public boolean isTurned() {
        return mTurned;
    }

    @Override
    public void seek(StageItem seekee) {
        mSeekAction.seek(seekee);
    }

    @Override
    public void seek(float cx, float cy) {
        mSeekAction.seek(cx, cy);
    }

    public void setSpeed(float x, float y) {
        mSeekAction.speedX = x;
        mSeekAction.speedY = y;
    }

    @Override
    public void onSeekMove(float dx, float dy) {
        changePostion(dx, dy);
    }

    @Override
    public float getSeekerY() {
        return getCy();
    }

    @Override
    public void onSeeked(StageItem seeked) {
    }

    public boolean canUpDown() {
        return mSeekAction != null && !Util.equals(mSeekAction.speedY, 0);
    }

    protected boolean shouldActCnt() {
        return true;
    }

    protected boolean act() {
        return true;
    }

    public void initPosition() {
        setX(MathUtils.random(0, 640 - getWidth()));

        if (canUpDown()) {
            setY(MathUtils.random(0, RoundScene.tankBottom()));
        } else {
            setCy(RoundScene.tankBottom());
        }
    }
}