package com.sunhongkao.fish.adve;

import com.sunhongkao.fish.mons.Cyclops;
import com.sunhongkao.fish.mons.MonsterMgr;


public class RoundAdve13 extends RoundAdve11 {
    @Override
    protected int getEggCost() {
        return 10000;
    }

    @Override
    public void wantMonster() {
        addMonster(new Cyclops(MonsterMgr.HEALTH_NORMAL));
    }
}