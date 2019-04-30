package com.sunhongkao.fish.pet;

import java.util.List;

import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.engine.Util;
import com.sunhongkao.fish.mons.Monster;
import com.sunhongkao.fish.mons.MonsterMgr;
import com.sunhongkao.fish.stage.Gun;
import com.sunhongkao.fish.stage.StageItem;
import com.sunhongkao.fish.R;


public class Crab extends Pet {
    private State mActState = new State().setResIds
            (R.drawable.pt_crab_act_800_80);
    private float mDx = 0;


    public Crab() {
        mIdleState.setResIds(R.drawable.pt_crab_idle_800_80);
        mIdleState.accept(mActState);
        mActState.accept(mIdleState);

        mSeekAction.speedX = SeekAssist.SPEED_NORMAL;
        mSeekAction.speedY = 0;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (mSeekAction.seekee == null) {
            Monster monster = (Monster) SeekAssist.seek(
                    this, MonsterMgr.getDown());
            if (monster == null) {
                monster = (Monster) SeekAssist.seek(
                        this, MonsterMgr.getAll());
            }

            mSeekAction.seek(monster);
        }

        if (Util.equals(mDx, 0)) {
            mCurState.tryState(mActState);
        } else {
            mCurState.tryState(mIdleState);
            mDx = 0;
        }

        if (mCurState.getSprite().getCurrentTileIndex() == 0) {
            List<StageItem> list = SeekAssist.touch(this, MonsterMgr.getAll());
            for (int i = 0; i < list.size(); i++) {
                AsEngine.it().playSound(R.raw.sd_attack);
                ((Monster) list.get(i)).harmed(Gun.attackValue(3));
            }
        }
    }

    @Override
    public void onSeekMove(float dx, float dy) {
        super.onSeekMove(dx, dy);
        mDx = dx;
    }
}