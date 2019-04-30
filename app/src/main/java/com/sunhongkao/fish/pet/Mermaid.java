package com.sunhongkao.fish.pet;

import java.util.List;

import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.engine.Util;
import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.fish.FishMgr;
import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.R;


public class Mermaid extends Pet implements ITurnable {
    private ActState mActState = new ActState();


    public Mermaid() {
        setSize(SIZE_NORMAL, SIZE_NORMAL);

        mIdleState.setResIds(R.drawable.pt_mermaid_idle_800_80);
        mTurnState = (TurnState) new TurnState().setResIds
                (R.drawable.pt_mermaid_turn_800_80);
        mIdleState.accept(mTurnState, mActState);

        mActLine = Util.TIMES_LONG * 2;
        mSeekAction.speedX = mSeekAction.speedY
                = SeekAssist.SPEED_SLOW;
    }

    @Override
    public boolean act() {
        return mCurState.tryState(mActState);
    }

    @Override
    public boolean shouldSeek() {
        return mCurState != mActState && super.shouldSeek();
    }


    private class ActState extends State {
        public ActState() {
            setResIds(R.drawable.pt_mermaid_act_800_80);
        }

        @Override
        public void start() {
            super.start();
            AsEngine.it().playSound(R.raw.sd_maid_work);
        }

        @Override
        public void onUpdate() {
            super.onUpdate();

            int index = mSprite.getCurrentTileIndex();

            if (index == 1) {
                List<Fish> fishs = FishMgr.getAll();

                for (int i = 0; i < fishs.size(); i++) {
                    Fish fish = fishs.get(i);

                    if (fish.hasParent() && !fish.isDying()) {
                        fish.act();
                    }
                }
            } else if (index == 0) {
                mIdleState.start();
            }
        }
    }
}