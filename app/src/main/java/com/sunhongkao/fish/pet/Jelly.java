package com.sunhongkao.fish.pet;

import java.util.List;

import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.stage.Money;
import com.sunhongkao.fish.stage.MoneyMgr;
import com.sunhongkao.fish.stage.StageItem;
import com.sunhongkao.fish.R;


public class Jelly extends Pet {
    public Jelly() {
        mIdleState.setResIds(R.drawable.pt_jelly_idle_800_80);
        mSeekAction.speedX = mSeekAction.speedY
                = SeekAssist.SPEED_SLOW;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        List<StageItem> list = SeekAssist.touch(
                this, MoneyMgr.getAll());
        for (int i = 0; i < list.size(); i++) {
            ((Money) list.get(i)).collect();
        }

        if (mSeekAction.seekee == null) {
            seek(SeekAssist.seek(this, MoneyMgr.getAll()));
        }
    }
}