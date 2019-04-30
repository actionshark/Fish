package com.sunhongkao.fish.pet;

import java.util.List;

import org.andengine.input.touch.TouchEvent;

import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.engine.Util;
import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.fish.FishMgr;
import com.sunhongkao.fish.fish.Guppy;
import com.sunhongkao.fish.iface.IMover;
import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.stage.Money;
import com.sunhongkao.fish.stage.MoneyMgr;
import com.sunhongkao.fish.R;


public class Eel extends Pet implements ITurnable {
    private int mMax = 10;


    public Eel() {
        setSize(SIZE_UP, SIZE_UP * 3 / 8);

        mIdleState.setResIds(R.drawable.pt_eel_idle_1600_60);
        mTurnState = (TurnState) new TurnState().setResIds
                (R.drawable.pt_eel_turn_1600_60);
        mIdleState.accept(mTurnState);

        mSeekAction.speedX = mSeekAction.speedY
                = SeekAssist.SPEED_SLOW;

        mActLine = Util.TIMES_LONGER * 3;
    }

    @Override
    public boolean act() {
        mIdleState.setResIds(R.drawable.pt_eel_active_idle_1600_60);
        mTurnState = (TurnState) new TurnState().setResIds
                (R.drawable.pt_eel_active_turn_1600_60);
        return false;
    }

    @Override
    public boolean onAreaTouched(TouchEvent event, float x, float y) {
        if (event.getAction() == TouchEvent.ACTION_UP && mActCnt > mActLine) {
            AsEngine.it().playSound(R.raw.sd_eel_work);

            mActCnt = 0;
            mIdleState.setResIds(R.drawable.pt_eel_idle_1600_60);
            mTurnState = (TurnState) new TurnState().setResIds
                    (R.drawable.pt_eel_turn_1600_60);

            List<Fish> fishs = FishMgr.getAll();
            int all = 0, guppy = 0;

            for (int i = 0; i < fishs.size(); i++) {
                Fish fish = fishs.get(i);
                all++;

                if (guppy < mMax && fish instanceof Guppy) {
                    guppy++;
                    fish.killed();

                    Money money = MoneyMgr.newMoney(
                            MoneyMgr.MONEY_DIAMOND_L, IMover.TYPE_DOWN);
                    money.setCenter(fish.getCx(), fish.getCy());
                    money.attachSelf();
                }
            }

            if (guppy >= all) {
                Fish fish = new Guppy();
                fish.initPosition();
                fish.setY(0);
                fish.attachSelf();
                fish.seek(fish.getCx(), fish.getHeight() * 3);
            }

            return true;
        }

        return false;
    }
}