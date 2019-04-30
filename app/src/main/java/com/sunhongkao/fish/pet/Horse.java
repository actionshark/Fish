package com.sunhongkao.fish.pet;

import com.sunhongkao.fish.fish.Breed;
import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.fish.FishMgr;
import com.sunhongkao.fish.fish.Guppy;
import com.sunhongkao.fish.fish.FishMgr.FishListener;
import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.stage.Food;
import com.sunhongkao.fish.stage.FoodMgr;
import com.sunhongkao.fish.R;


public class Horse extends Pet implements ITurnable, FishListener {
    private State mActState = new State() {
        @Override
        public void onUpdate() {
            super.onUpdate();

            int index = mSprite.getCurrentTileIndex();

            if (index == 8) {
                Food food = FoodMgr.newFood(FoodMgr.FOOD_MEDIUM);

                if (mTurned) {
                    food.setCx(getX() + getWidth() * 5f / 6f);
                } else {
                    food.setCx(getX() + getWidth() / 6f);
                }
                food.setCy(getCy());
                food.attachSelf();
            } else if (index == 0) {
                mIdleState.start();
            }
        }
    };


    public Horse() {
        mIdleState.setResIds(R.drawable.pt_horse_idle_800_80);
        mTurnState = (TurnState) new TurnState().setResIds
                (R.drawable.pt_horse_turn_800_80);
        mActState.setResIds(R.drawable.pt_horse_act_800_80);
        mIdleState.accept(mTurnState, mActState);
    }

    @Override
    public boolean attachSelf() {
        if (super.attachSelf()) {
            FishMgr.addListener(this);
            return true;
        }

        return false;
    }

    @Override
    public boolean detachSelf() {
        if (super.detachSelf()) {
            FishMgr.removeListener(this);
            return true;
        }

        return false;
    }

    @Override
    public void onFishEvent(Fish fish, int event) {
        if (event == FishMgr.EVENT_HUNGER) {
            if (fish instanceof Guppy || fish instanceof Breed) {
                mCurState.tryState(mActState);
            }
        }
    }
}