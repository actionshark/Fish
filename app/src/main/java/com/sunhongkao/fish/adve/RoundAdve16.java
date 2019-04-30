package com.sunhongkao.fish.adve;

import com.sunhongkao.fish.fish.Carn;
import com.sunhongkao.fish.fish.Ultra;
import com.sunhongkao.fish.mons.DualHeads;
import com.sunhongkao.fish.mons.MonsterMgr;
import com.sunhongkao.fish.stage.PropBox;


public class RoundAdve16 extends RoundAdve15 {
    @Override
    protected int getEggCost() {
        return 25000;
    }

    @Override
    public void wantMonster() {
        addMonster(new DualHeads(MonsterMgr.HEALTH_HIGH));
    }

    @Override
    public void openAll() {
        super.openAll();
        open(4, 5);
    }

    @Override
    public void onPropEvent(PropBox propBox) {
        if (propBox == mPropBoxs[3]) {
            addActor(new Carn());
            open(4, 5, 6);
            return;
        }

        if (propBox == mPropBoxs[4]) {
            addActor(new Ultra());
            return;
        }

        super.onPropEvent(propBox);
    }
}