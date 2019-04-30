package com.sunhongkao.fish.round;

import java.util.ArrayList;
import java.util.List;

import org.andengine.util.math.MathUtils;

import com.sunhongkao.fish.engine.AsActivity;
import com.sunhongkao.fish.engine.Deliver;
import com.sunhongkao.fish.engine.Recorder;
import com.sunhongkao.fish.pet.Pet;
import com.sunhongkao.fish.stage.PropBox;
import com.sunhongkao.fish.stage.StageGift;
import com.sunhongkao.fish.R;


public abstract class RoundPetBase extends RoundBase {
    @Override
    public void onCreate(Deliver deliver) {
        int len = (getMajor() + 1) * 5;
        for (int i = 0; i < len; i++) {
            Recorder.enablePet(i);
        }

        deliver.set(Pet.KEY_MASK, 0);
        super.onCreate(deliver);
    }

    @Override
    public String getName() {
        return AsActivity.it().getString(R.string.mode_pet)
                + " " + (getMajor() + 1);
    }

    protected int getRealEggCost() {
        return getEggCost() * (getPetNum() + 1);
    }

    protected int getPetNum() {
        int num = 0;
        for (int i = 0; i < Pet.PETS.length; ++i) {
            if ((mPetMask & 1 << i) != 0) {
                num++;
            }
        }

        return num;
    }

    @Override
    protected void open(Integer... indexs) {
        for (Integer index : indexs) {
            if (index == 6) {
                if (mPropBoxs[index].getState() == PropBox.STATE_READY) {
                    mPropBoxs[index].setStop(StageGift.STOP_STEP);
                    mPropBoxs[index].setResId(R.drawable.cp_question_80_80);
                    mPropBoxs[index].setPoint(getRealEggCost());
                    mPropBoxs[index].open(false);
                }
            } else {
                super.open(index);
            }
        }
    }

    @Override
    public void onPropEvent(PropBox propBox) {
        if (propBox == mPropBoxs[6]) {
            List<Integer> list = new ArrayList<Integer>();
            int i;

            for (i = 0; i < Pet.PETS.length; i++) {
                if (Recorder.isPetEnabled(i) && ((mPetMask & 1 << i) == 0)) {
                    list.add(i);
                }
            }

            if (list.isEmpty() || getPetNum() >= 5 * (getMajor() + 1)) {
                propBox.close();
                Recorder.setRoundPetsState(mRoundIndex, RoundMgr.STATE_PASSED);
                onFinish();
            } else {
                i = list.get(MathUtils.random(0, list.size() - 1));
                mPetMask |= 1 << i;

                Pet pet = Pet.newPet(i);
                pet.initPosition();
                pet.attachSelf();

                propBox.upgrade();
                propBox.setPoint(getRealEggCost());
            }

            return;
        }

        super.onPropEvent(propBox);
    }
}