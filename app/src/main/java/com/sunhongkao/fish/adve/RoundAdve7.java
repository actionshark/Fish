package com.sunhongkao.fish.adve;

import com.sunhongkao.fish.mons.Cram;
import com.sunhongkao.fish.mons.MonsterMgr;


public class RoundAdve7 extends RoundAdve6 {
    @Override
    protected int getEggCost() {
        return 5000;
    }

    @Override
    public void wantMonster() {
        addMonster(new Cram(MonsterMgr.HEALTH_LOW));
    }
}