package com.sunhongkao.fish.mons;

import org.andengine.input.touch.TouchEvent;

import android.graphics.Color;

import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.AsText;
import com.sunhongkao.fish.engine.Util;
import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.fish.FishMgr;
import com.sunhongkao.fish.iface.IMover;
import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.scene.RoundScene;
import com.sunhongkao.fish.stage.ActorState;
import com.sunhongkao.fish.stage.Gun;
import com.sunhongkao.fish.stage.Money;
import com.sunhongkao.fish.stage.MoneyMgr;
import com.sunhongkao.fish.R;


public abstract class Monster extends
        ActorState implements ITurnable {

    protected final AsText mHealthText = new AsText();
    protected int mHealth = Gun.attackValue(0) * 50;

    protected int mSeekMax = 30;
    protected int mSeekDelay = 0;

    protected boolean mFinal = false;


    public Monster(int health) {
        mHealth = Gun.attackValue(0) * health;

        mHealthText.setPosition(getWidth() / 2, 0);
        mHealthText.setText(mHealth + "");
        mHealthText.setTextColor(Color.RED);
        mHealthText.setTextSize(25);
        attachChild(mHealthText);

        mTurnState = new TurnState();
        mIdleState.accept(mTurnState);

        mSeekAction.seekProb = 0.01f;
    }

    @Override
    public boolean attachSelf() {
        if (super.attachSelf()) {
            MonsterMgr.sAll.add(this);

            if (Util.equals(mSeekAction.speedY, 0)) {
                MonsterMgr.sDown.add(this);
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean detachSelf() {
        if (super.detachSelf()) {
            MonsterMgr.sAll.remove(this);
            if (Util.equals(mSeekAction.speedY, 0)) {
                MonsterMgr.sDown.remove(this);
            }

            if (!mFinal) {
                Money money = MoneyMgr.newMoney(MoneyMgr.
                        MONEY_DIAMOND_L, IMover.TYPE_DOWN);
                money.setCenter(getCx(), getCy());
                money.attachSelf();
            }

            return true;
        }

        return false;
    }

    public void enableFinal(boolean enable) {
        mFinal = enable;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (mSeekDelay >= mSeekMax) {
            if (mSeekAction.seekee == null) {
                Fish fish = FishMgr.randFish();

                if (fish != null && fish.hasParent()) {
                    mSeekAction.seek(fish);
                    mSeekDelay = 0;
                }
            }
        } else {
            mSeekDelay++;
        }

        mHealthText.setText(mHealth + "");
        if (getY() <= 0) {
            mHealthText.setY(getHeight());
        } else if (getY() + getHeight() >= RoundScene.tankHeight()) {
            mHealthText.setY(0);
        }
    }

    public boolean escapeble() {
        return true;
    }

    public boolean harmed(int value) {
        if (mHealth > 0) {
            if ((mHealth -= value) <= 0) {
                detachSelf();
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean onAreaTouched(TouchEvent event, float x, float y) {
        if (event.getAction() != TouchEvent.ACTION_UP
                || !harmed(Gun.attackValue())) {

            return false;
        }

        AsEngine.it().playSound(R.raw.sd_gun);

        if (!escapeble()) {
            return false;
        }

        if (x > getWidth() / 2) {
            x = getSeekerX() - getWidth();
        } else if (x < getWidth() / 2) {
            x = getSeekerX() + getWidth();
        } else {
            x = getSeekerX();
        }

        if (x < 0) {
            x = 0;
        } else if (x > 640) {
            x = 640;
        }

        if (y > getHeight() / 2) {
            y = getSeekerY() - getHeight();
        } else if (y < getHeight() / 2) {
            y = getSeekerY() + getHeight();
        } else {
            y = getSeekerY();
        }

        if (y < 0) {
            y = 0;
        } else if (y > RoundScene.tankBottom()) {
            y = RoundScene.tankBottom();
        }

        mSeekAction.seek(x, y);
        mSeekDelay = 0;

        return true;
    }
}