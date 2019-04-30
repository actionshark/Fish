package com.sunhongkao.fish.adve;

import com.sunhongkao.fish.R;
import com.sunhongkao.fish.engine.AsActivity;
import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.Deliver;
import com.sunhongkao.fish.engine.Recorder;
import com.sunhongkao.fish.pet.Pet;
import com.sunhongkao.fish.round.RoundBase;
import com.sunhongkao.fish.round.RoundMgr;
import com.sunhongkao.fish.scene.BaseScene;
import com.sunhongkao.fish.scene.DialogScene;
import com.sunhongkao.fish.scene.HatchScene;
import com.sunhongkao.fish.stage.PropBox;


public abstract class RoundAdveBase extends RoundBase {
    @Override
    public int getMajor() {
        return mRoundIndex / 5;
    }

    public int getMinor() {
        return mRoundIndex % 5;
    }

    @Override
    public String getName() {
        return (getMajor() + 1) + "-" + (getMinor() + 1);
    }

    @Override
    public void onPropEvent(PropBox propBox) {
        if (propBox == mPropBoxs[6] && propBox.getCurIndex() >= 2) {
            AsEngine.it().playMusic(R.raw.ms_win, false);
            Recorder.setRoundAdveState(mRoundIndex, RoundMgr.STATE_PASSED);

            if (Recorder.getRoundAdveState(mRoundIndex + 1) == RoundMgr.STATE_LOCKED) {
                Recorder.setRoundAdveState(mRoundIndex + 1, RoundMgr.STATE_UNLOCKED);
            }

            if (getMinor() == 4) {
                if (Recorder.getRoundPetsState(getMajor()) == RoundMgr.STATE_LOCKED) {
                    Recorder.setRoundPetsState(getMajor(), RoundMgr.STATE_UNLOCKED);
                }

                if (Recorder.getRoundChalState(getMajor()) == RoundMgr.STATE_LOCKED) {
                    Recorder.setRoundChalState(getMajor(), RoundMgr.STATE_UNLOCKED);
                }
            }

            if (Recorder.isPetEnabled(mRoundIndex)) {
                BaseScene scene = new DialogScene();

                Deliver deliver = new Deliver();
                deliver.set(BaseScene.KEY_TITLE, R.string.dialog_hint);
                deliver.set(DialogScene.KEY_MSG, AsActivity.it().
                        getString(R.string.dialog_completed));
                deliver.set(DialogScene.KEY_BTNS, AsActivity.it().
                        getStrings(R.string.dialog_ok));
                deliver.set(DialogScene.KEY_RSTS, new int[]{Deliver.RST_EXIT,
                        Deliver.RST_EXIT});

                AsEngine.it().push(scene, deliver);
            } else {
                Recorder.enablePet(mRoundIndex);
                AsEngine.it().pop();
                AsEngine.it().push(new HatchScene(), new Deliver(
                        Pet.KEY_INDEX, mRoundIndex));
            }

            return;
        }

        super.onPropEvent(propBox);
    }
}