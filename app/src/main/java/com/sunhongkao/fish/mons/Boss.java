package com.sunhongkao.fish.mons;

import com.sunhongkao.fish.engine.AsActivity;
import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.Deliver;
import com.sunhongkao.fish.engine.Recorder;
import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.engine.Util;
import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.round.RoundMgr;
import com.sunhongkao.fish.scene.BaseScene;
import com.sunhongkao.fish.scene.DialogScene;
import com.sunhongkao.fish.stage.StageItem;
import com.sunhongkao.fish.R;


public class Boss extends Monster {
    public Boss(int health) {
        super(health);

        setSize(SIZE_LARGE, SIZE_LARGE);

        mIdleState.setResIds(R.drawable.ms_boss_idle_1600_160);
        mTurnState.setResIds(R.drawable.ms_boss_turn_1600_160);

        mSeekAction.seekProb = 0.04f;
        mSeekAction.speedX = mSeekAction.speedY
                = SeekAssist.SPEED_DOWN;

        mSeekMax = Util.TIMES_LONGER * 10;
    }

    @Override
    public void onSeeked(StageItem seekee) {
        if (seekee instanceof Fish) {
            ((Fish) seekee).killed();
        }
    }

    @Override
    public boolean detachSelf() {
        if (super.detachSelf()) {
            Recorder.setRoundAdveState(20, RoundMgr.STATE_PASSED);

            BaseScene scene = new DialogScene();

            Deliver deliver = new Deliver();
            deliver.set(BaseScene.KEY_TITLE, R.string.dialog_hint);
            deliver.set(DialogScene.KEY_MSG, AsActivity.it().
                    getString(R.string.dialog_completed));
            deliver.set(DialogScene.KEY_BTNS, AsActivity.it().
                    getStrings(R.string.dialog_ok));
            deliver.set(DialogScene.KEY_RSTS, new int[]{Deliver.RST_EXIT, Deliver.RST_EXIT});

            AsEngine.it().push(scene, deliver);

            return true;
        }

        return false;
    }

    @Override
    public boolean escapeble() {
        return false;
    }
}