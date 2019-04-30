package com.sunhongkao.fish.mons;

import java.util.List;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;

import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.engine.Util;
import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.fish.FishMgr;
import com.sunhongkao.fish.scene.RoundScene;
import com.sunhongkao.fish.stage.Gun;
import com.sunhongkao.fish.stage.StageItem;
import com.sunhongkao.fish.R;


public class Cyclops extends Monster {
    private ActState mActState = new ActState();


    public Cyclops(int health) {
        super(health);

        setSize(SIZE_LARGE, SIZE_LARGE);

        mIdleState.setResIds(R.drawable.ms_cyclops_idle_1600_160);
        mTurnState.setResIds(R.drawable.ms_cyclops_turn_1600_160);
        mIdleState.accept(mActState);

        mSeekAction.speedX = SeekAssist.SPEED_UP;
        mSeekAction.speedY = 0;
        mSeekMax = Integer.MAX_VALUE;

        mActLine = Util.TIMES_SHORT;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        List<StageItem> fishs = SeekAssist.touch(this, FishMgr.getAll());
        for (int i = 0; i < fishs.size(); i++) {
            ((Fish) fishs.get(i)).killed();
        }
    }

    @Override
    public boolean act() {
        return mCurState.tryState(mActState);
    }

    @Override
    public boolean escapeble() {
        return false;
    }

    /*****************************************************************/

    private class ActState extends State {
        private int mPhase = 1;


        public ActState() {
            setResIds(R.drawable.ms_cyclops_act_1600_160);
        }

        @Override
        public void onUpdate() {
            TiledSprite sprite = mSprite;
            int index = sprite.getCurrentTileIndex();

            if (mPhase == 1) {
                if (index + 1 < sprite.getTileCount()) {
                    sprite.setCurrentTileIndex(index + 1);
                } else {
                    mPhase = -1;

                    EnergyBall ball = new EnergyBall();
                    if (mTurned) {
                        ball.setCx(getX() + getWidth());
                    } else {
                        ball.setCx(getX());
                    }
                    ball.setY(getY() + getHeight() / 4);
                    ball.attachSelf();
                }
            } else {
                if (index >= 1) {
                    sprite.setCurrentTileIndex(index - 1);
                } else {
                    mIdleState.start();
                }
            }
        }

        @Override
        public void start() {
            super.start();
            mPhase = 1;
        }
    }

    /******************************************************************/

    public class EnergyBall extends StageItem {
        private float mSpeedX = SeekAssist.SPEED_FAST;
        private float mSpeedY = SeekAssist.SPEED_DOWN;
        private float mDx = -mSpeedX;
        private float mDy = -mSpeedY;


        EnergyBall() {
            setResIds(R.drawable.cp_energy_ball_480_80, 6);

            setSize(SIZE_SMALL, SIZE_SMALL);

            if (!Cyclops.this.mTurned) {
                mDx = mSpeedX;
            }
        }

        @Override
        public void onUpdate() {
            super.onUpdate();

            List<StageItem> touch = SeekAssist.touch(this, FishMgr.getAll());
            for (int i = 0; i < touch.size(); i++) {
                AsEngine.it().playSound(R.raw.sd_explode);
                ((Fish) touch.get(i)).killed();
            }

            touch = SeekAssist.touch(this, MonsterMgr.getAll());
            for (int i = 0; i < touch.size(); i++) {
                AsEngine.it().playSound(R.raw.sd_explode);
                ((Monster) touch.get(i)).harmed(Gun.attackValue(0));
            }

            if (getY() + getHeight() <= 0 || getY() > RoundScene.tankBottom()) {
                detachSelf();
                return;
            }

            if (getCx() <= 0) {
                mDx = mSpeedX;
            } else if (getCx() >= 640) {
                mDx = -mSpeedX;
            }

            changePostion(mDx, mDy);
        }

        @Override
        public boolean onAreaTouched(TouchEvent event, float x, float y) {
            if (event.getAction() != TouchEvent.ACTION_UP) {
                return false;
            }

            if (getCx() < x) {
                mDx = -mSpeedX;
            } else if (getCx() > x) {
                mDx = mSpeedX;
            }

            if (getCy() < y) {
                mDy = -mSpeedY;
            } else if (getCy() > y) {
                mDy = mSpeedY;
            }

            return true;
        }
    }
}