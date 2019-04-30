package com.sunhongkao.fish.pet;

import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.Util;
import com.sunhongkao.fish.fish.Guppy;
import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.R;


public class Breeder extends Pet implements ITurnable {
    private State mActState = new State() {
        @Override
        public void onUpdate() {
            super.onUpdate();

            int index = mSprite.getCurrentTileIndex();

            if (index == 0) {
                AsEngine.it().playSound(R.raw.sd_bread_work);

                Guppy guppy = new Guppy();
                guppy.setCenter(getCx(), getY() + getHeight());
                guppy.attachSelf();

                mIdleState.start();
            }
        }
    };


    public Breeder() {
        setSize(SIZE_NORMAL, SIZE_NORMAL);

        mIdleState.setResIds(R.drawable.pt_breeder_idle_800_80);
        mTurnState = (TurnState) new TurnState().setResIds
                (R.drawable.pt_breeder_turn_800_80);
        mActState.setResIds(R.drawable.pt_breeder_act_800_80);
        mIdleState.accept(mTurnState, mActState);

        mActLine = Util.TIMES_LONG;
    }

    @Override
    public boolean act() {
        return mCurState.tryState(mActState);
    }
}