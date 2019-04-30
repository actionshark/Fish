package com.sunhongkao.fish.scene;

import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.shape.IOnTouchListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.math.MathUtils;

import com.sunhongkao.fish.engine.AsActivity;
import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.Deliver;
import com.sunhongkao.fish.engine.Recorder;
import com.sunhongkao.fish.pet.Snail;
import com.sunhongkao.fish.round.RoundMgr;
import com.sunhongkao.fish.stage.StageItem;
import com.sunhongkao.fish.R;


public class SelectPetsScene extends BaseScene implements IOnTouchListener {
    private final Sprite[] mRound = new Sprite[4];
    private final StageItem[] mMark = new StageItem[4];


    @Override
    public void onCreate(Deliver deliver) {
        deliver.set(KEY_BG, R.drawable.bg_lake_200_140);
        deliver.set(KEY_TITLE, R.string.mode_pet);
        super.onCreate(deliver);

        Snail snail = new Snail();
        snail.setCx(MathUtils.random(0, 640));
        snail.setCy(456);
        snail.attachSelf();

        for (int i = 0; i < mRound.length; i++) {
            mRound[i] = new Sprite(i % 2 * 262 + 102, i / 2 * 187 + 85, 180, 135,
                    AsActivity.it().getRegion(AsActivity.it().getDrawId("bg_round_" + i + "_640_480")),
                    AsActivity.it().getVertexBufferObjectManager());
            attachChild(mRound[i]);

            mMark[i] = new StageItem();
            mMark[i].setSize(83, 83);
            mMark[i].setCenter(mRound[i].getX() + mRound[i].getWidth() / 2,
                    mRound[i].getY() + mRound[i].getHeight() / 2);
            mMark[i].attachSelf();
        }
    }

    @Override
    public void onResume(Deliver deliver) {
        super.onResume(deliver);

        for (int i = 0; i < mRound.length; i++) {
            if (Recorder.getRoundPetsState(i) == RoundMgr.STATE_UNLOCKED) {
                mMark[i].setResId(R.drawable.cp_unlock_80_80);
                mRound[i].setOnTouchListener(this);
            } else if (Recorder.getRoundPetsState(i) == RoundMgr.STATE_PASSED) {
                mMark[i].setResId(R.drawable.cp_pass_80_80);
                mRound[i].setOnTouchListener(this);
            } else {
                mMark[i].setResId(R.drawable.cp_lock_80_80);
                mRound[i].setOnTouchListener(null);
            }
        }
    }

    @Override
    public boolean onTouched(ITouchArea area, TouchEvent event, float x, float y) {
        if (event.getAction() != TouchEvent.ACTION_UP) {
            return false;
        }

        for (int i = 0; i < mRound.length; i++) {
            if (mRound[i] == area) {
                AsEngine.it().playSound(R.raw.sd_click);

                Deliver del = new Deliver(Deliver.KEY_INTENT,
                        PetScene.INTENT_ROUND);
                del.set(RoundMgr.KEY_TYPE, RoundMgr.TYPE_PETS);
                del.set(RoundMgr.KEY_INDEX, i);
                AsEngine.it().push(new RoundScene(), del);

                return true;
            }
        }

        return false;
    }
}