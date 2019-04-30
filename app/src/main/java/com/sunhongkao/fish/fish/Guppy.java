package com.sunhongkao.fish.fish;

import java.util.List;

import org.andengine.util.math.MathUtils;

import com.sunhongkao.fish.R;
import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.engine.Util;
import com.sunhongkao.fish.iface.IEatee;
import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.iface.ITypeble;
import com.sunhongkao.fish.stage.Food;
import com.sunhongkao.fish.stage.FoodMgr;
import com.sunhongkao.fish.stage.MoneyMgr;
import com.sunhongkao.fish.stage.StageItem;


public class Guppy extends Fish implements ITurnable, ITypeble {
    public static final int GUPPY_SMALL = 0;
    public static final int GUPPY_MEDIUM = 1;
    public static final int GUPPY_BIG = 2;
    public static final int GUPPY_KING = 3;
    public static final int GUPPY_STAR = 4;

    private static final float[] SIZE = new float[]{(SIZE_TINY + SIZE_SMALL) / 2,
            SIZE_DOWN, SIZE_UP, SIZE_UP, SIZE_UP};
    private static final int[] GROW_LINE = new int[]{
            0, 800, 1800, 6300};
    private static final int[] MONEY = new int[]{-1, MoneyMgr.MONEY_SILVER,
            MoneyMgr.MONEY_GOLD, MoneyMgr.MONEY_DIAMOND_L, MoneyMgr.MONEY_STAR};
    private static final int[][] RES_NORMAL = new int[][]{{
            R.drawable.fs_guppy_idle_800_80, R.drawable.fs_guppy_turn_800_80,
            R.drawable.fs_guppy_eat_800_80, R.drawable.fs_guppy_die_800_80}, {
            R.drawable.fs_guppy_hungry_idle_800_80, R.drawable.fs_guppy_hungry_turn_800_80,
            R.drawable.fs_guppy_hungry_eat_800_80}};
    private static final int[][] RES_KING = new int[][]{{
            R.drawable.fs_guppyking_idle_800_80, R.drawable.fs_guppyking_turn_800_80,
            R.drawable.fs_guppyking_eat_800_80, R.drawable.fs_guppyking_die_800_80}, {
            R.drawable.fs_guppyking_hungry_idle_800_80, R.drawable.fs_guppyking_hungry_turn_800_80,
            R.drawable.fs_guppyking_hungry_eat_800_80}};
    private static final int[][] RES_STAR = new int[][]{{
            R.drawable.fs_guppystar_idle_800_80, R.drawable.fs_guppystar_turn_800_80,
            R.drawable.fs_guppystar_eat_800_80, R.drawable.fs_guppy_die_800_80}, {
            R.drawable.fs_guppy_hungry_idle_800_80, R.drawable.fs_guppy_hungry_turn_800_80,
            R.drawable.fs_guppy_hungry_eat_800_80}};

    private int mType = -1;
    private float mSize = SIZE[GUPPY_SMALL];
    private int mGrowCount = 0;


    public Guppy() {
        setSize(mSize, mSize);

        mActLine = (int) (Util.TIMES_LONG * MathUtils.random(0.9f, 1.1f));

        setType(GUPPY_SMALL);
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
        if (type < GUPPY_SMALL || type > GUPPY_STAR) {
            return false;
        }
        if (mType == type) {
            return true;
        }
        mType = type;

        if (mType == GUPPY_KING) {
            mResIds = RES_KING;
        } else if (mType == GUPPY_STAR) {
            mResIds = RES_STAR;
        } else {
            mResIds = RES_NORMAL;
        }
        mMoneyType = MONEY[mType];
        reload();

        FishMgr.onEvent(this, FishMgr.EVENT_CHANGE);

        return true;
    }

    @Override
    protected boolean shouldActCnt() {
        return mType != GUPPY_SMALL;
    }

    @Override
    protected StageItem wantEat() {
        if (mType == GUPPY_BIG) {
            List<Food> list = FoodMgr.getDrug();
            if (list.size() > 0) {
                return SeekAssist.seek(this, list);
            }
        }

        return SeekAssist.seek(this, FoodMgr.getAll());
    }

    @Override
    public int eat(IEatee eatee) {
        int point = super.eat(eatee);

        if (eatee instanceof Food && ((Food) eatee).getType() == FoodMgr.FOOD_DRUG) {
            if (mType == GUPPY_SMALL) {
                point = 0;
                killed();
            } else if (mType == GUPPY_MEDIUM) {
                point = 0;
            } else if (mType == GUPPY_BIG) {
                setType(GUPPY_STAR);
                AsEngine.it().playSound(R.raw.sd_fish_grow);
            }
        } else {
            if (point > 0 && mType <= GUPPY_BIG &&
                    (mGrowCount += point) >= GROW_LINE[mType + 1]) {

                setType(mType + 1);
                AsEngine.it().playSound(R.raw.sd_fish_grow);
            }
        }

        return point;
    }
}