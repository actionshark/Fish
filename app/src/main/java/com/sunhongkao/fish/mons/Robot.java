package com.sunhongkao.fish.mons;

import java.util.List;

import org.andengine.input.touch.TouchEvent;

import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.engine.Util;
import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.fish.FishMgr;
import com.sunhongkao.fish.stage.StageItem;
import com.sunhongkao.fish.R;


public class Robot extends Monster {
    public Robot(int health) {
        super(health);

        setSize(SIZE_LARGE, SIZE_LARGE);

        mIdleState.setResIds(R.drawable.ms_robot_idle_1600_160);
        mTurnState.setResIds(R.drawable.ms_robot_turn_1600_160);

        mSeekAction.speedX = SeekAssist.SPEED_NORMAL;
        mSeekAction.speedY = 0;
        mSeekMax = Integer.MAX_VALUE;

        mActLine = Util.TIMES_SHORT;
    }

    @Override
    public boolean act() {
        for (int i = 0; i < 3; i++) {
            Missile missile = new Missile();
            if (mTurned) {
                missile.setCx(getCx() + getWidth() / 4 + (i - 1) * getWidth() / 16);
            } else {
                missile.setCx(getCx() - getWidth() / 4 + (i - 1) * getWidth() / 16);
            }
            missile.setCy(getCy() - getHeight() / 4);
            missile.attachSelf();
        }

        return true;
    }

    @Override
    public boolean escapeble() {
        return false;
    }

    /*************************************************************************************/

    public class Missile extends StageItem {
        private float mSpeed = SeekAssist.SPEED_DOWN;
        private double mRadian = Math.PI / 8;
        private Fish mTarget;


        Missile() {
            setResIds(R.drawable.cp_missile_1280_80, 16);

            if (Robot.this.mTurned) {
                mSprite.setCurrentTileIndex(1);
            } else {
                mSprite.setCurrentTileIndex(15);
            }

            setSize(SIZE_DOWN, SIZE_DOWN);
        }

        @Override
        public void onUpdate() {
            List<StageItem> fishs = SeekAssist.touch(this, FishMgr.getAll());
            for (int i = 0; i < fishs.size(); i++) {
                if (((Fish) fishs.get(i)).killed()) {
                    AsEngine.it().playSound(R.raw.sd_explode);
                    detachSelf();
                    return;
                }
            }

            int count = mSprite.getTileCount();
            int index = mSprite.getCurrentTileIndex();

            double rotation = mRadian * index;

            if (mTarget == null || !mTarget.hasParent()) {
                mTarget = FishMgr.randFish();
            }

            if (mTarget != null && mTarget.hasParent()) {
                float dx = mTarget.getCx() - getCx();
                float dy = mTarget.getCy() - getCy();

                if (Util.equals(dy, 0f)) {
                    if (dx > 0) {
                        rotation = Math.PI / 2;
                    } else {
                        rotation = Math.PI * 3 / 2;
                    }
                } else {
                    rotation = Math.atan((double) dx / -dy);

                    if (dy > 0) {
                        rotation += Math.PI;
                    } else if (dx < 0) {
                        rotation += Math.PI * 2;
                    }
                }

                int delta = ((int) (rotation / mRadian) - index + count) % count;
                if (delta > 0 && delta < 8) {
                    if (++index >= count) {
                        index = 0;
                    }

                    mSprite.setCurrentTileIndex(index);
                } else if (delta >= 8 && delta < 16) {
                    if (--index < 0) {
                        index = count - 1;
                    }

                    mSprite.setCurrentTileIndex(index);
                }

                rotation = mRadian * index;
            }

            changePostion(mSpeed * (float) Math.sin(rotation),
                    -mSpeed * (float) Math.cos(rotation));
        }

        @Override
        public boolean onAreaTouched(TouchEvent event, float x, float y) {
            return event.getAction() == TouchEvent.ACTION_UP && detachSelf();
        }
    }
}