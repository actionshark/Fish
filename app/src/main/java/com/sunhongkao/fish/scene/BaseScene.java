package com.sunhongkao.fish.scene;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;

import android.graphics.Color;

import com.sunhongkao.fish.engine.AsActivity;
import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.AsText;
import com.sunhongkao.fish.engine.Deliver;
import com.sunhongkao.fish.R;


public abstract class BaseScene extends Scene {
    public static final String KEY_BG = "basescene_background";
    public static final String KEY_TITLE = "basescene_title";


    protected Deliver mDeliver;

    protected Sprite mBg;
    protected AsText mTitle;


    public BaseScene() {
    }

    public void onCreate(Deliver deliver) {
        mDeliver = deliver;

        int bg = deliver.getInt(KEY_BG, 0);
        if (bg != 0) {
            mBg = new Sprite(0, 0, 640, 480, AsActivity.it().getRegion(bg),
                    AsActivity.it().getVertexBufferObjectManager());
            attachChild(mBg);
        }

        int titleInt = deliver.getInt(KEY_TITLE, -1);
        if (titleInt != -1) {
            mTitle = new AsText();
            mTitle.setText(titleInt);
            mTitle.setTextSize(35);
            mTitle.setTextColor(Color.BLUE);
            mTitle.setPosition(320, 35);
        }
    }

    public void onResume(Deliver deliver) {
        if (mTitle != null && !mTitle.hasParent()) {
            attachChild(mTitle);
        }

        int rst = deliver.getInt(Deliver.KEY_RESULT);

        if (rst == Deliver.RST_EXIT) {
            AsEngine.it().pop();
        }
    }

    public void onPause() {
    }

    public void onDestroy() {
    }

    public void onMenuClick() {
        AsEngine.it().playSound(R.raw.sd_click);
        AsEngine.it().push(new SettingScene());
    }

    public void onBackClick() {
        AsEngine.it().playSound(R.raw.sd_click);
        AsEngine.it().pop();
    }
}