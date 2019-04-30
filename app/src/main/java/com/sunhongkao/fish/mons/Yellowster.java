package com.sunhongkao.fish.mons;

import java.util.List;

import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.fish.FishMgr;
import com.sunhongkao.fish.stage.StageItem;
import com.sunhongkao.fish.R;


public class Yellowster extends Monster {
    public Yellowster(int health) {
        super(health);

        mIdleState.setResIds(R.drawable.ms_yellowster_idle_1600_160);
        mTurnState.setResIds(R.drawable.ms_yellowster_turn_1600_160);

        setSize(SIZE_LARGE, SIZE_LARGE);

        mSeekAction.speedX = mSeekAction.speedY
                = SeekAssist.SPEED_DOWN;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        List<StageItem> fishs = SeekAssist.touch(this, FishMgr.getAll());
        for (int i = 0; i < fishs.size(); i++) {
            ((Fish) fishs.get(i)).killed();
        }
    }
}