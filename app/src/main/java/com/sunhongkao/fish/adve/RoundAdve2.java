package com.sunhongkao.fish.adve;

import com.sunhongkao.fish.fish.Carn;
import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.fish.FishMgr;
import com.sunhongkao.fish.fish.Guppy;
import com.sunhongkao.fish.mons.MonsterMgr;
import com.sunhongkao.fish.mons.Yellowster;
import com.sunhongkao.fish.stage.PropBox;


public class RoundAdve2 extends RoundAdve0 {
    @Override
    protected int getEggCost() {
        return 2000;
    }

    @Override
    public void wantMonster() {
        addMonster(new Yellowster(MonsterMgr.HEALTH_LOWER));
    }

    @Override
    public void openAll() {
        super.openAll();
        open(1, 2, 3, 5);
    }

    @Override
    public void onPropEvent(PropBox propBox) {
        if (propBox == mPropBoxs[3]) {
            addActor(new Carn());
            open(5, 6);
            return;
        }

        super.onPropEvent(propBox);
    }

    @Override
    public void onFishEvent(Fish fish, int event) {
        if (event == FishMgr.EVENT_CHANGE && fish instanceof Guppy
                && ((Guppy) fish).getType() == Guppy.GUPPY_BIG) {

            open(1, 2, 3);
            return;
        }

        super.onFishEvent(fish, event);
    }
}