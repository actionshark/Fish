package com.sunhongkao.fish.fish;

import java.util.ArrayList;
import java.util.List;

import com.sunhongkao.fish.R;
import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.stage.Money;
import com.sunhongkao.fish.stage.MoneyMgr;
import com.sunhongkao.fish.stage.StageItem;


public class Pearler extends Fish implements ITurnable {
    public Pearler() {
        setSize(SIZE_UP, SIZE_UP);

        mMoneyType = MoneyMgr.MONEY_PEARL_B;

        mResIds = new int[][]{{
                R.drawable.fs_pearler_idle_800_80, R.drawable.fs_pearler_turn_800_80,
                R.drawable.fs_pearler_eat_800_80, R.drawable.fs_pearler_die_800_80}, {
                R.drawable.fs_pearler_hungry_idle_800_80, R.drawable.fs_pearler_hungry_turn_800_80,
                R.drawable.fs_pearler_hungry_eat_800_80}};

        mSeekAction.speedX = mSeekAction.speedY = SeekAssist.SPEED_SUPER;

        reload();
    }

    @Override
    protected StageItem wantEat() {
        List<Money> all = MoneyMgr.getAll();
        List<Money> list = new ArrayList<Money>();

        for (int i = 0; i < all.size(); ++i) {
            if (all.get(i).getType() == MoneyMgr.MONEY_WORM) {
                list.add(all.get(i));
            }
        }

        return SeekAssist.seek(this, list);
    }
}