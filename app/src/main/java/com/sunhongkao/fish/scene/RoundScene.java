package com.sunhongkao.fish.scene;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.ScreenGrabber;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.HorizontalAlign;

import android.graphics.Color;

import com.sunhongkao.fish.engine.AsActivity;
import com.sunhongkao.fish.engine.AsButton;
import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.AsText;
import com.sunhongkao.fish.engine.Deliver;
import com.sunhongkao.fish.mons.Monster;
import com.sunhongkao.fish.pet.Eel;
import com.sunhongkao.fish.pet.Pet;
import com.sunhongkao.fish.pet.Tadpole;
import com.sunhongkao.fish.round.RoundBase;
import com.sunhongkao.fish.round.RoundMgr;
import com.sunhongkao.fish.stage.Money;
import com.sunhongkao.fish.stage.PropBox;
import com.sunhongkao.fish.stage.StageGift;
import com.sunhongkao.fish.stage.StageItem;
import com.sunhongkao.fish.R;


public class RoundScene extends BaseScene {
    private RoundBase mRound;

    private final Rectangle mBottom = new Rectangle(0, 0, 640, 480,
            AsActivity.it().getVertexBufferObjectManager());
    private final Rectangle mCenter = new Rectangle(0, 0, 640, 480,
            AsActivity.it().getVertexBufferObjectManager());
    private final Rectangle mTop = new Rectangle(0, 0, 640, 480,
            AsActivity.it().getVertexBufferObjectManager());


    @Override
    public void onCreate(Deliver deliver) {
        super.onCreate(deliver);

        mRound = RoundMgr.newRound(deliver);

        mBottom.setAlpha(0);
        mCenter.setAlpha(0);
        mTop.setAlpha(0);

        super.attachChild(mBottom);
        super.attachChild(mCenter);
        super.attachChild(mTop);

        Sprite bg = new Sprite(0, 0, 640, tankHeight(),
                AsActivity.it().getRegion(AsActivity.it().getDrawId
                        ("bg_round_" + mRound.getMajor() + "_640_480")),
                AsActivity.it().getVertexBufferObjectManager()) {

            @Override
            public boolean onAreaTouched(TouchEvent event, float x, float y) {
                if (event.getAction() == TouchEvent.ACTION_UP) {
                    return mRound.onTouchEvent(event);
                }

                return false;
            }

            @Override
            public void onManagedUpdate(float second) {
                super.onManagedUpdate(second);

                mRound.onUpdate();
            }
        };
        mBottom.attachChild(bg);

        Sprite propBar = new Sprite(0, tankHeight(), 640, 480 - tankHeight(),
                AsActivity.it().getRegion(R.drawable.bg_propbar_640_67),
                AsActivity.it().getVertexBufferObjectManager());
        mTop.attachChild(propBar);

        PropBox[] propBox = new PropBox[7];
        float[] XS = new float[]{48, 116, 174, 247, 320, 394, 467};

        for (int i = 0; i < propBox.length; i++) {
            StageItem mark = new StageItem();
            mark.setSize(53, 48);
            mark.setCenter(XS[i], 434);
            mark.setResId(R.drawable.cp_petbox_select_89_76);

            StageGift prop = new StageGift();
            prop.setSize(35, 35);
            prop.setCenter(XS[i], 437);
            mTop.attachChild(prop);

            StageGift box = new StageGift();
            box.setStop(StageGift.STOP_LAST);
            box.setSize(58, 65);
            box.setCenter(XS[i], 443);
            box.setResIds(R.drawable.cp_prop_box_456_60, 8);
            box.setCurIndex(3);
            mTop.attachChild(box);

            AsText text = new AsText();
            text.setPosition(XS[i], 467);
            text.setTextSize(15);
            text.setTextColor(Color.WHITE);
            mTop.attachChild(text);

            propBox[i] = new PropBox(mark, prop, box, text);
        }

        AsButton setting = new AsButton() {
            @Override
            public boolean onAreaTouched(TouchEvent event, float x, float y) {
                if (event.getAction() == TouchEvent.ACTION_UP) {
                    onMenuClick();
                    return true;
                }

                return false;
            }
        };
        setting.setSize(90, 33);
        setting.text().setText(R.string.setting_title);
        setting.text().setTextSize(20);
        setting.text().setTextColor(Color.YELLOW);
        setting.setCenter(576, 426);
        mTop.attachChild(setting);

        AsButton moneyText = new AsButton() {
            @Override
            public boolean onAreaTouched(TouchEvent event, float x, float y) {
                if (event.getAction() == TouchEvent.ACTION_UP && mRound.cloneble()) {
                    AsEngine.it().playSound(R.raw.sd_click);
                    AsEngine.it().push(new PetScene(), new Deliver(
                            Deliver.KEY_INTENT, PetScene.INTENT_CLONE));
                    return true;
                }

                return false;
            }
        };
        moneyText.text().setTextSize(20);
        moneyText.text().setTextColor(Color.YELLOW);
        moneyText.text().setHorizontalAlign(HorizontalAlign.RIGHT);
        moneyText.setSize(90, 33);
        moneyText.setPosition(530, 445);

        mTop.attachChild(moneyText);

        AsText nameText = new AsText();
        nameText.setPosition(6, 15);
        nameText.setTextSize(30);
        nameText.setTextColor(Color.WHITE);
        nameText.setHorizontalAlign(HorizontalAlign.LEFT);
        nameText.setText(mRound.getName());
        mTop.attachChild(nameText);

        deliver.set(RoundMgr.KEY_PROP_BOX, propBox);
        deliver.set(RoundMgr.KEY_MONEY_TEXT, moneyText);
        mRound.onCreate(deliver);
    }

