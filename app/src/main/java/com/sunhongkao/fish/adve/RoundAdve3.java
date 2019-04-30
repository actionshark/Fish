package com.sunhongkao.fish.adve;

import org.andengine.util.math.MathUtils;

import com.sunhongkao.fish.mons.Bluester;
import com.sunhongkao.fish.mons.MonsterMgr;
import com.sunhongkao.fish.mons.Yellowster;


public class RoundAdve3 extends RoundAdve2 {
    @Override
    protected int getEggCost() {
        return 3000;
    }

    @Override
    public void wantMonster() {
        if (MathUtils.random(0.5f)) {
            addMonster(new Bluester(MonsterMgr.HEALTH_LOW));
        } else {
            addMonster(new Yellowster(MonsterMgr.HEALTH_LOWER));
        }
    }
}