package com.sunhongkao.fish.scene;

import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.Deliver;
import com.sunhongkao.fish.R;


public class StartScene extends BaseScene implements Runnable {
    @Override
    public void onCreate(Deliver deliver) {
        deliver.set(KEY_BG, R.drawable.bg_start_640_480);
        super.onCreate(deliver);
    }

    @Override
    public void onResume(Deliver deliver) {
        super.onResume(deliver);

        AsEngine.it().removeRunnable(this);
        AsEngine.it().runOnUpdateThread(this, 1.5f);
    }

    @Override
    public void run() {
        AsEngine.it().pop();
    }

    @Override
    public void onMenuClick() {
    }

    @Override
    public void onBackClick() {
    }
}