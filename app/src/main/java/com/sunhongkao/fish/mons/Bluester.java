package com.sunhongkao.fish.mons;

import com.sunhongkao.fish.R;
import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.stage.StageItem;


public class Bluester extends Monster {
    public Bluester(int times) {
        super(times);

        setSize(SIZE_LARGE, SIZE_LARGE);

        mIdleState.setResIds(R.drawable.ms_bluester_idle_1600_160);
        mTurnState.setResIds(R.drawable.ms_bluester_turn_1600_160);

        mSeekAction.speedX = mSeekAction.speedY
                = SeekAssist.SPEED_DOWN;
    }

    @Override
    public void onSeeked(StageItem seekee) {
        if (seekee instanceof Fish) {
            ((Fish) seekee).killed();
        }
    }
}