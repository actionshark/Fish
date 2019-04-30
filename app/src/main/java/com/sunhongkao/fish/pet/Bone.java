package com.sunhongkao.fish.pet;

import com.sunhongkao.fish.engine.Util;
import com.sunhongkao.fish.iface.IMover;
import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.stage.Money;
import com.sunhongkao.fish.stage.MoneyMgr;
import com.sunhongkao.fish.R;


public class Bone extends Pet implements ITurnable {
    public Bone() {
        mIdleState.setResIds(R.drawable.pt_bone_idle_800_80);
        mTurnState = (TurnState) new TurnState().setResIds
                (R.drawable.pt_bone_turn_800_80);
        mIdleState.accept(mTurnState);

        mActLine = Util.TIMES_DOWN;
    }

    @Override
    protected boolean act() {
        Money money = MoneyMgr.newMoney(MoneyMgr.MONEY_GOLD, IMover.TYPE_DOWN);
        money.setCenter(getCx(), getCy());
        money.attachSelf();

        return true;
    }
}