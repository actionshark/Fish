package com.sunhongkao.fish.scene;

import org.andengine.input.touch.TouchEvent;

import com.sunhongkao.fish.engine.AsActivity;
import com.sunhongkao.fish.engine.AsButton;
import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.AsText;
import com.sunhongkao.fish.engine.Deliver;
import com.sunhongkao.fish.R;


public class DialogScene extends DialogBaseScene {
    public static final String KEY_MSG = "dialog_message";
    public static final String KEY_BTNS = "dialog_buttons";
    public static final String KEY_RSTS = "dialog_results";


    private AsButton[] mBtns;
    private int[] mRsts;


    @Override
    public void onCreate(Deliver deliver) {
        super.onCreate(deliver);

        String msg = deliver.getString(KEY_MSG);
        AsText message = new AsText();
        message.setText(msg);
        message.setPosition(320, 230);
        message.setTextSize(35);
        message.setLineWidth(333);
        attachChild(message);

        String[] btns = deliver.getStringArray(KEY_BTNS);
        int i, len = btns.length;
        mBtns = new AsButton[len];
        mRsts = deliver.getIntArray(KEY_RSTS);

        for (i = 0; i < len; i++) {
            mBtns[i] = new AsButton() {
                @Override
                public boolean onAreaTouched(TouchEvent event, float x, float y) {
                    if (TouchEvent.ACTION_UP != event.getAction()) {
                        return false;
                    }

                    for (int i = 0; i < mBtns.length; i++) {
                        if (this == mBtns[i]) {
                            AsEngine.it().playSound(R.raw.sd_click);
                            Deliver deliver = new Deliver();

                            if (mRsts != null && mRsts.length > i) {
                                deliver.set(Deliver.KEY_RESULT, mRsts[i]);
                            }

                            AsEngine.it().pop(deliver);
                            return true;
                        }
                    }

                    return false;
                }
            };

            mBtns[i].setRegion(AsActivity.it().getRegion(R.drawable.cp_button_89_27));
            mBtns[i].setSize(460f * (len - 0.3f) / len / (len + 0.4f), 40);
            mBtns[i].setCenter(230f * (2 * i + 1) / len + 90, 375);
            mBtns[i].text().setText(btns[i]);
            mBtns[i].text().setTextSize(25);

            attachChild(mBtns[i]);
        }
    }

    @Override
    public void onBackClick() {
        if (mRsts != null && mRsts.length > mBtns.length) {
            AsEngine.it().playSound(R.raw.sd_click);
            Deliver deliver = new Deliver(Deliver.KEY_RESULT, mRsts[mBtns.length]);
            AsEngine.it().pop(deliver);
        } else {
            super.onBackClick();
        }
    }
}