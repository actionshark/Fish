package com.sunhongkao.fish.pet;

import java.util.List;

import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.fish.FishMgr;
import com.sunhongkao.fish.iface.IMover;
import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.mons.Monster;
import com.sunhongkao.fish.mons.MonsterMgr;
import com.sunhongkao.fish.scene.RoundScene;
import com.sunhongkao.fish.stage.Food;
import com.sunhongkao.fish.stage.FoodMgr;
import com.sunhongkao.fish.stage.Money;
import com.sunhongkao.fish.stage.MoneyMgr;
import com.sunhongkao.fish.R;


public class Lamp extends Pet implements ITurnable {
    private float mSpeed = SeekAssist.SPEED_SLOW / 2;


    public Lamp() {
        mIdleState.setResIds(R.drawable.pt_lamp_idle_800_80);
        mTurnState = (TurnState) new TurnState().setResIds
                (R.drawable.pt_lamp_turn_800_80);
        mIdleState.accept(mTurnState);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        float x = getCx();

        List<Food> foods = FoodMgr.getAll();
        for (int i = 0; i < foods.size(); i++) {
            Food food = foods.get(i);

            float tx = food.getCx();
            if (x > tx) {
                food.setCx(tx + mSpeed);
            } else if (x < tx) {
                food.setCx(tx - mSpeed);
            }
        }

        List<Money> moneys = MoneyMgr.getAll();
        for (int i = 0; i < moneys.size(); i++) {
            Money money = moneys.get(i);
            if (money.getMove() == IMover.TYPE_STILL) {
                continue;
            }

            float tx = money.getCx();
            if (x > tx) {
                money.setCx(tx + mSpeed);
            } else if (x < tx) {
                money.setCx(tx - mSpeed);
            }
        }

        if (MonsterMgr.getAll().size() > 0) {
            detachSelf();
            attachSelf();

            List<Fish> list = FishMgr.getAll();
            for (int i = 0; i < list.size(); i++) {
                list.get(i).seek(this);
            }

            Monster monster = MonsterMgr.getAll().get(0);
            float dx = 320 - monster.getCx();
            float dy = RoundScene.tankHeight() / 2 - monster.getCy();

            if (dx < -64) {
                dx = 0;
            } else if (dx > 64) {
                dx = 64;
            } else {
                dx = getCx();
            }

            if (dy < RoundScene.tankHeight() / -10f) {
                dy = 0;
            } else if (dy > RoundScene.tankHeight() / 10) {
                dy = RoundScene.tankHeight();
            } else {
                dy = getCy();
            }

            seek(dx, dy);
        }
    }

    @Override
    public boolean supportMulti() {
        return false;
    }
}