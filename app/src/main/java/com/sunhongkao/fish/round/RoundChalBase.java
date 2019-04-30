package com.sunhongkao.fish.round;

import com.sunhongkao.fish.engine.AsActivity;
import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.Recorder;
import com.sunhongkao.fish.stage.PropBox;
import com.sunhongkao.fish.R;


public abstract class RoundChalBase extends RoundBase {
    protected final int COUNT_MAX = AsEngine.it().getFPS() * 3;
    protected final int POINT_MAX = 99999;
    protected int mTime = 0;


    @Override
    public void onUpdate() {
        super.onUpdate();

        if (++mTime % COUNT_MAX == 0) {
            for (int i = 0; i < mPropBoxs.length; i++) {
                if (mPropBoxs[i].getState() == PropBox.STATE_OPEN) {
                    int point = mPropBoxs[i].getPoint() + getInitCost(i) / 100;
                    if (point < POINT_MAX) {
                        mPropBoxs[i].setPoint(point);
                    } else {
                        mPropBoxs[i].setPoint(POINT_MAX);
                    }
                }
            }
        }
    }

    @Override
    public String getName() {
        return AsActivity.it().getString(R.string.mode_chal)
                + " " + (getMajor() + 1);
    }

    protected int getInitCost(int index) {
        int major = getMajor();

        if (index == 0) {
            if (major < 3) {
                return 100;
            } else {
                return 200;
            }
        } else if (index == 1) {
            return 200;
        } else if (index == 2) {
            return 300;
        } else if (index == 3) {
            if (major == 1) {
                return 250;
            } else if (major == 2) {
                return 750;
            } else {
                return 1000;
            }
        } else if (index == 4) {
            if (major == 1) {
                return 750;
            } else if (major == 2) {
                return 2000;
            } else if (major == 3) {
                return 10000;
            }
        } else if (index == 5) {
            return getGunCost();
        } else if (index == 6) {
            return getEggCost();
        }

        return POINT_MAX;
    }

    @Override
    public void onPropEvent(PropBox propBox) {
        if (propBox == mPropBoxs[6]) {
            if (propBox.getCurIndex() < 2) {
                propBox.upgrade();

                for (int i = 0; i < mPropBoxs.length; i++) {
                    int state = mPropBoxs[i].getState();

                    if (state == PropBox.STATE_OPEN || state == PropBox.STATE_OPENING) {
                        mPropBoxs[i].setPoint(getInitCost(i));
                    }
                }
            } else {
                Recorder.setRoundChalState(mRoundIndex, RoundMgr.STATE_PASSED);
                onFinish();
            }

            return;
        }

        super.onPropEvent(propBox);
    }
}