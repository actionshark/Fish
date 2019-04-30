package com.sunhongkao.fish.adve;

import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.fish.FishMgr;
import com.sunhongkao.fish.fish.Guppy;
import com.sunhongkao.fish.fish.Pearler;
import com.sunhongkao.fish.fish.Tong;
import com.sunhongkao.fish.mons.Cram;
import com.sunhongkao.fish.mons.MonsterMgr;
import com.sunhongkao.fish.stage.PropBox;


public class RoundAdve11 extends RoundAdve10 {
    @Override
    protected int getGunCost() {
        return 2000;
    }

    @Override
    protected int getEggCost() {
        return 3000;
    }

    @Override
    public void wantMonster() {
        addMonster(new Cram(MonsterMgr.HEALTH_UP));
    }

    @Override
    public void openAll() {
        super.openAll();
        open(4, 5);
    }

    @Override
    public void onPropEvent(PropBox propBox) {
        if (propBox == mPropBoxs[3]) {
            addActor(new Tong());
            open(4);
            return;
        }

        if (propBox == mPropBoxs[4]) {
            addActor(new Pearler());
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