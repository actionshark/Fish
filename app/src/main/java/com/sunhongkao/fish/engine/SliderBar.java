package com.sunhongkao.fish.engine;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import com.sunhongkao.fish.R;


public class SliderBar extends Rectangle {
    private Sprite mBody;
    private Sprite mLeft;
    private Sprite mRight;
    private Sprite mBtn;

    private float mPercent = 0;

    private SliderListener mListener;


    public SliderBar(float x, float y, float width, float height) {
        super(x, y, width, height, AsActivity.it()
                .getVertexBufferObjectManager());

        setAlpha(0);

        mBody = new Sprite(0, 0, 1, 1, AsActivity.it().
                getRegion(R.drawable.cp_slider_body_15_15),
                AsActivity.it().getVertexBufferObjectManager());
        attachChild(mBody);

        mLeft = new Sprite(0, 0, 1, 1, AsActivity.it().
                getRegion(R.drawable.cp_slider_head_15_15),
                AsActivity.it().getVertexBufferObjectManager());
        attachChild(mLeft);

        mRight = new Sprite(0, 0, 1, 1, AsActivity.it().
                getRegion(R.drawable.cp_slider_head_15_15),
                AsActivity.it().getVertexBufferObjectManager());
        mRight.setFlipped(true, false);
        attachChild(mRight);

        mBtn = new Sprite(0, 0, 1, 1, AsActivity.it().
                getRegion(R.drawable.cp_slider_btn_23_21),
                AsActivity.it().getVertexBufferObjectManager());
        attachChild(mBtn);

        onSizeChanged();
    }

    public float getRealWidth() {
        return getWidth();
    }

    public float getRealHeight() {
        return getHeight() * 0.5f;
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
        onSizeChanged();
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(height);
        onSizeChanged();
    }

    protected void onSizeChanged() {
        float width = getWidth();
        float height = getRealHeight();

        mBody.setPosition(height - 1, height * 0.5f);
        mBody.setWidth(width - height * 2 + 2);
        mBody.setHeight(height);

        mLeft.setPosition(0, height * 0.5f);
        mLeft.setWidth(height);
        mLeft.setHeight(height);

        mRight.setPosition(width - height, height * 0.5f);
        mRight.setWidth(height);
        mRight.setHeight(height);

        mBtn.setPosition(height, height * 0.5f);
        mBtn.setWidth(height);
        mBtn.setHeight(height);

        setPercent(getPercent());
    }

    @Override
    public boolean onAreaTouched(TouchEvent event, float x, float y) {
        float width = getRealWidth();
        float height = getRealHeight();

        x -= height * 0.5f;
        if (x < height * 0.5f) {
            x = height * 0.5f;
        } else if (x > width - height * 1.5f) {
            x = width - height * 1.5f;
        }

        mBtn.setPosition(x, mBtn.getY());
        mPercent = (x - height * 0.5f) / (width - height * 2);

        SliderListener listener = mListener;
        if (listener != null) {
            listener.onSlide(this, mPercent);
        }

        return true;
    }

    public void setPercent(float percent) {
        mPercent = percent;

        float width = getWidth();
        float height = getRealHeight();
        float x = percent * (width - height * 2) + height * 0.5f;

        if (x < height * 0.5f) {
            x = height * 0.5f;
        } else if (x > width - height * 1.5f) {
            x = width - height * 1.5f;
        }

        mBtn.setPosition(x, mBtn.getY());
    }

    public float getPercent() {
        return mPercent;
    }

    public void setListener(SliderListener listener) {
        mListener = listener;
    }

    public static interface SliderListener {
        public void onSlide(SliderBar sliderBar, float percent);
    }
}
