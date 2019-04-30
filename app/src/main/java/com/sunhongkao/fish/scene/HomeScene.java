package com.sunhongkao.fish.scene;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.math.MathUtils;

import com.sunhongkao.fish.engine.AsActivity;
import com.sunhongkao.fish.engine.AsButton;
import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.Deliver;
import com.sunhongkao.fish.R;

import android.graphics.Color;


public class HomeScene extends BaseScene implements
        IAnimationListener, Runnable {

    private final AnimatedSprite[] mMery = new AnimatedSprite[3];
    private int mMusicId;


    @Override
    public void onCreate(Deliver deliver) {
        deliver.set(KEY_BG, R.drawable.bg_main_640_480);
        super.onCreate(deliver);

        String[] str = AsActivity.it().getStrings(R.string.mode_adve,
                R.string.mode_pet, R.string.mode_chal,
                R.string.mode_four, R.string.setting_title,
                R.string.help_title, R.string.dialog_exit);
        final AsButton[] btns = new AsButton[str.length];

        for (int i = 0; i < str.length; i++) {
            float x, y, w, h, s;

            if (i < 4) {
                x = 465;
                y = 83 + 80.5f * i;
                w = 210;
                h = 68;
                s = 33;
            } else {
                x = -6 + 94 * i;
                y = 415;
                w = 92;
                h = 65;
                s = 26;
            }

            btns[i] = new AsButton() {
                @Override
                public boolean onAreaTouched(TouchEvent event, float x, float y) {
                    if (event.getAction() != TouchEvent.ACTION_UP) {
                        return false;
                    }

                    AsEngine.it().playSound(R.raw.sd_click);

                    if (this == btns[0]) {
                        AsEngine.it().push(new SelectAdveScene());
                    } else if (this == btns[1]) {
                        AsEngine.it().push(new SelectPetsScene());
                    } else if (this == btns[2]) {
                        AsEngine.it().push(new SelectChalScene());
                    } else if (this == btns[3]) {

                    } else if (this == btns[4]) {
                        onMenuClick();
                    } else if (this == btns[5]) {
                        AsEngine.it().push(new HelpScene());
                    } else if (this == btns[6]) {
                        onBackClick();
                    }

                    return true;
                }
            };

            btns[i].setSize(w, h);
            btns[i].setCenter(x, y);
            btns[i].text().setText(str[i]);
            btns[i].text().setTextSize(s);
            btns[i].text().setTextColor(Color.YELLOW);

            attachChild(btns[i]);
        }

        mMery[0] = new AnimatedSprite(139, 196, 55, 25,
                AsActivity.it().getRegions(R.drawable.cp_mery_eye_165_25, 3),
                AsActivity.it().getVertexBufferObjectManager());
        mMery[1] = new AnimatedSprite(153, 229, 29, 18,
                AsActivity.it().getRegions(R.drawable.cp_mery_mouth_116_18, 4),
                AsActivity.it().getVertexBufferObjectManager());
        mMery[2] = new AnimatedSprite(42, 303, 230, 165,
                AsActivity.it().getRegions(R.drawable.cp_mery_tail_1904_165, 8),
                AsActivity.it().getVertexBufferObjectManager());

        for (AnimatedSprite mery : mMery) {
            attachChild(mery);
        }

        mMusicId = AsActivity.it().getRawId("ms_main_" + MathUtils.random(0, 1));
    }

    @Override
    public void onResume(Deliver deliver) {
        super.onResume(deliver);

        animate();

        AsEngine.it().playMusic(mMusicId);
    }

    @Override
    public void onBackClick() {
        AsEngine.it().playSound(R.raw.sd_click);

        Deliver deliver = new Deliver();
        deliver.set(KEY_TITLE, R.string.dialog_hint);
        deliver.set(DialogScene.KEY_MSG, AsActivity.it().
                getString(R.string.dialog_want_exit));
        deliver.set(DialogScene.KEY_BTNS, AsActivity.it().
                getStrings(R.string.dialog_ok, R.string.dialog_no));
        deliver.set(DialogScene.KEY_RSTS, new int[]{Deliver.RST_EXIT});

        AsEngine.it().push(new DialogScene(), deliver);
    }

    private void animate() {
        AsEngine.it().runOnUpdateThread(this, MathUtils.random(2f, 3f));
    }

    @Override
    public void run() {
        if (AsEngine.it().getScene() == this) {
            mMery[MathUtils.random(0, 2)].animate(100, 1, HomeScene.this);
            animate();
        } else {
            AsEngine.it().removeRunnable(this);
        }
    }

    @Override
    public void onAnimationStarted(AnimatedSprite arg0, int arg1) {
    }

    @Override
    public void onAnimationFrameChanged(AnimatedSprite arg0, int arg1, int arg2) {
    }

    @Override
    public void onAnimationLoopFinished(AnimatedSprite arg0, int arg1, int arg2) {
    }

    @Override
    public void onAnimationFinished(AnimatedSprite as) {
        as.setCurrentTileIndex(0);
    }
}