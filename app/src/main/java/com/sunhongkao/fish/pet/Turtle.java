package com.sunhongkao.fish.pet;

import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.stage.FoodMgr;
import com.sunhongkao.fish.stage.MoneyMgr;
import com.sunhongkao.fish.R;


public class Turtle extends Pet implements ITurnable {
    public Turtle() {
        mIdleState.setResIds(R.drawable.pt_turtle_idle_800_80);
        mTurnState = (TurnState) new TurnState().setResIds
                (R.drawable.pt_turtle_turn_800_80);
        mIdleState.accept(mTurnState);

        mSeekAction.speedX = mSeekAction.speedY
                = SeekAssist.SPEED_SLOW / 2;
    }

    @Override
    public boolean attachSelf() {
        if (super.attachSelf()) {
            FoodMgr.speedScale(1f / 3f);
            MoneyMgr.speedScale(1f / 3f);
            return true;
        }

        return false;
    }

    @Override
    public boolean detachSelf() {
        if (super.detachSelf()) {
            FoodMgr.speedScale(3);
            MoneyMgr.speedScale(3);
            return true;
        }

        return false;
    }
}