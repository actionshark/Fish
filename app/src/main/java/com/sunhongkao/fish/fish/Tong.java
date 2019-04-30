package com.sunhongkao.fish.fish;

import org.andengine.entity.sprite.TiledSprite;

import com.sunhongkao.fish.R;
import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.iface.IMover;
import com.sunhongkao.fish.scene.RoundScene;
import com.sunhongkao.fish.stage.MoneyMgr;
import com.sunhongkao.fish.stage.StageItem;


public class Tong extends Fish {
    private ActState mActState = new ActState();


    public Tong() {
        setSize(SIZE_NORMAL, SIZE_NORMAL);

        mMoneyType = MoneyMgr.MONEY_WORM;
        mMoneyMove = IMover.TYPE_UP;

        mResIds = new int[][]{{
                R.drawable.fs_tong_idle_800_80, 0, R.drawable.fs_tong_eat_800_80,
                R.drawable.fs_tong_die_800_80}, {R.drawable.fs_tong_hungry_idle_800_80,
                0, R.drawable.fs_tong_hungry_eat_800_80}};

        mSeekAction.speedX = SeekAssist.SPEED_FAST;
        mSeekAction.speedY = 0;

        mIdleState.accept(mActState);
        mTurnState.accept(mActState);
        mDieState.accept(mActState);
        mActState.accept(mDieState);

        reload();
    }

    @Override
    protected StageItem wantEat() {
        Guppy down = null;

        for (int i = 0; i < FishMgr.sAll.size(); i++) {
            Fish fish = FishMgr.sAll.get(i);

            if (fish instanceof Guppy && ((Guppy) fish).
                    getType() == Guppy.GUPPY_SMALL) {

                if (down == null || fish.getY() > down.getY()) {
                    down = (Guppy) fish;
                }
            }
        }

        return down;
    }

    @Override
    public boolean shouldSeek() {
        if (mCurState == mActState) {
            return false;
        }

        return super.shouldSeek();
    }

    @Override
    public float getSeekerY() {
        if (mSeekAction.seekee != null && super.getSeekerY() -
                getHeight() * 3 / 2 < mSeekAction.seekee.getCy()) {

            return mSeekAction.seekee.getCy();
        }

        return super.getSeekerY();
    }

    @Override
    public void onSeeked(StageItem seeked) {
        if (seeked instanceof Guppy) {
            mActState.mGuppy = (Guppy) seeked;
            mCurState.tryState(mActState);
        }
    }


    private class ActState extends State {
        private float mSpeed = SeekAssist.SPEED_NORMAL;
        public Guppy mGuppy;


        public ActState() {
            setResIds(R.drawable.fs_tong_eat_800_80);
        }

        @Override
        public void onUpdate() {
            if (mHungryCnt <= mHungryLine[0]) {
                setResIds(R.drawable.fs_tong_hungry_eat_800_80);
            } else {
                setResIds(R.drawable.fs_tong_eat_800_80);
            }

            if (mGuppy == null) {
                if (getCy() + mSpeed >= RoundScene.tankBottom()) {
                    setCy(RoundScene.tankBottom());
                    mIdleState.start();
                } else {
                    changeY(mSpeed);
                }
            } else {
                TiledSprite sprite = mSprite;
                int index = sprite.getCurrentTileIndex();

                if (index >= sprite.getTileCount() - 1) {
                    if (mGuppy != null && mGuppy.hasParent()) {
                        eat(mGuppy);
                    }

                    mGuppy = null;
                } else {
                    float y = getCy() - mSpeed;
                    if (y > mGuppy.getCy()) {
                        changeY(-mSpeed);
                    } else {
                        setCy(mGuppy.getCy());
                    }

                    sprite.setCurrentTileIndex(index + 1);
                }
            }
        }
    }
}