package com.sunhongkao.fish.pet;

import java.util.List;

import org.andengine.input.touch.TouchEvent;

import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.engine.Util;
import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.fish.FishMgr;
import com.sunhongkao.fish.iface.IMover;
import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.scene.RoundScene;
import com.sunhongkao.fish.stage.Money;
import com.sunhongkao.fish.stage.MoneyMgr;
import com.sunhongkao.fish.stage.StageItem;
import com.sunhongkao.fish.R;


public class Toy extends Pet implements ITurnable {
    public Toy() {
        mIdleState.setResIds(R.drawable.pt_toy_idle_800_80);
        mTurnState = (TurnState) new TurnState().setResIds
                (R.drawable.pt_toy_turn_800_80);
        mIdleState.accept(mTurnState);

        mActLine = Util.TIMES_UP;
    }

    @Override
    public boolean act() {
        Bomb bomb = new Bomb();
        bomb.setCenter(getCx(), getCy());
        bomb.attachSelf();

        return true;
    }


    private class Bomb extends StageItem {
        Bomb() {
            setResId(R.drawable.cp_bomb_72_72);
            setSize(SIZE_DOWN, SIZE_DOWN);
        }

        @Override
        public void onUpdate() {
            if (getCy() >= RoundScene.tankBottom()) {
                detachSelf();
                return;
            }

            changeY(SeekAssist.SPEED_DOWN);

            List<StageItem> list = SeekAssist.touch(this, FishMgr.getAll());

            for (int i = 0; i < list.size(); i++) {
                Fish fish = (Fish) list.get(i);

                if (fish.killed()) {
                    AsEngine.it().playSound(R.raw.sd_explode);
                    Money money = MoneyMgr.newMoney(
                            MoneyMgr.MONEY_DIAMOND_L, IMover.TYPE_DOWN);
                    money.setCenter(fish.getCx(), fish.getCy());
                    money.attachSelf();
                }
            }
        }

        @Override
        public boolean onAreaTouched(TouchEvent event, float x, float y) {
            if (event.getAction() == TouchEvent.ACTION_UP && detachSelf()) {
                AsEngine.it().playSound(R.raw.sd_money_mid);
                MoneyMgr.save(150);
                return true;
            }

            return false;
        }
    }
}