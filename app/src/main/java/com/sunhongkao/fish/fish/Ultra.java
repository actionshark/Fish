package com.sunhongkao.fish.fish;

import java.util.ArrayList;
import java.util.List;

import org.andengine.util.math.MathUtils;

import com.sunhongkao.fish.R;
import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.engine.Util;
import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.stage.MoneyMgr;
import com.sunhongkao.fish.stage.StageItem;


public class Ultra extends Fish implements ITurnable {
    public Ultra() {
        setSize(SIZE_BIG, SIZE_BIG);

        mMoneyType = MoneyMgr.MONEY_BOX;

        mResIds = new int[][]{{
                R.drawable.fs_ultra_idle_800_80, R.drawable.fs_ultra_turn_800_80,
                R.drawable.fs_ultra_eat_800_80, R.drawable.fs_ultra_die_800_80}, {
                R.drawable.fs_ultra_idle_800_80, R.drawable.fs_ultra_turn_800_80,
                R.drawable.fs_ultra_eat_800_80, R.drawable.fs_ultra_die_800_80}};

        mSeekAction.speedX = mSeekAction.speedY = SeekAssist.SPEED_FAST;

        mActLine = (int) (Util.TIMES_LONGER * MathUtils.random(0.9f, 1.1f));

        reload();
    }

    @Override
    protected StageItem wantEat() {
        List<Fish> all = FishMgr.getAll();
        List<Carn> list = new ArrayList<>();

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i) instanceof Carn) {
                list.add((Carn) all.get(i));
            }
        }

        return SeekAssist.seek(this, list);
    }
}