    @Override
    public void onResume(Deliver deliver) {
        super.onResume(deliver);

        if (AsEngine.it().getScene() == this && mRound != null) {
            mRound.onResume();
        }

        int rst = deliver.getInt(Deliver.KEY_RESULT);

        if (rst == Deliver.RST_CLONE) {
            int index = deliver.getInt(Pet.KEY_INDEX);
            mRound.onClone(index);
            return;
        }

        if (rst == Deliver.RST_RESTART) {
            Deliver del = new Deliver();
            del.set(Deliver.KEY_INTENT, PetScene.INTENT_ROUND);
            del.set(RoundMgr.KEY_TYPE, mDeliver.get(RoundMgr.KEY_TYPE));
            del.set(RoundMgr.KEY_INDEX, mDeliver.get(RoundMgr.KEY_INDEX));

            AsEngine.it().pop();
            AsEngine.it().push(new PetScene(), del);
            return;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        mRound.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mRound.onDestroy();
    }

    @Override
    public void onBackClick() {
        AsEngine.it().playSound(R.raw.sd_click);

        BaseScene scene = new DialogScene();

        Deliver deliver = new Deliver();
        deliver.set(KEY_TITLE, R.string.dialog_pause);
        deliver.set(DialogScene.KEY_MSG, AsActivity.it().getString(R.string.dialog_want_exit));
        deliver.set(DialogScene.KEY_BTNS, AsActivity.it().getStrings(R.string.dialog_exit,
                R.string.dialog_restart, R.string.dialog_cancel));
        deliver.set(DialogScene.KEY_RSTS, new int[]{Deliver.RST_EXIT, Deliver.RST_RESTART});

        AsEngine.it().push(scene, deliver);
    }

    @Override
    public void attachChild(IEntity entity) {
        if (entity instanceof ScreenGrabber) {
            super.attachChild(entity);
        } else if (entity instanceof Monster || entity instanceof Money ||
                entity instanceof Eel || entity instanceof Tadpole) {

            mCenter.attachChild(entity);
        } else {
            mBottom.attachChild(entity);
        }
    }

    public static float tankBottom() {
        return 375;
    }

    public static float tankHeight() {
        return 408;
    }
}