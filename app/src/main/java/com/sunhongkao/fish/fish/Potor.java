package com.sunhongkao.fish.fish;

import java.util.ArrayList;
import java.util.List;

import org.andengine.util.math.MathUtils;

import com.sunhongkao.fish.R;
import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.engine.Util;
import com.sunhongkao.fish.iface.IMover;
import com.sunhongkao.fish.stage.Money;
import com.sunhongkao.fish.stage.MoneyMgr;
import com.sunhongkao.fish.stage.StageItem;


public class Potor extends Fish {
    public Potor() {
        setSize(SIZE_NORMAL, SIZE_NORMAL);

        mMoneyType = MoneyMgr.MONEY_DIAMOND_B;
        mMoneyMove = IMover.TYPE_UP_DOWN;

        mResIds = new int[][]{{
                R.drawable.fs_potor_idle_800_80, 0, R.drawable.fs_potor_idle_800_80,
                R.drawable.fs_potor_die_800_80}, {R.drawable.fs_potor_hungry_800_80,
                0, R.drawable.fs_potor_hungry_800_80}};

        mSeekAction.speedX = SeekAssist.SPEED_NORMAL;
        mSeekAction.speedY = 0;

        mActLine = (int) (Util.TIMES_LONG * MathUtils.random(0.9f, 1.1f));

        reload();
    }

    @Override
    protected StageItem wantEat() {
        List<Money> all = MoneyMgr.getAll();
        List<Money> list = new ArrayList<Money>();

        for (int i = 0; i < all.size(); ++i) {
            Money money = all.get(i);

            if (money.getMove() == IMover.TYPE_DOWN && money.getType() == MoneyMgr.MONEY_STAR) {
                list.add(money);
            }
        }

        return SeekAssist.seek(this, list);
    }
}