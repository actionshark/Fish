package com.sunhongkao.fish.pet;

import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.mons.Monster;
import com.sunhongkao.fish.mons.MonsterMgr;
import com.sunhongkao.fish.stage.Gun;
import com.sunhongkao.fish.stage.StageItem;
import com.sunhongkao.fish.R;


public class Sword extends Pet implements ITurnable {
    private Monster mSeeking;


    public Sword() {
        mIdleState.setResIds(R.drawable.pt_sword_idle_800_80);
        mTurnState = (TurnState) new TurnState().setResIds
                (R.drawable.pt_sword_turn_800_80);
        mIdleState.accept(mTurnState);

        mSeekAction.speedX = mSeekAction.speedY
                = SeekAssist.SPEED_FAST;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (mSeeking == null || !mSeeking.hasParent()) {
            mSeeking = null;

            if (mSeekAction.seekee == null) {
                Monster monster = (Monster) SeekAssist.seek(
                        this, MonsterMgr.getAll());
                mSeekAction.seek(monster);
            }
        }
    }

    @Override
    public boolean tryTurn(float dx) {
        if (mSeeking == null) {
            return super.tryTurn(dx);
        }

        return false;
    }

    @Override
    public void onSeeked(StageItem seeked) {
        if (mSeeking == null) {
            if (seeked instanceof Monster) {
                AsEngine.it().playSound(R.raw.sd_attack);

                mSeeking = (Monster) seeked;
                mSeeking.harmed(Gun.attackValue(1));
                mSeekAction.seek(getSeekerX() + (mTurned ? -3 : 3) *
                        mSeekAction.speedX, getSeekerY());
            }
        } else {
            detachSelf();
            attachSelf();

            mSeekAction.seek(mSeeking);
            mSeeking = null;
        }
    }
}