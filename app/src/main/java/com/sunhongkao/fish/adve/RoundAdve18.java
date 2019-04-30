package com.sunhongkao.fish.adve;

import org.andengine.util.math.MathUtils;

import com.sunhongkao.fish.mons.MonsterMgr;


public class RoundAdve18 extends RoundAdve16 {
    @Override
    protected int getEggCost() {
        return 75000;
    }

    @Override
    public void wantMonster() {
        int index = MathUtils.random(0, 2);
        addMonster(MonsterMgr.newMonster(index,
                MonsterMgr.HEALTH_HIGHER));

        index = MathUtils.random(3, 6);
        addMonster(MonsterMgr.newMonster(index,
                MonsterMgr.HEALTH_HIGH));
    }
}