package com.sunhongkao.fish.scene;

import org.andengine.util.HorizontalAlign;

import android.content.pm.PackageInfo;

import com.sunhongkao.fish.engine.AsActivity;
import com.sunhongkao.fish.engine.AsText;
import com.sunhongkao.fish.engine.Deliver;
import com.sunhongkao.fish.R;
import com.sunhongkao.fish.engine.Logger;


public class AboutScene extends BaseScene {
    @Override
    public void onCreate(Deliver deliver) {
        deliver.set(KEY_BG, R.drawable.bg_dialog_320_240);
        deliver.set(KEY_TITLE, R.string.help_about);
        super.onCreate(deliver);

        AsText text = new AsText();
        text.setText(R.string.app_name);

        try {
            PackageInfo pkgInfo = AsActivity.it().getPackageManager().
                    getPackageInfo(AsActivity.it().getPackageName(), 0);
            text.appendText(" " + pkgInfo.versionName);
        } catch (Exception e) {
            Logger.print(e);
        }

        text.appendText("\n\n");
        text.appendText(R.string.help_about_words);
        text.setHorizontalAlign(HorizontalAlign.LEFT);
        text.setTextSize(30);
        text.setPosition(85, 250);
        text.setLineWidth(470);
        attachChild(text);
    }
}