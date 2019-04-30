package com.sunhongkao.fish.stage;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import com.sunhongkao.fish.engine.AsActivity;
import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.iface.IUpdater;


public class StageItem extends Rectangle implements IUpdater {
    protected TiledSprite mSprite;


    public StageItem() {
        super(0, 0, 100, 100, AsActivity.it().getVertexBufferObjectManager());

        setAlpha(0);
    }

    public boolean attachSelf() {
        if (hasParent()) {
            return false;
        }

        AsEngine.it().getScene().attachChild(this);
        return true;
    }

    public boolean attachAfter(StageItem prev) {
        if (hasParent() || prev == null || !prev.hasParent()) {
            return false;
        }

        final IEntity parent = prev.getParent();
        boolean find = false;

        for (int i = 0; i < parent.getChildCount(); i++) {
            IEntity entity = parent.getChildByIndex(i);

            if (find) {
                entity.setZIndex(i + 1);
            } else {
                entity.setZIndex(i);

                if (entity == prev) {
                    find = true;
                    setZIndex(i + 1);
                }
            }
        }

        if (find) {
            parent.attachChild(this);
            parent.sortChildren();
            return true;
        }

        return false;
    }

    public boolean attachBefore(StageItem next) {
        if (hasParent() || next == null || !next.hasParent()) {
            return false;
        }

        final IEntity parent = next.getParent();
        boolean find = false;

        for (int i = 0; i < parent.getChildCount(); i++) {
            IEntity entity = parent.getChildByIndex(i);

            if (find) {
                entity.setZIndex(i + 1);
            } else {
                if (entity == next) {
                    find = true;
                    setZIndex(i);
                    entity.setZIndex(i + 1);
                } else {
                    entity.setZIndex(i);
                }

            }
        }

        if (find) {
            parent.attachChild(this);
            parent.sortChildren();
            return true;
        }

        return false;
    }

    @Override
    public void attachChild(IEntity entity) {
        if (this instanceof ITurnable && entity instanceof Sprite) {
            ((Sprite) entity).setFlippedHorizontal(((ITurnable) this).isTurned());
        }

        super.attachChild(entity);
    }

    @Override
    public void onManagedUpdate(float second) {
        super.onManagedUpdate(second);

        onUpdate();
    }

    @Override
    public void onUpdate() {
        if (mSprite != null) {
            int next = mSprite.getCurrentTileIndex() + 1;

            if (next < mSprite.getTileCount()) {
                mSprite.setCurrentTileIndex(next);
            } else {
                mSprite.setCurrentTileIndex(0);
            }
        }
    }

    public void setResId(int resId) {
        setResIds(resId, 1);
    }

    public void setResIds(int resId) {
        setResIds(resId, 10);
    }

    public void setResIds(int resId, int num) {
        setRegion(AsActivity.it().getRegions(resId, num));
    }

    public void setRegion(ITiledTextureRegion region) {
        if (mSprite != null) {
            if (mSprite.getTiledTextureRegion() == region) {
                return;
            }

            mSprite.detachSelf();
        }

        if (region != null) {
            mSprite = new TiledSprite(0, 0, getWidth(), getHeight(),
                    region, AsActivity.it().getVertexBufferObjectManager());
            attachChild(mSprite);
        }
    }

    public float getCx() {
        return getX() + getWidth() / 2;
    }

    public float getCy() {
        return getY() + getHeight() / 2;
    }

    public void setCx(float cx) {
        setX(cx - getWidth() / 2);
    }

    public void setCy(float cy) {
        setY(cy - getHeight() / 2);
    }

    public void setCenter(float cx, float cy) {
        setPosition(cx - getWidth() / 2, cy - getHeight() / 2);
    }

    public void changeX(float delta) {
        setX(getX() + delta);
    }

    public void changeY(float delta) {
        setY(getY() + delta);
    }

    public void changePostion(float dx, float dy) {
        setPosition(getX() + dx, getY() + dy);
    }

    @Override
    protected void onUpdateVertices() {
        super.onUpdateVertices();

        if (mSprite != null) {
            mSprite.setSize(getWidth(), getHeight());
        }
    }
}