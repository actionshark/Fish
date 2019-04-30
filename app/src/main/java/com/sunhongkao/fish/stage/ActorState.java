package com.sunhongkao.fish.stage;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import com.sunhongkao.fish.engine.AsActivity;
import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.engine.Util;
import com.sunhongkao.fish.iface.IEatee;
import com.sunhongkao.fish.iface.IEater;
import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.iface.IUpdater;
import com.sunhongkao.fish.scene.RoundScene;
import com.sunhongkao.fish.R;


public abstract class ActorState extends Actor {
    private int mStateCount = 0;

    protected State mIdleState = new State();
    protected TurnState mTurnState;
    protected EatState mEatState;
    protected DieState mDieState;

    protected State mCurState = mIdleState;


    @Override
    public void onUpdate() {
        mCurState.onUpdate();

        super.onUpdate();
    }

    public boolean tryTurn(float dx) {
        if (this instanceof ITurnable) {
            if (Util.equals(getCx(), getSeekerX())) {
                if (dx < 0 && mTurned || dx > 0 && !mTurned) {
                    return mCurState.tryState(mTurnState);
                }
            } else {
                if (dx < -getWidth() / 2 && mTurned || dx > getWidth() / 2 && !mTurned) {
                    return mCurState.tryState(mTurnState);
                }
            }
        }

        return false;
    }

    @Override
    public boolean shouldSeek() {
        return mCurState != mDieState;
    }

    @Override
    public float getSeekerX() {
        if ((this instanceof ITurnable) && (this instanceof IEater)) {
            float rate = 0.2f;

            if (mCurState == mTurnState) {
                TiledSprite sprite = mTurnState.getSprite();
                if (sprite != null) {
                    rate = sprite.getCurrentTileIndex() /
                            (float) sprite.getTileCount();
                }

                rate = (rate - 0.5f) / 1.75f + 0.5f;
            }

            if (mTurned) {
                rate = 1f - rate;
            }

            return getX() + getWidth() * rate;
        }

        return getCx();
    }

    @Override
    protected boolean shouldActCnt() {
        return mCurState == mIdleState || mCurState == mTurnState;
    }

    @Override
    protected void onUpdateVertices() {
        super.onUpdateVertices();

        if (mCurState != null) {
            Sprite sprite = mCurState.getSprite();
            if (sprite != null) {
                sprite.setSize(getWidth(), getHeight());
            }
        }
    }

    /*************************************************************************************/

    public class State implements IUpdater {
        private int mId;
        private int mMask = 0;

        protected TiledSprite mSprite;


        public State() {
            mId = mStateCount++;
        }

        public State setResId(int resId) {
            return setResIds(resId, 1);
        }

        public State setResIds(int resId) {
            return setResIds(resId, 10);
        }

        public State setResIds(int resId, int num) {
            return setRegion(AsActivity.it().getRegions(resId, num));
        }

        public State setRegion(ITiledTextureRegion region) {
            if (mSprite != null) {
                if (mSprite.getTiledTextureRegion() == region) {
                    return this;
                }

                mSprite.detachSelf();
            }

            if (region != null) {
                mSprite = new TiledSprite(0, 0, getWidth(), getHeight(),
                        region, AsActivity.it().getVertexBufferObjectManager());

                if (mCurState == this) {
                    attachChild(mSprite);
                }
            }

            return this;
        }

        public TiledSprite getSprite() {
            return mSprite;
        }

        @Override
        public void onUpdate() {
            int next = mSprite.getCurrentTileIndex() + 1;

            if (next < mSprite.getTileCount()) {
                mSprite.setCurrentTileIndex(next);
            } else {
                mSprite.setCurrentTileIndex(0);
            }
        }

        public void start() {
            if (mCurState != this) {
                ActorState.this.detachChild(mCurState.mSprite);

                mCurState = this;

                mSprite.setCurrentTileIndex(0);
                mSprite.setSize(getWidth(), getHeight());
                ActorState.this.attachChild(mSprite);
            }
        }

        public void accept(State... next) {
            for (State state : next) {
                mMask |= 1 << state.mId;
            }
        }

        public boolean tryState(State target) {
            if (target != null && (mMask & (1 << target.mId)) != 0) {
                target.start();
                return true;
            }

            return false;
        }
    }

    /*************************************************************************************/

    public class TurnState extends State {
        @Override
        public void onUpdate() {
            super.onUpdate();

            if (mSprite != null) {
                if (mSprite.getCurrentTileIndex() == 0) {
                    if (ActorState.this instanceof ITurnable) {
                        mTurned = !mTurned;
                    }

                    mIdleState.start();
                }
            }
        }
    }

    /*************************************************************************************/

    public class EatState extends State {
        public IEatee mEatee;

        @Override
        public void onUpdate() {
            super.onUpdate();

            if (mSprite.getCurrentTileIndex() == 0) {
                mIdleState.start();
            } else if (mSprite.getTileCount() - 1 == mSprite.getCurrentTileIndex()) {
                if (ActorState.this instanceof IEater && mEatee != null) {
                    ((IEater) ActorState.this).eat(mEatee);
                    mEatee = null;
                }
            }
        }
    }

    /*************************************************************************************/

    public class DieState extends State {
        public int mDelay = 0;
        public float mSpeed = SeekAssist.SPEED_SLOW;


        @Override
        public void start() {
            super.start();

            mDelay = 0;
            AsEngine.it().playSound(R.raw.sd_fish_die);
        }

        @Override
        public void onUpdate() {
            int next = mSprite.getCurrentTileIndex() + 1;

            if (next < mSprite.getTileCount()) {
                mSprite.setCurrentTileIndex(next);
            } else if (mDelay++ < Util.TIMES_SHORT) {
                if (mSeekAction.speedY != 0) {
                    if (getY() < RoundScene.tankBottom()) {
                        changeY(mSpeed);
                    }
                }
            } else {
                detachSelf();
            }
        }
    }
}