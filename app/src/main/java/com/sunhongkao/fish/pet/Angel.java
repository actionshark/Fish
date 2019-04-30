package com.sunhongkao.fish.pet;

import java.util.ArrayList;
import java.util.List;

import org.andengine.util.math.MathUtils;

import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.fish.FishMgr;
import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.stage.StageItem;
import com.sunhongkao.fish.R;


public class Angel extends Pet implements ITurnable {
    public Angel() {
        mIdleState.setResIds(R.drawable.pt_angel_idle_800_80);
        mTurnState = (TurnState) new TurnState().setResIds(R.drawable.pt_angel_turn_800_80);
        mIdleState.accept(mTurnState);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        List<StageItem> touch = SeekAssist.touch(this, FishMgr.getAll());
        for (int i = 0; i < touch.size(); i++) {
            ((Fish) touch.get(i)).revive();
        }

        if (mSeekAction.seekee == null || !((Fish) mSeekAction.seekee).isDying()) {
            List<Fish> fishs = FishMgr.getAll();
            List<Fish> dying = new ArrayList<Fish>();

            for (int i = 0; i < fishs.size(); ++i) {
                if (fishs.get(i).isDying()) {
                    dying.add(fishs.get(i));
                }
            }

            if (dying.size() > 0) {
                seek(dying.get(MathUtils.random(0, dying.size() - 1)));
            }
        }
    }
}