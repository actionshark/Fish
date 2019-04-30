package com.sunhongkao.fish.fish;

import com.sunhongkao.fish.engine.AsActivity;
import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.stage.StageItem;


public class Victim extends Fish implements ITurnable {
    public Victim(String name, boolean jump) {
        setWidth(SIZE_NORMAL);

        String size;

        if ("pt_eel".equals(name)) {
            setHeight(getWidth() * 3 / 8);
            size = "_1600_60";
        } else {
            setHeight(getWidth());
            size = "_800_80";
        }

        mResIds = new int[][]{{AsActivity.it().getDrawId(name + "_idle" + size),
                AsActivity.it().getDrawId(name + "_turn" + size), 0, 0}};
        if (mResIds[0][1] == 0) {
            mResIds[0][1] = mResIds[0][0];
        }

        if ("clam".equals(name)) {
            mSeekAction.speedX = 0;
        } else {
            mSeekAction.speedX = SeekAssist.SPEED_NORMAL;
        }

        if (jump) {
            mSeekAction.speedY = SeekAssist.SPEED_NORMAL;
        } else {
            mSeekAction.speedY = 0;
        }

        reload();
    }

    @Override
    public void onUpdate() {
        mHungryCnt = mHungryLine[2];
        mActCnt = 0;
        super.onUpdate();
    }

    @Override
    protected StageItem wantEat() {
        return null;
    }
}