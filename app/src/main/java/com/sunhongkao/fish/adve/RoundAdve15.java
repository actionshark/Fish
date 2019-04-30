package com.sunhongkao.fish.adve;

import com.sunhongkao.fish.engine.Deliver;
import com.sunhongkao.fish.engine.Util;
import com.sunhongkao.fish.fish.Breed;
import com.sunhongkao.fish.fish.Carn;
import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.fish.FishMgr;
import com.sunhongkao.fish.fish.Guppy;
import com.sunhongkao.fish.mons.MonsterMgr;
import com.sunhongkao.fish.mons.Yellowster;
import com.sunhongkao.fish.stage.PropBox;


public class RoundAdve15 extends RoundAdveBase {
    @Override
    public void onCreate(Deliver deliver) {
        super.onCreate(deliver);

        mMonsterLine = Util.TIMES_LONG * 8;

        addActor(new Breed(), 1);
    }

    @Override
    protected int getGunCost() {
        return 5000;
    }

    @Override
    protected int getEggCost() {
        return 3000;
    }

    @Override
    public void wantMonster() {
        addMonster(new Yellowster(MonsterMgr.HEALTH_HIGH));
    }

    @Override
    public void openAll() {
        super.openAll();
        open(0, 1, 2, 3);
    }

    @Override
    public void onPropEvent(PropBox propBox) {
        if (propBox == mPropBoxs[0]) {
            addActor(new Breed());
            return;
        }

        if (propBox == mPropBoxs[3]) {
            addActor(new Carn());
            open(6);
            return;
        }

        super.onPropEvent(propBox);
    }

    @Override
    public void onFishEvent(Fish fish, int event) {
        if (event == FishMgr.EVENT_CHANGE) {
            if (fish instanceof Breed && ((Breed) fish).getType() == Breed.BREED_MEDIUM) {
                open(0);
                return;
            }

            if (fish instanceof Guppy && ((Guppy) fish).getType() == Guppy.GUPPY_BIG) {
                open(1, 2, 3);
                return;
            }
        }

        super.onFishEvent(fish, event);
    }
}