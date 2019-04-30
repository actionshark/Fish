package com.sunhongkao.fish.pet;

import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.R;


public class Tadpole extends Pet implements ITurnable {
    public Tadpole() {
        mIdleState.setResIds(R.drawable.pt_tadpole_idle_800_80);
        mTurnState = (TurnState) new TurnState().setResIds
                (R.drawable.pt_tadpole_turn_800_80);
        mIdleState.accept(mTurnState);
    }

    @Override
    public boolean supportMulti() {
        return false;
    }
}