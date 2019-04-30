package com.sunhongkao.fish.mons;

import java.util.ArrayList;
import java.util.List;


public class MonsterMgr {
    public static final int HEALTH_LOWER = 17;
    public static final int HEALTH_LOW = 28;
    public static final int HEALTH_DOWN = 45;
    public static final int HEALTH_NORMAL = 72;
    public static final int HEALTH_UP = 117;
    public static final int HEALTH_HIGH = 189;
    public static final int HEALTH_HIGHER = 307;

    protected static final List<Monster> sAll = new ArrayList<Monster>();
    protected static final List<Monster> sDown = new ArrayList<Monster>();


    public static void reset() {
        sAll.clear();
        sDown.clear();
    }

    public static List<Monster> getAll() {
        return sAll;
    }

    public static List<Monster> getDown() {
        return sDown;
    }

    public static Monster newMonster(int index, int health) {
        switch (index) {
            case 0:
                return new Bluester(health);
            case 1:
                return new Yellowster(health);
            case 2:
                return new Cram(health);
            case 3:
                return new Robot(health);
            case 4:
                return new Octopus(health);
            case 5:
                return new Cyclops(health);
            case 6:
                return new DualHeads(health);
        }

        return null;
    }
}