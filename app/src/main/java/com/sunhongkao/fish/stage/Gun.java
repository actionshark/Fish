package com.sunhongkao.fish.stage;

import com.sunhongkao.fish.stage.PropBox.PropListener;
import com.sunhongkao.fish.R;


public class Gun {
    private static PropBox sPropBox;
    private static final int MAX = 10;
    private static int sLevel = 0;


    public static void init(PropBox propBox) {
        sPropBox = propBox;
        sLevel = 0;
    }

    public static void open(int cost) {
        if (sPropBox.getState() != PropBox.STATE_READY) {
            return;
        }

        sPropBox.setStop(StageGift.STOP_STEP);
        sPropBox.setResIds(R.drawable.cp_gun_460_39);
        sPropBox.setPoint(cost);
        sPropBox.addListener(new PropListener() {
            @Override
            public void onPropEvent(PropBox propBox) {
                if (sLevel + 1 < MAX) {
                    sLevel++;
                    sPropBox.upgrade();
                } else if (sLevel + 1 == MAX) {
                    sLevel++;
                    sPropBox.close();
                }
            }
        });
        sPropBox.open(false);
    }

    public static int attackValue() {
        return attackValue(sLevel);
    }

    public static int attackValue(int level) {
        return level + 1;
    }
}