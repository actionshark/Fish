package com.sunhongkao.fish.adve;

import com.sunhongkao.fish.mons.Cyclops;
import com.sunhongkao.fish.mons.MonsterMgr;
import com.sunhongkao.fish.mons.Octopus;


public class RoundAdve14 extends RoundAdve11 {
    @Override
    protected int getEggCost() {
        return 15000;
    }

    @Override
    public void wantMonster() {
        addMonster(new Octopus(MonsterMgr.HEALTH_NORMAL));
        addMonster(new Cyclops(MonsterMgr.HEALTH_NORMAL));
    }
}