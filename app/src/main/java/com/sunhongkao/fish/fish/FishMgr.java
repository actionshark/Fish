package com.sunhongkao.fish.fish;

import java.util.ArrayList;
import java.util.List;

import org.andengine.util.math.MathUtils;


public class FishMgr {
    public static final int EVENT_ADD = 1;
    public static final int EVENT_REMOVE = 2;
    public static final int EVENT_CHANGE = 3;
    public static final int EVENT_HUNGER = 4;

    protected static final List<Fish> sAll = new ArrayList<>();
    protected static int sHided;
    protected static final List<FishListener> sListeners
            = new ArrayList<>();


    public static void reset() {
        sAll.clear();
        sHided = 0;
        sListeners.clear();
    }

    public static List<Fish> getAll() {
        return sAll;
    }

    public static int showNum() {
        return sAll.size();
    }

    public static int hideNum() {
        return sHided;
    }

    public static Fish randFish() {
        int size = sAll.size();
        if (size <= 0) {
            return null;
        }

        return sAll.get(MathUtils.random(0, size - 1));
    }

    public static boolean hide(Guppy guppy) {
        if (guppy.detachSelf()) {
            sHided++;
            return true;
        }

        return false;
    }

    public static boolean show(Guppy guppy) {
        if (guppy.attachSelf()) {
            sHided--;
            return true;
        }

        return false;
    }

    public static void addListener(FishListener listener) {
        sListeners.add(listener);
    }

    public static void removeListener(FishListener listener) {
        sListeners.remove(listener);
    }

    protected static void onEvent(Fish fish, int event) {
        for (int i = 0; i < sListeners.size(); i++) {
            sListeners.get(i).onFishEvent(fish, event);
        }
    }

    public interface FishListener {
        void onFishEvent(Fish fish, int event);
    }
}