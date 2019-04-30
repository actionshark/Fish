package com.sunhongkao.fish.stage;

import java.util.ArrayList;
import java.util.List;

import com.sunhongkao.fish.stage.PropBox.PropListener;
import com.sunhongkao.fish.R;


public class FoodMgr {
    public static final int FOOD_LOW = 0;
    public static final int FOOD_MEDIUM = 1;
    public static final int FOOD_HIGH = 2;
    public static final int FOOD_DRUG = 3;

    protected static final int[] RESID = new int[]{
            R.drawable.fd_low_400_40, R.drawable.fd_medium_400_40,
            R.drawable.fd_high_400_40, R.drawable.fd_drug_400_40};
    protected static final List<Food> sAll = new ArrayList<>();

    private static int sType = FOOD_LOW;
    private static int sTypeCost = 200;
    private static PropBox sTypeBox;


    private static final int MAX = 9;
    private static int sNum = 1;
    protected static int sNumEx = 0;
    private static int sNumCost = 300;
    private static PropBox sNumBox;

    private static PropBox sDrugBox;
    private static boolean sDrugEnabled = false;

    private static int sCost = 5;
    protected static float sSpeedScale = 1;

    private static PropListener sPropListener = new PropListener() {
        @Override
        public void onPropEvent(PropBox propBox) {
            if (sTypeBox == propBox) {
                if (sType < FOOD_MEDIUM) {
                    sTypeBox.upgrade();
                    sType = FOOD_MEDIUM;
                    sTypeBox.setResIds(RESID[FOOD_HIGH]);
                } else if (sType == FOOD_MEDIUM) {
                    sType = FOOD_HIGH;
                    sTypeBox.close();
                }
            } else if (sNumBox == propBox) {
                if (sNum + 1 < MAX) {
                    sNumBox.upgrade();
                    sNum++;
                } else if (sNum + 1 == MAX) {
                    sNum = MAX;
                    sNumBox.close();
                }
            }
        }
    };


    public static void init(PropBox typeBox, PropBox numBox, PropBox drugBox) {
        sTypeBox = typeBox;
        sNumBox = numBox;
        sDrugBox = drugBox;
        sAll.clear();

        sType = FOOD_LOW;
        sTypeCost = 200;

        sNum = 1;
        sNumEx = 0;
        sNumCost = 300;

        sCost = 5;
        sSpeedScale = 1;
        sDrugEnabled = false;
    }

    public static void openType() {
        if (sTypeBox.getState() == PropBox.STATE_READY) {
            sTypeBox.setResIds(RESID[sType + 1]);
            sTypeBox.setPoint(sTypeCost);
            sTypeBox.addListener(sPropListener);
            sTypeBox.open();
        }
    }

    public static void openNum() {
        if (sNumBox.getState() == PropBox.STATE_READY) {
            sNumBox.setStop(StageGift.STOP_STEP);
            sNumBox.setResIds(R.drawable.cp_num_400_40);
            sNumBox.setCurIndex(1);
            sNumBox.setPoint(sNumCost);
            sNumBox.addListener(sPropListener);
            sNumBox.open();
        }
    }

    public static Food newFood() {
        if (sDrugEnabled) {
            sDrugEnabled = false;
            sDrugBox.mark(false);
            sDrugBox.enableClick(true);
            return newFood(FOOD_DRUG);
        }

        if (sAll.size() < sNum + sNumEx && MoneyMgr.spend(sCost)) {
            Food food = new Food(sType);
            sAll.add(food);
            return food;
        }

        return null;
    }

    public static Food newFood(int type) {
        Food food = new Food(type, true);
        sAll.add(food);
        return food;
    }

    public static List<Food> getAll() {
        return sAll;
    }

    public static List<Food> getDrug() {
        List<Food> drugs = new ArrayList<>();

        for (int i = 0; i < sAll.size(); i++) {
            if (sAll.get(i).getType() == FOOD_DRUG) {
                drugs.add(sAll.get(i));
            }
        }

        return drugs;
    }

    public static int getType() {
        return sType;
    }

    public static void enableDrug() {
        sDrugEnabled = !sDrugEnabled;
        sDrugBox.mark(sDrugEnabled);
    }

    public static void speedScale(float scale) {
        sSpeedScale *= scale;
    }
}