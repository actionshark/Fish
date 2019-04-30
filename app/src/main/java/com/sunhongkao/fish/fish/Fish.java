package com.sunhongkao.fish.fish;

import org.andengine.entity.sprite.Sprite;
import org.andengine.util.math.MathUtils;

import com.sunhongkao.fish.R;
import com.sunhongkao.fish.engine.AsActivity;
import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.engine.Util;
import com.sunhongkao.fish.iface.IEatee;
import com.sunhongkao.fish.iface.IEater;
import com.sunhongkao.fish.iface.IMover;
import com.sunhongkao.fish.round.RoundBase;
import com.sunhongkao.fish.round.RoundMgr;
import com.sunhongkao.fish.stage.ActorState;
import com.sunhongkao.fish.stage.Money;
import com.sunhongkao.fish.stage.MoneyMgr;
import com.sunhongkao.fish.stage.StageItem;


public abstract class Fish extends ActorState implements IEater, IEatee {
    protected final int[] mHungryLine = new int[]{180, 360, 450};
    protected int mHungryCnt = mHungryLine[2];
    protected float mEatProp = MathUtils.random(0.9f, 1.1f);
    protected final HungryHint mHungryHint = new HungryHint();

    protected int[][] mResIds;

    protected int mMoneyType;
    protected int mMoneyMove = IMover.TYPE_DOWN;
    protected int mPoint = 960;


    public Fish() {
        mTurnState = new TurnState();
        mEatState = new EatState();
        mDieState = new DieState();

        mIdleState.accept(mTurnState, mEatState, mDieState);
        mTurnState.accept(mDieState);
        mEatState.accept(mDieState);

        mActLine = (int) (Util.TIMES_UP * MathUtils.random(0.9f, 1.1f));

        attachChild(mHungryHint);
    }

    @Override
    public boolean attachSelf() {
        if (super.attachSelf()) {
            FishMgr.sAll.add(this);
            FishMgr.onEvent(this, FishMgr.EVENT_ADD);
            return true;
        }

        return false;
    }

    @Override
    public boolean detachSelf() {
        if (super.detachSelf()) {
            FishMgr.sAll.remove(this);
            FishMgr.onEvent(this, FishMgr.EVENT_REMOVE);
            return true;
        }

        return false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (mCurState != mDieState && mCurState != mEatState) {
            mHungryCnt--;

            if (mHungryCnt <= 0) {
                mCurState.tryState(mDieState);
            } else if (mHungryCnt <= mHungryLine[1]) {
                if (mHungryCnt <= mHungryLine[0] && mHungryCnt + 1 > mHungryLine[0]) {
                    reload();
                    FishMgr.onEvent(this, FishMgr.EVENT_HUNGER);
                } else if (mHungryCnt <= mHungryLine[1] &&
                        mHungryCnt + 1 > mHungryLine[1]) {

                    FishMgr.onEvent(this, FishMgr.EVENT_HUNGER);
                }

                if (mCurState == mIdleState) {
                    mSeekAction.seek(wantEat());
                }
            }
        }
    }

    @Override
    protected void onUpdateVertices() {
        super.onUpdateVertices();

        if (mHungryHint != null) {
            mHungryHint.setSize(getWidth(), getHeight());
        }
    }

    @Override
    public void onSeeked(StageItem seeked) {
        if (seeked instanceof IEatee && mCurState.tryState(mEatState)) {
            mEatState.mEatee = (IEatee) seeked;
        }
    }

    protected abstract StageItem wantEat();

    @Override
    public int eat(IEatee eatee) {
        if (eatee == null || !SeekAssist.isTouched(this, (StageItem) eatee)) {
            return 0;
        }

        int point = (int) (eatee.eaten() * mEatProp);
        int count = mHungryCnt;
        mHungryCnt += point;

        if (count <= mHungryLine[0] && mHungryCnt > mHungryLine[0]) {
            reload();
        }

        return point;
    }

    @Override
    public int eaten() {
        int ret = 0;

        if (detachSelf()) {
            AsEngine.it().playSound(R.raw.sd_eat_fish);
            ret = mPoint;
            mPoint = 0;
        }

        return ret;
    }

    public boolean isDying() {
        return hasParent() && (mHungryCnt <= 0 || mCurState == mDieState);
    }

    public boolean killed() {
        if (hasParent()) {
            if (mDieState == null || mDieState.getSprite() == null) {
                detachSelf();
                return true;
            } else {
                if (mCurState.tryState(mDieState)) {
                    AsEngine.it().playSound(R.raw.sd_fish_die);
                    return true;
                }
            }
        }

        return false;
    }

    public void revive() {
        if (isDying()) {
            if (mHungryCnt < mHungryLine[2]) {
                mHungryCnt = mHungryLine[2];
            }

            mIdleState.start();
            reload();
        }
    }

    protected void reload() {
        int index = 0;
        if (mHungryCnt <= mHungryLine[0]) {
            index = 1;
        }

        mIdleState.setResIds(mResIds[index][0]);
        mTurnState.setResIds(mResIds[index][1]);
        mEatState.setResIds(mResIds[index][2]);
        mDieState.setResIds(mResIds[0][3]);
    }

    @Override
    public boolean act() {
        Money money = MoneyMgr.newMoney(mMoneyType, mMoneyMove);
        if (money == null) {
            return false;
        }

        money.setCenter(getCx(), getCy());
        money.attachAfter(this);
        return true;
    }


    private class HungryHint extends Sprite {
        private int mCount = 0;


        HungryHint() {
            super(0, 0, AsActivity.it().getRegion(R.drawable.cp_hungry_hint_80_80),
                    AsActivity.it().getVertexBufferObjectManager());

            setVisible(false);
        }

        @Override
        public void onManagedUpdate(float seconds) {
            super.onManagedUpdate(seconds);

            RoundBase round = RoundMgr.getRound();

            if (round != null && RoundMgr.getRound().hintEnabled() &&
                    Fish.this.hasParent() && mHungryCnt <= mHungryLine[1]) {

                setVisible(-4 < mCount % 8 || mCount % 8 < 4);
                mCount++;
            } else {
                setVisible(false);
            }
        }
    }
}