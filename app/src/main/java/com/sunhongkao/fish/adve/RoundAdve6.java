package com.sunhongkao.fish.adve;

import org.andengine.util.math.MathUtils;

import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.fish.FishMgr;
import com.sunhongkao.fish.fish.Guppy;
import com.sunhongkao.fish.fish.Potor;
import com.sunhongkao.fish.mons.Bluester;
import com.sunhongkao.fish.mons.MonsterMgr;
import com.sunhongkao.fish.mons.Yellowster;
import com.sunhongkao.fish.stage.PropBox;


public class RoundAdve6 extends RoundAdve5 {
    @Override
    protected int getEggCost() {
        return 3000;
    }

    @Override
    public void wantMonster() {
        if (MathUtils.random(0.5f)) {
            addMonster(new Bluester(MonsterMgr.HEALTH_DOWN));
        } else {
            addMonster(new Yellowster(MonsterMgr.HEALTH_LOW));
        }
    }

    @Override
    public void openAll() {
        super.openAll();
        open(4, 5);
    }

    @Override
    public void onPropEvent(PropBox propBox) {
        if (propBox == mPropBoxs[4]) {
            addActor(new Potor());
            open(5, 6);
            return;
        }

        super.onPropEvent(propBox);
    }

    @Override
    public void onFishEvent(Fish fish, int event) {
        if (event == FishMgr.EVENT_CHANGE && fish instanceof Guppy
                && ((Guppy) fish).getType() == Guppy.GUPPY_BIG) {

            open(1, 2, 3, 4);
            return;
        }

        super.onFishEvent(fish, event);
    }
}