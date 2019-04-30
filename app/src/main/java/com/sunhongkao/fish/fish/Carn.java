package com.sunhongkao.fish.fish;

import java.util.ArrayList;
import java.util.List;

import com.sunhongkao.fish.R;
import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.stage.MoneyMgr;
import com.sunhongkao.fish.stage.StageItem;


public class Carn extends Fish implements ITurnable {
    public Carn() {
        setSize(SIZE_UP, SIZE_UP);

        mMoneyType = MoneyMgr.MONEY_DIAMOND_L;

        mResIds = new int[][]{{
                R.drawable.fs_carn_idle_800_80,
                R.drawable.fs_carn_turn_800_80,
                R.drawable.fs_carn_eat_800_80,
                R.drawable.fs_carn_die_800_80}, {
                R.drawable.fs_carn_hungry_idle_800_80,
                R.drawable.fs_carn_hungry_turn_800_80,
                R.drawable.fs_carn_hungry_idle_800_80}};

        mSeekAction.speedX = mSeekAction.speedY = SeekAssist.SPEED_UP;

        reload();
    }

    @Override
    protected StageItem wantEat() {
        List<Fish> list = new ArrayList<>();

        for (int i = 0; i < FishMgr.sAll.size(); i++) {
            Fish fish = FishMgr.sAll.get(i);

            if (fish instanceof Guppy && ((Guppy) fish).
                    getType() == Guppy.GUPPY_SMALL) {
                list.add(fish);
            }
        }

        return SeekAssist.seek(this, list);
    }
}