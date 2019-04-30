package com.sunhongkao.fish.mons;

import org.andengine.input.touch.TouchEvent;

import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.fish.FishMgr;
import com.sunhongkao.fish.iface.IEatee;
import com.sunhongkao.fish.iface.IEater;
import com.sunhongkao.fish.stage.Food;
import com.sunhongkao.fish.stage.FoodMgr;
import com.sunhongkao.fish.stage.StageItem;
import com.sunhongkao.fish.R;


public class Cram extends Monster implements IEater {
    private int mEatLine = 30;


    public Cram(int health) {
        super(health);

        setSize(SIZE_LARGE, SIZE_LARGE);

        mEatLine = health;

        mIdleState.setResIds(R.drawable.ms_cram_idle_1600_160);
        mTurnState.setResIds(R.drawable.ms_cram_turn_1600_160);

        mEatState = (EatState) new EatState().setResIds(
                R.drawable.ms_cram_eat_1600_160);
        mIdleState.accept(mEatState);
        mTurnState.accept(mEatState);

        mSeekAction.speedX = mSeekAction.speedY
                = SeekAssist.SPEED_DOWN;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (mCurState != mEatState) {
            StageItem si = SeekAssist.seek(this, SeekAssist
                    .touch(this, FoodMgr.getAll()));

            if (si == null) {
                si = SeekAssist.seek(this, SeekAssist.
                        touch(this, FishMgr.getAll()));
            }

            if (si != null && mCurState.tryState(mEatState)) {
                mEatState.mEatee = (IEatee) si;
                mSeekAction.seeking = false;
                mSeekDelay = 0;
            }
        }
    }

    @Override
    public boolean shouldSeek() {
        if (mCurState == mEatState) {
            return false;
        }

        return super.shouldSeek();
    }

    @Override
    public float getSeekerX() {
        return getCx();
    }

    @Override
    public float getSeekerY() {
        return getY() + getHeight() / 4;
    }

    @Override
    public int eat(IEatee eatee) {
        int point = 0;
        int time = 0;

        if (eatee != null && (point = eatee.eaten()) > 0) {
            if (eatee instanceof Fish) {
                time = 4;
            } else if (eatee instanceof Food) {
                time = ((Food) eatee).getType() + 1;
            } else {
                time = 1;
            }
        }

        if ((mEatLine -= time) <= 0) {
            detachSelf();
        }

        return point;
    }

    @Override
    public boolean onAreaTouched(TouchEvent event, float x, float y) {
        if (mFinal) {
            return super.onAreaTouched(event, x, y);
        }

        return false;
    }
}