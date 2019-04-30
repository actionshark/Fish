package com.sunhongkao.fish.iface;


public interface IMover {
    public static final int TYPE_DOWN = 1;
    public static final int TYPE_UP = 2;
    public static final int TYPE_UP_DOWN = 3;
    public static final int TYPE_STILL = 4;


    public void setMove(int type);

    public int getMove();
}