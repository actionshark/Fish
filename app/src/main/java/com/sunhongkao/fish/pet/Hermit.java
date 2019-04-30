package com.sunhongkao.fish.pet;

import java.util.List;

import org.andengine.entity.sprite.TiledSprite;

import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.engine.Util;
import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.fish.FishMgr;
import com.sunhongkao.fish.stage.Money;
import com.sunhongkao.fish.stage.MoneyMgr;
import com.sunhongkao.fish.stage.StageItem;
import com.sunhongkao.fish.stage.MoneyMgr.MoneyEventListener;
import com.sunhongkao.fish.R;


public class Hermit extends Pet implements MoneyEventListener {
    private float mDx = 0;


    public Hermit() {
        mIdleState.setResIds(R.drawable.pt_hermit_idle_800_80);

        mSeekAction.speedX = SeekAssist.SPEED_FAST;
        mSeekAction.speedY = 0;
    }

    @Override
    public boolean attachSelf() {
        if (super.attachSelf()) {
            MoneyMgr.addListener(this);
            return true;
        }

        return false;
    }

    @Override
    public boolean detachSelf() {
        if (super.detachSelf()) {
            MoneyMgr.removeListener(this);
            return true;
        }

        return false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        List<StageItem> touch = SeekAssist.touch(this, FishMgr.getAll());
        for (int i = 0; i < touch.size(); i++) {
            Fish tmp = (Fish) touch.get(i);
            if (tmp.canUpDown()) {
                tmp.seek(tmp.getSeekerX(), 0);
            }
        }

        if (mSeekAction.seekee == null || mSeekAction.seekee.getY()
                + mSeekAction.seekee.getHeight() * 2 < getY()) {

            List<Fish> all = FishMgr.getAll();
            Fish fish = null;

            for (int i = 0; i < all.size(); i++) {
                Fish tmp = all.get(i);

                if (tmp.canUpDown() && (fish == null || tmp.getCy() > fish.getCy())) {
                    fish = tmp;
                }
            }

            seek(fish);
        }

        if (Util.equals(mDx, 0)) {
            TiledSprite sprite = mCurState.getSprite();
            int index = sprite.getCurrentTileIndex();

            if (index <= 0) {
                sprite.setCurrentTileIndex(sprite.getTileCount() - 1);
            } else {
                sprite.setCurrentTileIndex(index - 1);
            }
        } else {
            mDx = 0;
        }
    }

    @Override
    public void onMoneyEvent(Money money, int event) {
        if (event == MoneyMgr.EVENT_LOST) {
            MoneyMgr.save(money.getPoint() / 3);
        }
    }

    @Override
    public void onSeekMove(float dx, float dy) {
        super.onSeekMove(dx, dy);
        mDx = dx;
    }
}