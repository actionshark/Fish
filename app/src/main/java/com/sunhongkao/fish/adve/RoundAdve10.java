package com.sunhongkao.fish.adve;

import com.sunhongkao.fish.engine.Deliver;
import com.sunhongkao.fish.engine.Util;
import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.fish.FishMgr;
import com.sunhongkao.fish.fish.Guppy;
import com.sunhongkao.fish.fish.Tong;
import com.sunhongkao.fish.mons.MonsterMgr;
import com.sunhongkao.fish.mons.Yellowster;
import com.sunhongkao.fish.stage.PropBox;


public class RoundAdve10 extends RoundAdve0 {
    @Override
    public void onCreate(Deliver deliver) {
        super.onCreate(deliver);

        mMonsterLine = Util.TIMES_LONG * 8;
    }

    @Override
    protected int getEggCost() {
        return 1000;
    }

    @Override
    public void wantMonster() {
        addMonster(new Yellowster(MonsterMgr.HEALTH_DOWN));
    }

    @Override
    public void openAll() {
        super.openAll();
        open(1, 2, 3);
    }

    @Override
    public void onPropEvent(PropBox propBox) {
        if (propBox == mPropBoxs[3]) {
            addActor(new Tong());
            open(6);
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