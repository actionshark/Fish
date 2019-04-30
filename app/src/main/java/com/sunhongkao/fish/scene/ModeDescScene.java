package com.sunhongkao.fish.scene;

import org.andengine.util.HorizontalAlign;

import com.sunhongkao.fish.engine.AsText;
import com.sunhongkao.fish.engine.Deliver;
import com.sunhongkao.fish.R;


public class ModeDescScene extends BaseScene {
    @Override
    public void onCreate(Deliver deliver) {
        deliver.set(KEY_BG, R.drawable.bg_dialog_320_240);
        deliver.set(KEY_TITLE, R.string.help_mode);
        super.onCreate(deliver);

        AsText text = new AsText();
        text.setText(R.string.help_mode_words);
        text.setHorizontalAlign(HorizontalAlign.LEFT);
        text.setTextSize(29);
        text.setLineWidth(500);
        text.setPosition(70, 260);
        attachChild(text);
    }
}