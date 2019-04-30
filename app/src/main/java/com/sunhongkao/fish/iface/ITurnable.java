package com.sunhongkao.fish.iface;


public interface ITurnable {
    boolean isTurned();

    boolean tryTurn(float dx);
}