package com.sunhongkao.fish.mons;

import java.util.ArrayList;
import java.util.List;

import org.andengine.input.touch.TouchEvent;

import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.fish.FishMgr;
import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.stage.ActorState;
import com.sunhongkao.fish.stage.Gun;
import com.sunhongkao.fish.stage.StageItem;
import com.sunhongkao.fish.R;


public class DualHeads extends Monster {
    private DualHeads mOther;
    private final Body[] mBodies = new Body[7];

    private boolean mActive = false;

    private int mFollowMax = 300;
    private int mFollowCnt = 0;


    public DualHeads(int health) {
        super(health / 2);

        mOther = new DualHeads(health / 2, 0);
        mOther.mOther = this;

        setSize(SIZE_UP, SIZE_UP);
        mOther.setSize(SIZE_UP, SIZE_UP);

        mSeekAction.speedX = mSeekAction.speedY = mOther.mSeekAction.speedX
                = mOther.mSeekAction.speedY = SeekAssist.SPEED_DOWN;

        mIdleState.setResIds(R.drawable.ms_dualheads_heada_800_80);
        mTurnState.setResIds(R.drawable.ms_dualheads_heada_800_80);

        mOther.mIdleState.setResIds(R.drawable.ms_dualheads_headb_800_80);
        mOther.mTurnState.setResIds(R.drawable.ms_dualheads_headb_800_80);

        mActive = true;
        mOther.mActive = false;

        int i;
        for (i = 0; i < mBodies.length; i++) {
            mBodies[i] = new Body();
            mOther.mBodies[mBodies.length - 1 - i] = mBodies[i];
        }

        mBodies[0].setFollow(this, mBodies[1]);
        mBodies[mBodies.length - 1].setFollow(
                mBodies[mBodies.length - 2], mOther);
        for (i = 1; i < mBodies.length - 1; i++) {
            mBodies[i].setFollow(mBodies[i - 1], mBodies[i + 1]);
        }
    }

    private DualHeads(int health, int other) {
        super(health);
    }

    @Override
    public boolean attachSelf() {
        if (!mActive) {
            return super.attachSelf();
        }

        if (hasParent() || mOther.hasParent()) {
            return false;
        }

        int i;
        for (i = 0; i < mBodies.length; i++) {
            if (mBodies[i].hasParent()) {
                return false;
            }
        }

        super.attachSelf();
        float x = getCx();
        float y = getCy();
        mOther.setCenter(x, y);
        mOther.attachSelf();

        for (i = 0; i < mBodies.length; i++) {
            mBodies[i].setCenter(x, y);
            mBodies[i].attachSelf();
        }

        return true;
    }

    @Override
    public boolean detachSelf() {
        if (!mOther.hasParent()) {
            for (Body body : mBodies) {
                body.detachSelf();
            }
        } else if (mActive) {
            mActive = false;
            mOther.mActive = true;
            for (Body body : mBodies) {
                body.turnHead();
            }
        }

        return super.detachSelf();
    }

    @Override
    public void enableFinal(boolean enable) {
        super.enableFinal(enable);
        mOther.mFinal = enable;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        mIdleState.getSprite().setCurrentTileIndex(0);

        List<StageItem> list = SeekAssist.touch(this, FishMgr.getAll());
        for (int i = 0; i < list.size(); i++) {
            ((Fish) list.get(i)).killed();
        }

        if (mActive && mOther.hasParent() && ++mFollowCnt > mFollowMax) {
            mFollowCnt = 0;

            mActive = false;
            mOther.mActive = true;
            for (Body body : mBodies) {
                body.turnHead();
            }
        }

        if (!mActive) {
            mSeekDelay = 0;
            seek(mBodies[0]);
        }
    }

    @Override
    public boolean onAreaTouched(TouchEvent event, float x, float y) {
        if (mActive) {
            return super.onAreaTouched(event, x, y);
        }

        if (event.getAction() == TouchEvent.ACTION_UP
                && harmed(Gun.attackValue())) {

            AsEngine.it().playSound(R.raw.sd_gun);
            return true;
        }

        return false;
    }
}


class Body extends ActorState implements ITurnable {
    private ActorState mPrev;
    private ActorState mNext;

    private final List<Point> mPoints = new ArrayList<>();


    Body() {
        setSize(SIZE_UP, SIZE_UP);

        mIdleState.setResIds(R.drawable.ms_dualheads_body_800_80);
        mTurnState = (TurnState) new TurnState().setResIds
                (R.drawable.ms_dualheads_body_800_80);

        mSeekAction.speedX = mSeekAction.speedY
                = SeekAssist.SPEED_DOWN;
    }

    protected void setFollow(ActorState prev, ActorState next) {
        mPrev = prev;
        mNext = next;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        mIdleState.getSprite().setCurrentTileIndex(0);

        mPoints.add(new Point(mPrev.getCx(), mPrev.getCy()));

        if (mPoints.size() > 4) {
            Point p = mPoints.get(0);
            mPoints.remove(p);
            seek(p.x, p.y);
        }
    }

    protected void turnHead() {
        ActorState tmp = mPrev;
        mPrev = mNext;
        mNext = tmp;
    }
}

class Point {
    public float x;
    public float y;


    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }
}