package com.sunhongkao.fish.pet;

import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.round.RoundMgr;
import com.sunhongkao.fish.R;


public class Dolphin extends Pet implements ITurnable {
    public Dolphin() {
        mIdleState.setResIds(R.drawable.pt_dolphin_idle_800_80);
        mTurnState = (TurnState) new TurnState().setResIds
                (R.drawable.pt_dolphin_turn_800_80);
        mIdleState.accept(mTurnState);
    }

    @Override
    public boolean attachSelf() {
        if (super.attachSelf()) {
            RoundMgr.getRound().enableHint(true);
            return true;
        }

        return false;
    }

    @Override
    public boolean detachSelf() {
        if (super.detachSelf()) {
            RoundMgr.getRound().enableHint(false);
            return true;
        }

        return false;
    }

    @Override
    public boolean supportMulti() {
        return false;
    }
}