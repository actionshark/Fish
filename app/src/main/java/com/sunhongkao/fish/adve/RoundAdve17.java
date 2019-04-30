package com.sunhongkao.fish.adve;

import org.andengine.util.math.MathUtils;

import com.sunhongkao.fish.mons.MonsterMgr;


public class RoundAdve17 extends RoundAdve16 {
    @Override
    protected int getEggCost() {
        return 50000;
    }

    @Override
    public void wantMonster() {
        int index = MathUtils.random(3, 5);
        addMonster(MonsterMgr.newMonster(index,
                MonsterMgr.HEALTH_UP));

        addMonster(MonsterMgr.newMonster(6,
                MonsterMgr.HEALTH_UP));
    }
}