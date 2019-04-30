package com.sunhongkao.fish.pet;

import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.Util;
import com.sunhongkao.fish.iface.IMover;
import com.sunhongkao.fish.round.RoundMgr;
import com.sunhongkao.fish.stage.Money;
import com.sunhongkao.fish.stage.MoneyMgr;
import com.sunhongkao.fish.R;


public class Clam extends Pet {
    private final ActState mActState = new ActState();
    private final CloseState mCloseState = new CloseState();


    public Clam() {
        mIdleState.setResIds(R.drawable.pt_clam_idle_800_80);
        mIdleState.accept(mActState);
        mActState.accept(mCloseState);
        mCloseState.accept(mIdleState);

        mActLine = Util.TIMES_LONGER;

        mSeekAction.speedX = mSeekAction.speedY = 0;
    }

    @Override
    public boolean act() {
        return mCurState.tryState(mActState);
    }

    @Override
    public boolean shouldSeek() {
        return false;
    }

    @Override
    public void initPosition() {
        switch (RoundMgr.getRound().getMajor()) {
            case 0:
                setPosition(102, 216);
                break;

            case 1:
                setPosition(186, 144);
                break;

            case 2:
                setPosition(154, 230);
                break;

            case 3:
                setPosition(77, 168);
                break;

            default:
                super.initPosition();
        }
    }


    private class ActState extends State {
        private int mDelayMax = Util.TIMES_DOWN;
        private int mDelay;

        private Money mPearl;


        @Override
        public void start() {
            setResIds(R.drawable.pt_clam_act_has_800_80);

            super.start();

            mDelay = 0;
        }

        @Override
        public void onUpdate() {
            if (mSprite.getCurrentTileIndex() + 1 < mSprite.getTileCount()) {
                mSprite.setCurrentTileIndex(mSprite.getCurrentTileIndex() + 1);
            } else if (mDelay < mDelayMax) {
                if (mDelay == 0) {
                    mPearl = MoneyMgr.newMoney(MoneyMgr.MONEY_PEARL_L,
                            IMover.TYPE_STILL);
                    mPearl.setCx(getCx());
                    mPearl.setCy(getCy());

                    mPearl.attachAfter(Clam.this);
                    AsEngine.it().playSound(R.raw.sd_clam_work);
                }

                if (!mPearl.hasParent()) {
                    setResIds(R.drawable.pt_clam_act_no_800_80);
                    mSprite.setCurrentTileIndex(mSprite.getTileCount() - 1);
                }

                mDelay++;
            } else {
                if (mPearl.hasParent()) {
                    mPearl.detachSelf();
                } else {
                    setResIds(R.drawable.pt_clam_act_no_800_80);
                    mSprite.setCurrentTileIndex(mSprite.getTileCount() - 1);
                }

                mCurState.tryState(mCloseState);
            }
        }
    }

    /*************************************************************/

    private class CloseState extends State {
        @Override
        public void start() {
            setRegion(mActState.getSprite().getTiledTextureRegion());

            super.start();

            mSprite.setCurrentTileIndex(mSprite.getTileCount() - 1);
        }

        @Override
        public void onUpdate() {
            if (mSprite.getCurrentTileIndex() > 0) {
                mSprite.setCurrentTileIndex(mSprite.getCurrentTileIndex() - 1);
            } else {
                mCurState.tryState(mIdleState);
            }
        }
    }
}