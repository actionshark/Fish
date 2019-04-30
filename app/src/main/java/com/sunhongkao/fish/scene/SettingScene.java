package com.sunhongkao.fish.scene;

import org.andengine.input.touch.TouchEvent;
import org.andengine.util.HorizontalAlign;

import com.sunhongkao.fish.engine.AsActivity;
import com.sunhongkao.fish.engine.AsButton;
import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.AsText;
import com.sunhongkao.fish.engine.Deliver;
import com.sunhongkao.fish.engine.Recorder;
import com.sunhongkao.fish.engine.SliderBar;
import com.sunhongkao.fish.engine.SliderBar.SliderListener;
import com.sunhongkao.fish.R;


public class SettingScene extends DialogBaseScene implements SliderListener {
    private SliderBar mMusicSlider;
    private SliderBar mSoundSlider;
    private AsButton mScreenOn;


    @Override
    public void onCreate(Deliver deliver) {
        deliver.set(KEY_TITLE, R.string.setting_title);
        super.onCreate(deliver);

        AsText text = new AsText();
        text.setPosition(180, 163);
        text.setText(R.string.setting_music);
        text.setTextSize(25);
        text.setHorizontalAlign(HorizontalAlign.RIGHT);
        attachChild(text);

        mMusicSlider = new SliderBar(190, 130, 330, 65);
        mMusicSlider.setPercent(AsEngine.it().getMusicManager().getMasterVolume());
        mMusicSlider.setListener(this);
        attachChild(mMusicSlider);

        text = new AsText();
        text.setPosition(180, 233);
        text.setText(R.string.setting_sound);
        text.setTextSize(25);
        text.setHorizontalAlign(HorizontalAlign.RIGHT);
        attachChild(text);

        mSoundSlider = new SliderBar(190, 200, 330, 65);
        mSoundSlider.setPercent(AsEngine.it().getSoundManager().getMasterVolume());
        mSoundSlider.setListener(this);
        attachChild(mSoundSlider);

        mScreenOn = new AsButton() {
            @Override
            public boolean onAreaTouched(TouchEvent event, float x, float y) {
                if (event.getAction() == TouchEvent.ACTION_UP) {
                    if (Recorder.isKeepScreenOn()) {
                        mScreenOn.setRegion(AsActivity.it().getRegion(
                                R.drawable.cp_check_disable_37_37));
                        AsEngine.it().getEngineOptions().getWakeLockOptions().setKeepOn(false);
                        Recorder.setKeepScreenOn(false);
                    } else {
                        mScreenOn.setRegion(AsActivity.it().getRegion(
                                R.drawable.cp_check_enable_37_37));
                        AsEngine.it().getEngineOptions().getWakeLockOptions().setKeepOn(true);
                        Recorder.setKeepScreenOn(true);
                    }

                    return true;
                }

                return false;
            }
        };
        mScreenOn.setRegion(AsActivity.it().getRegion(Recorder.isKeepScreenOn() ?
                R.drawable.cp_check_enable_37_37 : R.drawable.cp_check_disable_37_37));
        mScreenOn.setCenter(200, 280);
        mScreenOn.setSize(50, 50);
        attachChild(mScreenOn);

        text = new AsText();
        text.setPosition(260, 300);
        text.setText(R.string.setting_keep_screen_on);
        text.setTextSize(30);
        text.setHorizontalAlign(HorizontalAlign.LEFT);
        attachChild(text);

        AsButton ok = new AsButton() {
            @Override
            public boolean onAreaTouched(TouchEvent event, float x, float y) {
                if (TouchEvent.ACTION_UP != event.getAction()) {
                    return false;
                }

                AsEngine.it().playSound(R.raw.sd_click);
                AsEngine.it().pop();
                return true;
            }
        };
        ok.setRegion(AsActivity.it().getRegion(R.drawable.cp_button_89_27));
        ok.setSize(120, 40);
        ok.setCenter(320, 375);
        ok.text().setText(R.string.dialog_ok);
        ok.text().setTextSize(25);

        attachChild(ok);
    }

    @Override
    public void onSlide(SliderBar sliderBar, float percent) {
        if (sliderBar == mMusicSlider) {
            AsEngine.it().getMusicManager().setMasterVolume(percent);
            Recorder.setMusicVolume(percent);
        } else if (sliderBar == mSoundSlider) {
            AsEngine.it().getSoundManager().setMasterVolume(percent);
            Recorder.setSoundVolume(percent);
        }
    }
}