package com.sunhongkao.fish.scene;

import org.andengine.util.HorizontalAlign;
import org.andengine.util.VerticalAlign;

import com.sunhongkao.fish.engine.AsActivity;
import com.sunhongkao.fish.engine.AsText;
import com.sunhongkao.fish.engine.Deliver;
import com.sunhongkao.fish.R;


public class HowToPlayScene extends BaseScene {
    @Override
    public void onCreate(Deliver deliver) {
        deliver.set(KEY_BG, R.drawable.bg_how_to_play_640_480);
        super.onCreate(deliver);

        AsText[] text = new AsText[6];

        for (int i = 0; i < text.length; i++) {
            text[i] = new AsText();
            text[i].setText(AsActivity.it().getStringId("help_" + i));
            text[i].setAlign(HorizontalAlign.LEFT, VerticalAlign.TOP);
            text[i].setTextSize(22);
            text[i].setLineWidth(190);
            text[i].setPosition(i % 3 * 214 + 3, i / 3 * 240 + 160);
            attachChild(text[i]);
        }
    }
}