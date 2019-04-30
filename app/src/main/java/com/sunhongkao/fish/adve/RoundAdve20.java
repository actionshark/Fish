package com.sunhongkao.fish.adve;

import org.andengine.input.touch.TouchEvent;
import org.andengine.util.math.MathUtils;

import com.sunhongkao.fish.R;
import com.sunhongkao.fish.engine.AsActivity;
import com.sunhongkao.fish.engine.Deliver;
import com.sunhongkao.fish.engine.Util;
import com.sunhongkao.fish.fish.Victim;
import com.sunhongkao.fish.mons.Boss;
import com.sunhongkao.fish.mons.Monster;
import com.sunhongkao.fish.mons.MonsterMgr;
import com.sunhongkao.fish.pet.Pet;


public class RoundAdve20 extends RoundAdveBase {
    @Override
    public void onCreate(Deliver deliver) {
        super.onCreate(deliver);

        mMoneyText.text().setText("");

        final boolean[] jump = new boolean[]{
                false, false, true, true, true,
                true, true, false, true, true,
                true, true, true, true, false,
                true, true, true, true, true
        };

        for (int i = 0; i < jump.length; i++) {
            addActor(new Victim("pt_" + Pet.PETS[i], jump[i]));
        }

        new Boss(MonsterMgr.HEALTH_HIGHER * 2).attachSelf();

        mMonsterLine = Util.TIMES_UP;
        mMonsterCnt = 0;
    }

    @Override
    public String getName() {
        return AsActivity.it().getString(R.string.wd_round) + " 5";
    }

    @Override
    protected int getGunCost() {
        return 0;
    }

    @Override
    protected int getEggCost() {
        return 0;
    }

    @Override
    public void wantMonster() {
        addMonster();

        if (MathUtils.random(0.1f)) {
            addMonster();
        }
    }

    protected void addMonster() {
        Monster mst = MonsterMgr.newMonster(
                MathUtils.random(0, 6), MonsterMgr.HEALTH_LOWER);
        mst.enableFinal(true);
        super.addMonster(mst);
    }

    @Override
    public boolean onTouchEvent(TouchEvent event) {
        return false;
    }
}