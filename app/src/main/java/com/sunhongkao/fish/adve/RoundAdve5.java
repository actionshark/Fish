package com.sunhongkao.fish.adve;

import org.andengine.util.math.MathUtils;

import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.fish.FishMgr;
import com.sunhongkao.fish.fish.Guppy;
import com.sunhongkao.fish.mons.Bluester;
import com.sunhongkao.fish.mons.MonsterMgr;
import com.sunhongkao.fish.mons.Yellowster;
import com.sunhongkao.fish.stage.FoodMgr;
import com.sunhongkao.fish.stage.PropBox;


public class RoundAdve5 extends RoundAdve0 {
    @Override
    protected int getEggCost() {
        return 750;
    }

    @Override
    public void wantMonster() {
        if (MathUtils.random(0.5f)) {
            addMonster(new Bluester(MonsterMgr.HEALTH_LOW));
        } else {
            addMonster(new Yellowster(MonsterMgr.HEALTH_LOWER));
        }
    }

    @Override
    public void openAll() {
        super.openAll();
        open(1, 2, 3);
    }

    @Override
    public void onPropEvent(PropBox propBox) {
        if (propBox == mPropBoxs[3]) {
            FoodMgr.enableDrug();
            return;
        }

        super.onPropEvent(propBox);
    }

    @Override
    public void onFishEvent(Fish fish, int event) {
        if (event == FishMgr.EVENT_CHANGE && fish instanceof Guppy
                && ((Guppy) fish).getType() == Guppy.GUPPY_BIG) {

            open(1, 2, 3, 6);
            return;
        }

        super.onFishEvent(fish, event);
    }
}