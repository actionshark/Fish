package com.sunhongkao.fish.round;

import com.sunhongkao.fish.engine.AsActivity;
import com.sunhongkao.fish.engine.Deliver;
import com.sunhongkao.fish.engine.Logger;


public class RoundMgr {
    public static final String KEY_TYPE = "roundmgr_type";
    public static final String KEY_INDEX = "roundmgr_index";
    public static final String KEY_SCENE = "roundmgr_scene";
    public static final String KEY_PROP_BOX = "roundmgr_prop_box";
    public static final String KEY_MONEY_TEXT = "roundmgr_money_text";

    public static final int TYPE_ADVE = 1;
    public static final int TYPE_PETS = 2;
    public static final int TYPE_CHAL = 3;

    public static final int STATE_LOCKED = 0;
    public static final int STATE_UNLOCKED = 1;
    public static final int STATE_PASSED = 2;

    private static RoundBase sRound;


    public static RoundBase newRound(Deliver deliver) {
        int type = deliver.getInt(KEY_TYPE);
        int index = deliver.getInt(KEY_INDEX);

        String name = AsActivity.it().getPackageName();
        switch (type) {
            case TYPE_ADVE:
                name += ".adve.RoundAdve";
                break;

            case TYPE_PETS:
                name += ".round.RoundPet";
                break;

            case TYPE_CHAL:
                name += ".round.RoundChal";
                break;
        }

        name += index;

        try {
            sRound = (RoundBase) Class.forName(name).newInstance();
            sRound.init(type, index);
            return sRound;
        } catch (Exception e) {
            Logger.print(e);
        }

        return null;
    }

    public static RoundBase getRound() {
        return sRound;
    }
}