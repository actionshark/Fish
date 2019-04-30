package com.sunhongkao.fish.engine;

import java.util.ArrayList;
import java.util.List;

import com.sunhongkao.fish.engine.SeekAction.Seeker;
import com.sunhongkao.fish.stage.StageItem;


public class SeekAssist {
    public static final float SPEED_SLOW = 3.2f;
    public static final float SPEED_DOWN = SPEED_SLOW * 1.5f;
    public static final float SPEED_NORMAL = SPEED_SLOW * 2.2f;
    public static final float SPEED_UP = SPEED_SLOW * 3.3f;
    public static final float SPEED_FAST = SPEED_SLOW * 5.0f;
    public static final float SPEED_SUPER = SPEED_SLOW * 7.0f;


    public static StageItem seek(Seeker seeker, List<? extends StageItem> seekees) {
        return seek(seeker.getSeekerX(), seeker.getSeekerY(), seekees);
    }

    public static StageItem seek(float x, float y, List<? extends StageItem> seekees) {
        if (seekees == null) {
            return null;
        }

        StageItem ret = null;
        float best = Float.MAX_VALUE;

        for (int i = 0; i < seekees.size(); i++) {
            StageItem tmp = seekees.get(i);
            float dist = Math.max(Math.abs(tmp.getCx() - x),
                    Math.abs(tmp.getCy() - y));

            if (best > dist) {
                best = dist;
                ret = tmp;
            }
        }

        return ret;
    }

    public static List<StageItem> touch(StageItem toucher,
                                        List<? extends StageItem> touchees) {

        List<StageItem> ret = new ArrayList<>();
        if (toucher == null || touchees == null) {
            return ret;
        }

        for (int i = 0; i < touchees.size(); ++i) {
            if (isTouched(toucher, touchees.get(i))) {
                ret.add(touchees.get(i));
            }
        }

        return ret;
    }

    public static boolean isTouched(StageItem a, StageItem b) {
        if (a == null || b == null) {
            return false;
        }

        float ax, ay;

        if (a instanceof Seeker) {
            ax = ((Seeker) a).getSeekerX();
            ay = ((Seeker) a).getSeekerY();
        } else {
            ax = a.getCx();
            ay = a.getCy();
        }

        return Math.abs(ax - b.getCx()) < (a.getWidth() + b.getWidth()) / 4 &&
                Math.abs(ay - b.getCy()) < (a.getHeight() + b.getHeight()) / 4;
    }
}