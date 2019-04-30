package com.sunhongkao.fish.adve;

import com.sunhongkao.fish.mons.MonsterMgr;
import com.sunhongkao.fish.mons.Robot;


public class RoundAdve8 extends RoundAdve6 {
    @Override
    protected int getEggCost() {
        return 7500;
    }

    @Override
    public void wantMonster() {
        addMonster(new Robot(MonsterMgr.HEALTH_LOW));
    }
}