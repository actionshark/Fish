package com.sunhongkao.fish.adve;

import com.sunhongkao.fish.mons.Cram;
import com.sunhongkao.fish.mons.MonsterMgr;
import com.sunhongkao.fish.mons.Robot;


public class RoundAdve9 extends RoundAdve6 {
    @Override
    protected int getEggCost() {
        return 10000;
    }

    @Override
    public void wantMonster() {
        addMonster(new Cram(MonsterMgr.HEALTH_LOW));
        addMonster(new Robot(MonsterMgr.HEALTH_LOW));
    }
}