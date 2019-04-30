package com.sunhongkao.fish.adve;

import com.sunhongkao.fish.engine.Deliver;
import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.fish.FishMgr;
import com.sunhongkao.fish.fish.Guppy;
import com.sunhongkao.fish.stage.PropBox;


public class RoundAdve0 extends RoundAdveBase {
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
        return 150;
    }

    @Override
    public void wantMonster() {
    }

    @Override
    public void openAll() {
        super.openAll();
        open(0);
    }

    @Override
    public void onPropEvent(PropBox propBox) {
        if (propBox == mPropBoxs[0]) {
            addActor(new Guppy());
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
                open(6);
                return;
            }
        }

        super.onFishEvent(fish, event);
    }
}