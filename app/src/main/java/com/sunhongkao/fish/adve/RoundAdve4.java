package com.sunhongkao.fish.adve;

import com.sunhongkao.fish.mons.Bluester;
import com.sunhongkao.fish.mons.MonsterMgr;
import com.sunhongkao.fish.mons.Yellowster;


public class RoundAdve4 extends RoundAdve2 {
    @Override
    protected int getEggCost() {
        return 5000;
    }

    @Override
    public void wantMonster() {
        addMonster(new Bluester(MonsterMgr.HEALTH_LOW));
        addMonster(new Yellowster(MonsterMgr.HEALTH_LOWER));
    }
}