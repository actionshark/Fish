package com.sunhongkao.fish.engine;

import java.util.HashMap;
import java.util.Map;


public class Deliver {
    public static final String KEY_INTENT = "deliver_intent";
    public static final String KEY_RESULT = "deliver_result";

    public static final int RST_NOTHING = 0;
    public static final int RST_RESTART = 1;
    public static final int RST_EXIT = 2;
    public static final int RST_CLONE = 3;


    private final Map<String, Object> mMap = new HashMap<>();


    public Deliver() {
    }

    public Deliver(String key, Object val) {
        mMap.put(key, val);
    }

    public void set(String key, Object val) {
        mMap.put(key, val);
    }

    public void remove(String key) {
        mMap.remove(key);
    }

    public Object get(String key) {
        return mMap.get(key);
    }

    public int getInt(String key) {
        return getInt(key, -1);
    }

    public int getInt(String key, int def) {
        if (mMap.containsKey(key)) {
            try {
                return (Integer) mMap.get(key);
            } catch (Exception e) {
                Logger.print(e);
            }
        }

        return def;
    }

    public String getString(String key) {
        if (mMap.containsKey(key)) {
            try {
                return (String) mMap.get(key);
            } catch (Exception e) {
                Logger.print(e);
            }
        }

        return null;
    }

    public boolean getBoolean(String key) {
        if (mMap.containsKey(key)) {
            try {
                return (Boolean) mMap.get(key);
            } catch (Exception e) {
                Logger.print(e);
            }
        }

        return false;
    }

    public int[] getIntArray(String key) {
        if (mMap.containsKey(key)) {
            try {
                return (int[]) mMap.get(key);
            } catch (Exception e) {
                Logger.print(e);
            }
        }

        return null;
    }

    public String[] getStringArray(String key) {
        if (mMap.containsKey(key)) {
            try {
                return (String[]) mMap.get(key);
            } catch (Exception e) {
                Logger.print(e);
            }
        }

        return null;
    }
}