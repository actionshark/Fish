package com.sunhongkao.fish.adve;

import com.sunhongkao.fish.mons.MonsterMgr;
import com.sunhongkao.fish.mons.Octopus;


public class RoundAdve12 extends RoundAdve11 {
    @Override
    protected int getEggCost() {
        return 7500;
    }

    @Override
    public void wantMonster() {
        addMonster(new Octopus(MonsterMgr.HEALTH_NORMAL));
    }
}