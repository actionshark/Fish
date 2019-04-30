package com.sunhongkao.fish.stage;

import org.andengine.input.touch.TouchEvent;

import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.iface.IEatee;
import com.sunhongkao.fish.iface.IMover;
import com.sunhongkao.fish.iface.ITypeble;
import com.sunhongkao.fish.scene.RoundScene;
import com.sunhongkao.fish.R;


public class Money extends StageItem implements ITypeble, IEatee, IMover {
    private float mSpeed = SeekAssist.SPEED_SLOW * MoneyMgr.sSpeedScale;
    private int mDelay = 30;

    private int mType = -1;
    private int mMove = IMover.TYPE_DOWN;

    private int mEaten = 1440;


    Money(int type, int move) {
        setType(type);
        setMove(move);
    }

    @Override
    public boolean setType(int type) {
        if (type < MoneyMgr.MONEY_SILVER || type > MoneyMgr.MONEY_BOX) {
            return false;
        }

        if (mType == type) {
            return true;
        }
        mType = type;

        int num = 10;
        if (type == MoneyMgr.MONEY_PEARL_L || type == MoneyMgr.MONEY_PEARL_B) {
            num = 1;
        }

        setResIds(MoneyMgr.RESID[mType], num);

        if (type < MoneyMgr.MONEY_DIAMOND_B) {
            setSize(Actor.SIZE_NORMAL, Actor.SIZE_NORMAL);
        } else {
            setSize(Actor.SIZE_UP, Actor.SIZE_UP);
        }

        return true;
    }

    @Override
    public int getType() {
        return mType;
    }

    @Override
    public void setMove(int move) {
        mMove = move;
    }

    @Override
    public int getMove() {
        return mMove;
    }

    public int getPoint() {
        return MoneyMgr.POINT[mType];
    }

    @Override
    public boolean detachSelf() {
        return super.detachSelf() && MoneyMgr.sAllMoney.remove(this);
    }

    @Override
    public boolean onAreaTouched(TouchEvent event, float x, float y) {
        if (event.getAction() == TouchEvent.ACTION_UP) {
            return collect();
        }

        return false;
    }

    public boolean collect() {
        if (detachSelf()) {
            if (mType < MoneyMgr.MONEY_WORM) {
                AsEngine.it().playSound(R.raw.sd_money_small);
            } else if (mType < MoneyMgr.MONEY_DIAMOND_B) {
                AsEngine.it().playSound(R.raw.sd_money_mid);
            } else {
                AsEngine.it().playSound(R.raw.sd_money_big);
            }

            MoneyMgr.save(MoneyMgr.POINT[mType]);
            return true;
        }

        return false;
    }

    @Override
    public void onUpdate() {
        if (mDelay < 0) {
            detachSelf();
            MoneyMgr.onEvent(this, MoneyMgr.EVENT_LOST);
            return;
        }

        super.onUpdate();

        if (mMove == IMover.TYPE_DOWN) {
            if (getCy() < RoundScene.tankBottom()) {
                changeY(mSpeed);
            } else {
                mDelay--;
            }
        } else if (mMove == IMover.TYPE_UP) {
            if (getCy() > 0) {
                changeY(-mSpeed);
            } else {
                mDelay = -1;
            }
        } else if (mMove == IMover.TYPE_UP_DOWN) {
            if (getCy() > 0) {
                changeY(-mSpeed);
            } else {
                mMove = IMover.TYPE_DOWN;
            }
        }
    }

    @Override
    public int eaten() {
        int ret = 0;

        if (detachSelf()) {
            AsEngine.it().playSound(R.raw.sd_eat_fish);
            ret = mEaten;
            mEaten = 0;
        }

        return ret;
    }
}