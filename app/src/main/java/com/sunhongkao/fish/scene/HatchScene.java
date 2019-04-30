package com.sunhongkao.fish.scene;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;

import com.sunhongkao.fish.engine.AsActivity;
import com.sunhongkao.fish.engine.AsButton;
import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.AsText;
import com.sunhongkao.fish.engine.Deliver;
import com.sunhongkao.fish.pet.Pet;
import com.sunhongkao.fish.R;


public class HatchScene extends BaseScene {
    @Override
    public void onCreate(Deliver deliver) {
        deliver.set(KEY_BG, R.drawable.bg_dialog_320_240);
        deliver.set(KEY_TITLE, R.string.dialog_unlock_pet);
        super.onCreate(deliver);

        int index = deliver.getInt(Pet.KEY_INDEX);

        AnimatedSprite pet = new AnimatedSprite(263, 115, 114, 114,
                AsActivity.it().getRegions(AsActivity.it().getDrawId
                        ("pt_" + Pet.PETS[index] + "_idle" + Pet.getSize(index))),
                AsActivity.it().getVertexBufferObjectManager());
        pet.animate(AsEngine.it().getMPF());
        attachChild(pet);

        AsText text = new AsText();
        text.setPosition(320, 300);
        text.setText(AsActivity.it().getStringId("pet_" + Pet.PETS[index]));
        text.setTextSize(30);
        text.setLineWidth(500);
        attachChild(text);

        AsButton ok = new AsButton() {
            @Override
            public boolean onAreaTouched(TouchEvent event, float x, float y) {
                if (event.getAction() == TouchEvent.ACTION_UP) {
                    onBackClick();
                    return true;
                }

                return false;
            }
        };
        ok.setRegion(AsActivity.it().getRegion(R.drawable.cp_button_89_27));
        ok.setSize(192, 48);
        ok.setCenter(320, 410);
        ok.text().setText(R.string.dialog_ok);
        ok.text().setTextSize(25);

        attachChild(ok);
    }
}