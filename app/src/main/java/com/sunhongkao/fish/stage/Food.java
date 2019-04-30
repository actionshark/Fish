package com.sunhongkao.fish.stage;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.TiledSprite;

import com.sunhongkao.fish.engine.AsActivity;
import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.iface.IEatee;
import com.sunhongkao.fish.iface.IMover;
import com.sunhongkao.fish.iface.ITypeble;
import com.sunhongkao.fish.scene.RoundScene;
import com.sunhongkao.fish.R;


public class Food extends StageItem implements IEatee, ITypeble, IMover {
    private float mSpeed = SeekAssist.SPEED_SLOW * FoodMgr.sSpeedScale;
    private int mDelay = 35;

    private int mType = -1;
    private int mMove = IMover.TYPE_DOWN;
    private boolean mExtra = false;


    Food(int type) {
        this(type, false);
    }

    Food(int type, boolean extra) {
        setSize(Actor.SIZE_TINY, Actor.SIZE_TINY);
        mExtra = extra;

        setType(type);

        if (mExtra) {
            FoodMgr.sNumEx++;
        }
    }

    @Override
    public boolean detachSelf() {
        if (super.detachSelf()) {
            if (mExtra) {
                FoodMgr.sNumEx--;
            }

            FoodMgr.sAll.remove(this);
            return true;
        }

        return false;
    }

    @Override
    public int getType() {
        return mType;
    }

    @Override
    public boolean setType(int type) {
        if (FoodMgr.FOOD_LOW > type || type > FoodMgr.FOOD_DRUG) {
            return false;
        }
        if (mType == type) {
            return true;
        }
        mType = type;

        TiledSprite sprite = mSprite;
        if (sprite != null) {
            sprite.detachSelf();
        }

        sprite = mSprite = new AnimatedSprite(0, 0, getWidth(), getHeight(),
                AsActivity.it().getRegions(FoodMgr.RESID[mType]),
                AsActivity.it().getVertexBufferObjectManager());
        attachChild(sprite);

        return true;
    }

    @Override
    public int getMove() {
        return mMove;
    }

    @Override
    public void setMove(int move) {
        mMove = move;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        float y = getY();

        if (mMove == Food.TYPE_UP_DOWN) {
            if (y > 0) {
                changeY(-mSpeed);
            } else {
                mMove = Food.TYPE_DOWN;
            }
        } else {
            if (y + getHeight() / 2 < RoundScene.tankBottom()) {
                changeY(mSpeed);
            } else if (mDelay > 0) {
                mDelay--;
            } else {
                detachSelf();
            }
        }
    }

    @Override
    public int eaten() {
        if (detachSelf()) {
            AsEngine.it().playSound(R.raw.sd_eat_food);
            return 450 + 200 * mType;
        }

        return 0;
    }
}