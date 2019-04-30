package com.sunhongkao.fish.mons;

import java.util.List;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;

import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.engine.Util;
import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.fish.FishMgr;
import com.sunhongkao.fish.stage.Gun;
import com.sunhongkao.fish.stage.StageItem;
import com.sunhongkao.fish.R;


public class Octopus extends Monster {
    private ChangeState mChangeState = new ChangeState();

    private int mSleepLine = Util.TIMES_NORMAL;
    private int mSleepCnt = 0;


    public Octopus(int health) {
        super(health);

        mIdleState.setResIds(R.drawable.ms_octopus_idle_1600_160);
        mTurnState.setResIds(R.drawable.ms_octopus_turn_1600_160);
        mIdleState.accept(mChangeState);
        mTurnState.accept(mChangeState);

        setSize(SIZE_LARGE, SIZE_LARGE);

        mSeekMax = 10;
        mSeekAction.speedX = mSeekAction.speedY
                = SeekAssist.SPEED_NORMAL;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (++mSleepCnt > mSleepLine) {
            mSleepCnt = 0;
            mCurState.tryState(mChangeState);
        }

        if (!mChangeState.mSleep) {
            List<StageItem> fishs = SeekAssist.touch(this, FishMgr.getAll());
            for (int i = 0; i < fishs.size(); i++) {
                ((Fish) fishs.get(i)).killed();
            }
        }
    }

    @Override
    public boolean shouldSeek() {
        if (mChangeState.mSleep) {
            return false;
        }

        return super.shouldSeek();
    }

    @Override
    public boolean onAreaTouched(TouchEvent event, float x, float y) {
        if (event.getAction() == TouchEvent.ACTION_UP && mChangeState.mSleep) {
            return harmed(-Gun.attackValue(0));
        }

        return super.onAreaTouched(event, x, y);
    }


    private class ChangeState extends State {
        public boolean mSleep = false;


        public ChangeState() {
            setResIds(R.drawable.ms_octopus_change_1600_160);
        }

        @Override
        public void start() {
            super.start();

            TiledSprite sprite = mSprite;

            if (mSleep) {
                sprite.setCurrentTileIndex(sprite.getTileCount() - 1);
            } else {
                sprite.setCurrentTileIndex(0);
            }
        }

        @Override
        public void onUpdate() {
            TiledSprite sprite = mSprite;
            int index = sprite.getCurrentTileIndex();

            if (mSleep) {
                if (index <= 0) {
                    mIdleState.setResIds(R.drawable.ms_octopus_idle_1600_160);
                    mTurnState.setResIds(R.drawable.ms_octopus_turn_1600_160);

                    mSleep = false;
                    mIdleState.start();
                } else {
                    sprite.setCurrentTileIndex(index - 1);
                }
            } else {
                if (index >= sprite.getTileCount() - 1) {
                    mIdleState.setResIds(R.drawable.ms_octopus_sleep_idle_1600_160);
                    mTurnState.setResIds(R.drawable.ms_octopus_sleep_turn_1600_160);

                    mSleep = true;
                    mIdleState.start();
                } else {
                    sprite.setCurrentTileIndex(index + 1);
                }
            }
        }
    }
}