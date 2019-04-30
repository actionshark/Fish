package com.sunhongkao.fish.iface;


public interface IMover {
    int TYPE_DOWN = 1;
    int TYPE_UP = 2;
    int TYPE_UP_DOWN = 3;
    int TYPE_STILL = 4;


    void setMove(int type);

    int getMove();
}