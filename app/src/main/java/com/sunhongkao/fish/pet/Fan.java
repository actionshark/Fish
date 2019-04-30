package com.sunhongkao.fish.pet;

import java.util.List;

import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.iface.IMover;
import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.scene.RoundScene;
import com.sunhongkao.fish.stage.Food;
import com.sunhongkao.fish.stage.FoodMgr;
import com.sunhongkao.fish.stage.Money;
import com.sunhongkao.fish.stage.MoneyMgr;
import com.sunhongkao.fish.stage.StageItem;
import com.sunhongkao.fish.R;


public class Fan extends Pet implements ITurnable {
    public Fan() {
        mIdleState.setResIds(R.drawable.pt_fan_idle_800_80);
        mTurnState = (TurnState) new TurnState().setResIds
                (R.drawable.pt_fan_turn_800_80);
        mIdleState.accept(mTurnState);

        mSeekAction.speedX = mSeekAction.speedY
                = SeekAssist.SPEED_FAST;
    }

    public void onUpdate() {
        super.onUpdate();

        List<StageItem> touch = SeekAssist.touch(this, FoodMgr.getAll());
        int i;
        for (i = 0; i < touch.size(); i++) {
            if (((IMover) touch.get(i)).getMove() == IMover.TYPE_DOWN) {
                ((IMover) touch.get(i)).setMove(IMover.TYPE_UP_DOWN);
            }
        }

        touch = SeekAssist.touch(this, MoneyMgr.getAll());
        for (i = 0; i < touch.size(); i++) {
            if (((IMover) touch.get(i)).getMove() == IMover.TYPE_DOWN) {
                ((IMover) touch.get(i)).setMove(IMover.TYPE_UP_DOWN);
            }
        }

        if (mSeekAction.seekee != null && ((IMover) mSeekAction.seekee)
                .getMove() == IMover.TYPE_DOWN) {
            return;
        }

        IMover target = null;
        float ty = RoundScene.tankBottom() / 2;

        List<Food> foods = FoodMgr.getAll();
        for (i = 0; i < foods.size(); i++) {
            Food tmp = foods.get(i);

            if (tmp.getMove() == IMover.TYPE_DOWN && tmp.getCy() > ty) {
                ty = tmp.getCy();
                target = tmp;
            }
        }

        List<Money> moneys = MoneyMgr.getAll();
        for (i = 0; i < moneys.size(); i++) {
            Money tmp = moneys.get(i);

            if (tmp.getMove() == IMover.TYPE_DOWN && tmp.getCy() > ty) {
                ty = tmp.getCy();
                target = tmp;
            }
        }

        if (target instanceof StageItem) {
            seek((StageItem) target);
        }

        if (mSeekAction.seekee != null && ((IMover) mSeekAction.seekee)
                .getMove() != IMover.TYPE_DOWN) {

            mSeekAction.seekee = null;
            mSeekAction.seeking = false;
        }
    }
}