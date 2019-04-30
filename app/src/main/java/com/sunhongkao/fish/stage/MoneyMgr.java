package com.sunhongkao.fish.stage;

import java.util.ArrayList;
import java.util.List;

import org.andengine.util.math.MathUtils;

import android.graphics.Color;

import com.sunhongkao.fish.engine.AsButton;
import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.R;


public class MoneyMgr {
    public static final int MONEY_SILVER = 0;
    public static final int MONEY_GOLD = 1;
    public static final int MONEY_STAR = 2;
    public static final int MONEY_WORM = 3;
    public static final int MONEY_DIAMOND_L = 4;
    public static final int MONEY_PEARL_L = 5;
    public static final int MONEY_DIAMOND_B = 6;
    public static final int MONEY_PEARL_B = 7;
    public static final int MONEY_BOX = 8;

    public static final int EVENT_LOST = 1;

    protected static final int[] POINT =
            new int[]{15, 35, 40, 150, 200, 250, 350, 500, 2000};
    protected static final int[] RESID = new int[]{
            R.drawable.mn_silver_720_72, R.drawable.mn_gold_720_72,
            R.drawable.mn_star_720_72, R.drawable.mn_worm_720_72,
            R.drawable.mn_diamond_720_72, R.drawable.mn_pearl_72_72,
            R.drawable.mn_diamond_720_72, R.drawable.mn_pearl_72_72,
            R.drawable.mn_box_720_72};

    protected static final List<Money> sAllMoney = new ArrayList<>();
    private static final List<MoneyEventListener> sListener = new ArrayList<>();

    private static AsButton sText;
    private static int sPoint;
    protected static float sSpeedScale = 1;


    public static void init(AsButton text, int point) {
        sAllMoney.clear();
        sListener.clear();
        sText = text;
        sPoint = point;
        sSpeedScale = 1;
        sText.text().setText(sPoint + "");
        sText.setColor(1, 1, 0);
    }

    public static boolean spend(int point) {
        if (sPoint >= point) {
            sPoint -= point;
            sText.text().setText(sPoint + "");
            return true;
        }

        sText.text().setTextColor(Color.RED);
        AsEngine.it().runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                sText.text().setTextColor(Color.YELLOW);
            }
        }, 0.15f);
        AsEngine.it().runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                sText.text().setTextColor(Color.RED);
            }
        }, 0.30f);
        AsEngine.it().runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                sText.text().setTextColor(Color.YELLOW);
            }
        }, 0.45f);

        AsEngine.it().playSound(R.raw.sd_warn);
        return false;
    }

    public static void save(int count) {
        sPoint += count;
        sText.text().setText(sPoint + "");
    }

    public static int getPoint() {
        return sPoint;
    }

    public static Money newMoney(int type, int move) {
        if (type >= MONEY_SILVER && type <= MONEY_BOX) {
            Money money = new Money(type, move);
            sAllMoney.add(money);
            return money;
        }

        return null;
    }

    public static List<Money> getAll() {
        return sAllMoney;
    }

    public static Money randMoney() {
        int size = sAllMoney.size();
        if (size <= 0) {
            return null;
        }

        return sAllMoney.get(MathUtils.random(0, size - 1));
    }

    public static void speedScale(float scale) {
        sSpeedScale *= scale;
    }

    public static void addListener(MoneyEventListener listener) {
        sListener.add(listener);
    }

    public static void removeListener(MoneyEventListener listener) {
        sListener.remove(listener);
    }

    protected static void onEvent(Money money, int event) {
        for (int i = 0; i < sListener.size(); ++i) {
            sListener.get(i).onMoneyEvent(money, event);
        }
    }


    public interface MoneyEventListener {
        void onMoneyEvent(Money money, int event);
    }
}