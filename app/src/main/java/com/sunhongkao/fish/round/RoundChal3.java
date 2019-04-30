package com.sunhongkao.fish.round;

import org.andengine.util.math.MathUtils;

import com.sunhongkao.fish.engine.Deliver;
import com.sunhongkao.fish.fish.Breed;
import com.sunhongkao.fish.fish.Carn;
import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.fish.FishMgr;
import com.sunhongkao.fish.fish.Guppy;
import com.sunhongkao.fish.fish.Ultra;
import com.sunhongkao.fish.mons.MonsterMgr;
import com.sunhongkao.fish.stage.PropBox;


public class RoundChal3 extends RoundChalBase {
    @Override
    public void onCreate(Deliver deliver) {
        super.onCreate(deliver);

        addActor(new Breed(), 1);
    }

    @Override
    protected int getGunCost() {
        return 5000;
    }

    @Override
    protected int getEggCost() {
        return 99999;
    }

    @Override
    public void wantMonster() {
        int index = MathUtils.random(0, 6);
        addMonster(MonsterMgr.newMonster(index, MonsterMgr.HEALTH_HIGHER));

        index = MathUtils.random(0, 6);
        addMonster(MonsterMgr.newMonster(index, MonsterMgr.HEALTH_HIGHER));
    }

    @Override
    public void openAll() {
        super.openAll();
        open(0, 1, 2, 3, 4, 5);
    }

    @Override
    public void onPropEvent(PropBox propBox) {
        if (propBox == mPropBoxs[0]) {
            addActor(new Breed());
            return;
        }

        if (propBox == mPropBoxs[3]) {
            addActor(new Carn());
            open(4, 5, 6);
            return;
        }

        if (propBox == mPropBoxs[4]) {
            addActor(new Ultra());
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