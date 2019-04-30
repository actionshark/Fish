package com.sunhongkao.fish.scene;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import com.sunhongkao.fish.engine.AsActivity;
import com.sunhongkao.fish.engine.AsButton;
import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.Deliver;
import com.sunhongkao.fish.R;


public class HelpScene extends BaseScene {
    @Override
    public void onCreate(Deliver deliver) {
        deliver.set(KEY_BG, R.drawable.bg_lake_200_140);
        deliver.set(KEY_TITLE, R.string.help_title);
        super.onCreate(deliver);

        Sprite bg = new Sprite(0, 0, 640, 480,
                AsActivity.it().getRegion(R.drawable.bg_help_640_480),
                AsActivity.it().getVertexBufferObjectManager());
        attachChild(bg);

        Sprite[] icons = new Sprite[4];
        icons[0] = new AnimatedSprite(0, 0, AsActivity.it().getRegions(
                R.drawable.pt_horse_idle_800_80), AsActivity.it().getVertexBufferObjectManager());
        icons[1] = new Sprite(0, 0, AsActivity.it().getRegion(R.drawable.cp_question_80_80),
                AsActivity.it().getVertexBufferObjectManager());
        icons[2] = new AnimatedSprite(0, 0, AsActivity.it().getRegions(R.drawable.fs_carn_idle_800_80),
                AsActivity.it().getVertexBufferObjectManager());
        icons[3] = new Sprite(0, 0, AsActivity.it().getRegion(R.drawable.cp_i_80_80),
                AsActivity.it().getVertexBufferObjectManager());

        ((AnimatedSprite) icons[0]).animate(AsEngine.it().getMPF());
        ((AnimatedSprite) icons[2]).animate(AsEngine.it().getMPF());

        String[] text = AsActivity.it().getStrings(R.string.pet_title,
                R.string.help_how_to_play, R.string.help_mode, R.string.help_about);
        final AsButton[] btns = new AsButton[4];

        for (int i = 0; i < btns.length; i++) {
            btns[i] = new AsButton() {
                @Override
                public boolean onAreaTouched(TouchEvent event, float x, float y) {
                    if (TouchEvent.ACTION_UP != event.getAction()) {
                        return false;
                    }

                    AsEngine.it().playSound(R.raw.sd_click);

                    if (btns[0] == this) {
                        Deliver del = new Deliver(Deliver.KEY_INTENT, PetScene.INTENT_SHOW);
                        AsEngine.it().push(new PetScene(), del);
                    } else if (btns[1] == this) {
                        AsEngine.it().push(new HowToPlayScene());
                    } else if (btns[2] == this) {
                        AsEngine.it().push(new ModeDescScene());
                    } else if (btns[3] == this) {
                        AsEngine.it().push(new AboutScene());
                    }

                    return true;
                }
            };

            btns[i].setSize(230, 65);
            btns[i].setCenter(320, 95 * i + 108);
            btns[i].text().setText(text[i]);
            btns[i].text().setTextSize(35);

            icons[i].setPosition(140, 95 * i + 80);
            icons[i].setSize(60, 60);
            attachChild(icons[i]);
            attachChild(btns[i]);
        }
    }
}