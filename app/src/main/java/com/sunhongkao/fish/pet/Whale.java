package com.sunhongkao.fish.pet;

import java.util.ArrayList;
import java.util.List;

import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.engine.Util;
import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.fish.FishMgr;
import com.sunhongkao.fish.fish.Guppy;
import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.mons.MonsterMgr;
import com.sunhongkao.fish.stage.StageItem;
import com.sunhongkao.fish.R;


public class Whale extends Pet implements ITurnable {
    private final List<Guppy> mGuppys = new ArrayList<>();
    private ActState mActState = new ActState();
    private boolean mProtected = false;


    public Whale() {
        setSize(SIZE_LARGE, SIZE_LARGE);

        mIdleState.setResIds(R.drawable.pt_whale_idle_800_80);
        mTurnState = (TurnState) new TurnState().setResIds
                (R.drawable.pt_whale_turn_800_80);
        mIdleState.accept(mTurnState, mActState);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (MonsterMgr.getAll().isEmpty()) {
            if (mGuppys.size() > 0 && mCurState.tryState(mActState)) {
                mProtected = false;
            }
        } else if (!mProtected) {
            if (mCurState.tryState(mActState)) {
                mProtected = true;

                List<Fish> fishs = FishMgr.getAll();
                for (int i = 0; i < fishs.size(); i++) {
                    if (fishs.get(i) instanceof Guppy) {
                        fishs.get(i).seek(getCx(), getCy());
                    }
                }
            }
        }
    }

    @Override
    public boolean detachSelf() {
        if (super.detachSelf()) {
            for (int i = 0; i < mGuppys.size(); i++) {
                Guppy guppy = mGuppys.get(i);
                guppy.setCx(getCx());
                guppy.setCy(getCy());
                guppy.seek(getCx(), getCy());
                FishMgr.show(guppy);
            }

            mGuppys.clear();
            return true;
        }

        return false;
    }

    @Override
    public boolean shouldSeek() {
        if (mCurState == mActState) {
            return false;
        }

        return super.shouldSeek();
    }


    private class ActState extends State {
        private int mMax = (Util.TIMES_SHORT + Util.TIMES_DOWN) / 2;
        private int mCount;


        public ActState() {
            setResIds(R.drawable.pt_whale_act_800_80);
        }

        @Override
        public void start() {
            super.start();
            mCount = 0;
        }

        @Override
        public void onUpdate() {
            super.onUpdate();

            if (mProtected) {
                List<StageItem> list = SeekAssist.touch
                        (Whale.this, FishMgr.getAll());
                for (int i = 0; i < list.size(); i++) {
                    StageItem item = list.get(i);
                    if (item instanceof Guppy) {
                        mGuppys.add((Guppy) item);
                        FishMgr.hide((Guppy) item);
                    }
                }
            }

            int index = mSprite.getCurrentTileIndex();

            if (index == 5) {
                if (++mCount < mMax) {
                    mSprite.setCurrentTileIndex(4);
                }

                if (!mProtected) {
                    for (int i = 0; i < mGuppys.size(); i++) {
                        Guppy guppy = mGuppys.get(i);
                        guppy.setCx(getCx());
                        guppy.setCy(getCy());
                        guppy.seek(getCx(), getCy());
                        FishMgr.show(guppy);
                    }
                    mGuppys.clear();
                }
            } else if (index == 0) {
                mIdleState.start();
            }
        }
    }

    @Override
    public boolean supportMulti() {
        return false;
    }
}