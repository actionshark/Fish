package com.sunhongkao.fish.pet;

import org.andengine.entity.sprite.TiledSprite;

import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.engine.Util;
import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.fish.FishMgr;
import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.mons.Monster;
import com.sunhongkao.fish.mons.MonsterMgr;
import com.sunhongkao.fish.stage.Gun;
import com.sunhongkao.fish.stage.StageItem;
import com.sunhongkao.fish.R;


public class Shark extends Pet implements ITurnable {
    private ActState mActState = new ActState();


    public Shark() {
        mIdleState.setResIds(R.drawable.pt_shark_idle_800_80);
        mTurnState = (TurnState) new TurnState().setResIds
                (R.drawable.pt_shark_turn_800_80);
        mIdleState.accept(mTurnState, mActState);

        setSize(SIZE_BIG, SIZE_BIG);
        mActLine = Util.TIMES_LONGER * 4;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (mSeekAction.seekee == null) {
            StageItem monster = SeekAssist.seek(getSeekerX(),
                    getSeekerY(), MonsterMgr.getAll());

            if (monster != null) {
                detachSelf();
                attachSelf();
                mSeekAction.seek(monster);
            }
        }
    }

    @Override
    public void onSeeked(StageItem seeked) {
        if (seeked != null && mCurState.tryState(mActState)) {
            mActState.mSeeked = seeked;
        }
    }

    @Override
    public boolean act() {
        if (FishMgr.showNum() + FishMgr.hideNum() > 1
                && mSeekAction.seekee == null) {

            seek(SeekAssist.seek(this, FishMgr.getAll()));
        }

        return true;
    }


    private class ActState extends State {
        public StageItem mSeeked;


        public ActState() {
            setResIds(R.drawable.pt_shark_act_800_80);
        }

        @Override
        public void onUpdate() {
            super.onUpdate();

            TiledSprite sprite = mSprite;
            int index = sprite.getCurrentTileIndex();

            if (index == sprite.getTileCount() - 1) {
                if (mSeeked instanceof Fish) {
                    AsEngine.it().playSound(R.raw.sd_eat_fish);
                    ((Fish) mSeeked).eaten();
                } else if (mSeeked instanceof Monster) {
                    AsEngine.it().playSound(R.raw.sd_eat_fish);
                    ((Monster) mSeeked).harmed(Gun.attackValue(7));
                }
            } else if (index == 0) {
                mIdleState.start();
            }
        }
    }
}