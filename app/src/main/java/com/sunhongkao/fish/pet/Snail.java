package com.sunhongkao.fish.pet;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.sprite.TiledSprite;

import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.engine.Util;
import com.sunhongkao.fish.iface.IMover;
import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.stage.Money;
import com.sunhongkao.fish.stage.MoneyMgr;
import com.sunhongkao.fish.stage.StageItem;
import com.sunhongkao.fish.R;


public class Snail extends Pet implements ITurnable {
    private float mDx = 0;


    public Snail() {
        mIdleState.setResIds(R.drawable.pt_snail_idle_800_80);
        mTurnState = (TurnState) new TurnState().setResIds
                (R.drawable.pt_snail_turn_800_80);
        mIdleState.accept(mTurnState);

        mSeekAction.speedX = SeekAssist.SPEED_DOWN;
        mSeekAction.speedY = 0;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        List<StageItem> moneys = SeekAssist.touch(this, MoneyMgr.getAll());
        for (int i = 0; i < moneys.size(); i++) {
            ((Money) moneys.get(i)).collect();
        }

        if (mSeekAction.seekee == null) {
            List<Money> all = MoneyMgr.getAll();
            List<Money> list = new ArrayList<>();

            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getMove() == IMover.TYPE_DOWN) {
                    list.add(all.get(i));
                }
            }

            mSeekAction.seek(SeekAssist.seek(getCx(), getCy(), list));
        }

        if (Util.equals(mDx, 0)) {
            if (mCurState == mIdleState) {
                TiledSprite sprite = mCurState.getSprite();
                int index = sprite.getCurrentTileIndex();

                if (index <= 0) {
                    sprite.setCurrentTileIndex(sprite.getTileCount() - 1);
                } else {
                    sprite.setCurrentTileIndex(index - 1);
                }
            }
        } else {
            mDx = 0;
        }
    }

    @Override
    public void onSeekMove(float dx, float dy) {
        super.onSeekMove(dx, dy);
        mDx = dx;
    }
}