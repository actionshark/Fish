package com.sunhongkao.fish.scene;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.IOnTouchListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.math.MathUtils;

import android.graphics.Color;

import com.sunhongkao.fish.engine.AsActivity;
import com.sunhongkao.fish.engine.AsButton;
import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.Deliver;
import com.sunhongkao.fish.engine.Recorder;
import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.fish.Victim;
import com.sunhongkao.fish.round.RoundMgr;
import com.sunhongkao.fish.R;


public class SelectAdveScene extends BaseScene implements
        IOnTouchListener, IOnSceneTouchListener {

    private final AsButton[] mRound = new AsButton[21];
    private final Fish[] mFishs = new Fish[2];


    @Override
    public void onCreate(Deliver deliver) {
        deliver.set(KEY_BG, R.drawable.bg_lake_200_140);
        deliver.set(KEY_TITLE, R.string.mode_adve);
        super.onCreate(deliver);

        for (int i = 0; i < mFishs.length; i++) {
            mFishs[i] = new Victim("fs_guppy", true);
            mFishs[i].setCx(MathUtils.random(0f, 640f));
            mFishs[i].setCy(MathUtils.random(0f, 480f));
            mFishs[i].attachSelf();
        }

        for (int i = 0; i < mRound.length; i++) {
            int major = i / 5 + 1;
            int minor = i % 5 + 1;

            mRound[i] = new AsButton();
            mRound[i].setRegion(AsActivity.it().getRegion(
                    R.drawable.cp_round_select_adve_100_73));
            mRound[i].setSize(96, 72);
            mRound[i].setCenter(122 * minor - 47, 82 * major + 20);
            mRound[i].text().setText(major + "-" + minor);
            mRound[i].text().setTextSize(30);

            attachChild(mRound[i]);
        }

        mRound[20].setX(mRound[17].getX());
        mRound[20].text().setText("5");

        setOnSceneTouchListener(this);
    }

    @Override
    public void onResume(Deliver deliver) {
        super.onResume(deliver);

        for (int i = 0; i < mRound.length; i++) {
            switch (Recorder.getRoundAdveState(i)) {
                case RoundMgr.STATE_PASSED:
                    mRound[i].text().setTextColor(Color.BLUE);
                    mRound[i].setOnTouchListener(this);
                    break;

                case RoundMgr.STATE_UNLOCKED:
                    mRound[i].text().setTextColor(Color.RED);
                    mRound[i].setOnTouchListener(this);
                    break;

                default:
                    mRound[i].text().setTextColor(Color.GRAY);
                    mRound[i].setOnTouchListener(null);
                    break;
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

                Deliver del = new Deliver(Deliver.KEY_INTENT, PetScene.INTENT_ROUND);
                del.set(RoundMgr.KEY_TYPE, RoundMgr.TYPE_ADVE);
                del.set(RoundMgr.KEY_INDEX, i);
                AsEngine.it().push(new PetScene(), del);

                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onSceneTouchEvent(Scene scene, TouchEvent event) {
        if (scene == this && event.getAction() == TouchEvent.ACTION_DOWN) {
            mFishs[MathUtils.random(0, 1)].seek(event.getX(), event.getY());
            return true;
        }

        return false;
    }
}