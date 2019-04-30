package com.sunhongkao.fish.round;

import org.andengine.util.math.MathUtils;

import com.sunhongkao.fish.engine.Deliver;
import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.fish.FishMgr;
import com.sunhongkao.fish.fish.Guppy;
import com.sunhongkao.fish.fish.Potor;
import com.sunhongkao.fish.mons.MonsterMgr;
import com.sunhongkao.fish.stage.FoodMgr;
import com.sunhongkao.fish.stage.PropBox;


public class RoundChal1 extends RoundChalBase {
    @Override
    public void onCreate(Deliver deliver) {
        super.onCreate(deliver);

        addActor(new Guppy(), 1);
        addActor(new Guppy(), 1);
    }

    @Override
    protected int getGunCost() {
        return 1000;
    }

    @Override
    protected int getEggCost() {
        return 10000;
    }

    @Override
    public void wantMonster() {
        int index = MathUtils.random(0, 2);
        addMonster(MonsterMgr.newMonster(index, MonsterMgr.HEALTH_HIGH));
    }

    @Override
    public void openAll() {
        super.openAll();
        open(0, 1, 2, 3, 4, 5);
    }

    @Override
    public void onPropEvent(PropBox propBox) {
        if (propBox == mPropBoxs[0]) {
            addActor(new Guppy());
            return;
        }

        if (propBox == mPropBoxs[3]) {
            FoodMgr.enableDrug();
            return;
        }

        if (propBox == mPropBoxs[4]) {
            addActor(new Potor());
            open(5, 6);
            return;
        }

        super.onPropEvent(propBox);
    }

    @Override
    public void onFishEvent(Fish fish, int event) {
        if (event == FishMgr.EVENT_CHANGE && fish instanceof Guppy) {
            if (((Guppy) fish).getType() == Guppy.GUPPY_MEDIUM) {
                open(0);
                return;
            }

            if (((Guppy) fish).getType() == Guppy.GUPPY_BIG) {
                open(1, 2, 3, 4);
                return;
            }
        }

        super.onFishEvent(fish, event);
    }
}