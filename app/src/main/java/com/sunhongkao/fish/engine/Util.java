package com.sunhongkao.fish.engine;


public class Util {
    public static final int TIMES_SHORTER = 30;
    public static final int TIMES_SHORT = 55;
    public static final int TIMES_DOWN = 95;
    public static final int TIMES_NORMAL = 150;
    public static final int TIMES_UP = 220;
    public static final int TIMES_LONG = 305;
    public static final int TIMES_LONGER = 400;


    public static int[] getSize(String name) {
        try {
            int[] size = new int[2];

            int index = name.lastIndexOf('_');
            size[1] = getInt(name, index + 1, size[1]);

            index = name.lastIndexOf('_', index - 1);
            size[0] = getInt(name, index + 1, size[0]);

            return size;
        } catch (Exception e) {
            Logger.print(e);
        }

        return null;
    }

    public static int getInt(String name, int start, int def) {
        try {
            int n = 0;

            for (int i = start; i < name.length(); i++) {
                char ch = name.charAt(i);

                if (Character.isDigit(ch)) {
                    n = n * 10 + ch - '0';
                } else {
                    break;
                }
            }

            return n;
        } catch (Exception e) {
            Logger.print(e);
        }

        return def;
    }

    public static boolean equals(float a, float b) {
        return Math.abs(a - b) < 0.01f;
    }
}