package com.sunhongkao.fish.engine;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.HorizontalAlign;


public class AsButton extends Rectangle {
    protected Sprite mBg;
    protected AsText mText;


    public AsButton() {
        super(0, 0, 10, 10, AsActivity.it().getVertexBufferObjectManager());

        setAlpha(0);
    }

    @Override
    public void onUpdateVertices() {
        super.onUpdateVertices();

        if (mBg != null) {
            mBg.setSize(getWidth(), getHeight());
        }

        if (mText != null) {
            if (HorizontalAlign.LEFT == mText.getHorizontalAlign()) {
                mText.setX(0);
            } else if (HorizontalAlign.RIGHT == mText.getHorizontalAlign()) {
                mText.setX(getWidth());
            } else {
                mText.setX(getWidth() / 2);
            }

            mText.setY(getHeight() / 2);
        }
    }

    public void setRegion(ITextureRegion region) {
        if (mBg != null) {
            if (mBg.getTextureRegion() == region) {
                return;
            }

            mBg.detachSelf();
        }

        mBg = new Sprite(0, 0, getWidth(), getHeight(), region,
                AsActivity.it().getVertexBufferObjectManager());
        attachChild(mBg);
    }

    protected void createTextIfNull() {
        if (mText == null) {
            mText = new AsText();
            attachChild(mText);
            onUpdateVertices();
        }
    }

    public AsText text() {
        createTextIfNull();

        return mText;
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
}