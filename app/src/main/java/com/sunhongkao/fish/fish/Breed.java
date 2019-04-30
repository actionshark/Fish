package com.sunhongkao.fish.fish;

import org.andengine.util.math.MathUtils;

import com.sunhongkao.fish.R;
import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.engine.Util;
import com.sunhongkao.fish.iface.IEatee;
import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.iface.ITypeble;
import com.sunhongkao.fish.stage.FoodMgr;
import com.sunhongkao.fish.stage.StageItem;


public class Breed extends Fish implements ITurnable, ITypeble {
    public static final int BREED_SMALL = 0;
    public static final int BREED_MEDIUM = 1;
    public static final int BREED_BIG = 2;

    private static final float[] SIZE = new float[]{
            SIZE_SMALL, SIZE_DOWN, SIZE_UP};
    private static final int[] GROW_LINE = new int[]{0, 900, 2700};
    private static final int[][] RES = new int[][]{{
            R.drawable.fs_breed_idle_800_80, R.drawable.fs_breed_turn_800_80,
            R.drawable.fs_breed_eat_800_80, R.drawable.fs_breed_die_800_80}, {
            R.drawable.fs_breed_hungry_idle_800_80, R.drawable.fs_breed_hungry_turn_800_80,
            R.drawable.fs_breed_hungry_eat_800_80}};

    private int mType = -1;
    private float mSize = SIZE[BREED_SMALL];
    private int mGrowCount = 0;


    public Breed() {
        setSize(mSize, mSize);

        mResIds = RES;
        mActLine = (int) (Util.TIMES_LONGER * MathUtils.random(0.9f, 1.1f));
        setType(BREED_SMALL);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (mSize < SIZE[mType]) {
            mSize++;
            setSize(mSize, mSize);
        }
    }

    @Override
    public int getType() {
        return mType;
    }

    @Override
    public boolean setType(int type) {
        if (type < BREED_SMALL || type > BREED_BIG) {
            return false;
        }
        if (mType == type) {
            return true;
        }
        mType = type;

        if (mType == BREED_BIG) {
            mActLine = Util.TIMES_LONG;
        }

        reload();

        FishMgr.onEvent(this, FishMgr.EVENT_CHANGE);

        return true;
    }

    @Override
    public boolean shouldActCnt() {
        return mType != BREED_SMALL;
    }

    @Override
    protected StageItem wantEat() {
        return SeekAssist.seek(this, FoodMgr.getAll());
    }

    @Override
    public int eat(IEatee eatee) {
        int point = super.eat(eatee);

        if (point > 0 && mType < BREED_BIG &&
                (mGrowCount += point) >= GROW_LINE[mType + 1]) {

            setType(mType + 1);
            AsEngine.it().playSound(R.raw.sd_fish_grow);
        }

        return point;
    }

    @Override
    public boolean act() {
        AsEngine.it().playSound(R.raw.sd_bread_work);

        Guppy guppy = new Guppy();
        guppy.setCx(getCx());
        guppy.setCy(getCy());
        guppy.attachSelf();
        guppy.seek(getCx(), getCy() + getHeight() * 2);

        return true;
    }
}