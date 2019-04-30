package com.sunhongkao.fish.engine;

import java.util.HashMap;
import java.util.Map;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontUtils;
import org.andengine.opengl.font.IFont;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.VerticalAlign;

import android.graphics.Color;
import android.graphics.Typeface;


public class AsText extends Rectangle {
    private static final Typeface TYPEFACE = Typeface.createFromAsset(
            AsActivity.it().getAssets(), "font/roboto.ttf");
    private static final Map<String, Font> sMap = new HashMap<String, Font>();

    protected static Font getFont(float size, int color) {
        String key = size + "," + color;
        if (sMap.containsKey(key)) {
            return sMap.get(key);
        }

        Font font = FontFactory.create(AsActivity.it().getFontManager(),
                AsActivity.it().getTextureManager(), 512, 512,
                TYPEFACE, size, true, color);
        font.load();
        sMap.put(key, font);
        return font;
    }


    protected Text mText;
    protected float mTextSize = 25;
    protected int mTextColor = Color.BLACK;

    protected CharSequence mString = "";
    protected float mLineWidth = 1000;
    protected VerticalAlign mVerticalAlign = VerticalAlign.CENTER;


    public AsText() {
        super(0, 0, 0, 0, AsActivity.it().getVertexBufferObjectManager());

        setAlpha(0);
        onChanged(true);
    }

    protected void onChanged(boolean fontChange) {
        if (fontChange) {
            HorizontalAlign align = HorizontalAlign.CENTER;
            if (mText != null) {
                align = mText.getHorizontalAlign();
                mText.detachSelf();
            }

            mText = new Text(0, 0, getFont(mTextSize, mTextColor), mString, 300,
                    AsActivity.it().getVertexBufferObjectManager());
            mText.setHorizontalAlign(align);
            attachChild(mText);
        }

        IFont font = mText.getFont();
        StringBuilder ret = new StringBuilder();
        StringBuilder tmp = new StringBuilder();
        float max = 0;
        int lineNum = 0;

        for (int i = 0; i < mString.length(); i++) {
            char ch = mString.charAt(i);
            tmp.append(ch);

            float len = FontUtils.measureText(mText.getFont(), tmp);
            if (len > max) {
                max = len;
            }

            if (ch == '\n') {
                ret.append(tmp);
                tmp.setLength(0);
                lineNum++;
            } else if (len >= mLineWidth) {
                ret.append(tmp).append('\n');
                tmp.setLength(0);
                lineNum++;
            }
        }

        if (tmp.length() > 0) {
            float len = FontUtils.measureText(mText.getFont(), tmp);
            if (len > max) {
                max = len;
            }
            ret.append(tmp);
            lineNum++;
        }

        mText.setText(ret);

        if (HorizontalAlign.LEFT == mText.getHorizontalAlign()) {
            mText.setX(0);
        } else if (HorizontalAlign.RIGHT == mText.getHorizontalAlign()) {
            mText.setX(-max);
        } else {
            mText.setX(-max / 2);
        }

        if (VerticalAlign.TOP == mVerticalAlign) {
            mText.setY(0);
        } else if (VerticalAlign.BOTTOM == mVerticalAlign) {
            mText.setY(-font.getLineHeight() * lineNum);
        } else {
            mText.setY(-font.getLineHeight() * lineNum / 2);
        }
    }

    public void setTextSize(float size) {
        mTextSize = size;
        onChanged(true);
    }

    public void setTextColor(int color) {
        mTextColor = color;
        onChanged(true);
    }

    public void setText(int text) {
        setText(AsActivity.it().getString(text));
    }

    public void setText(CharSequence text) {
        mString = text;
        onChanged(false);
    }

    public void appendText(int text) {
        appendText(AsActivity.it().getString(text));
    }

    public void appendText(CharSequence text) {
        setText(mString.toString() + text);
    }

    public void setLineWidth(float width) {
        mLineWidth = width;
        onChanged(false);
    }

    public void setHorizontalAlign(HorizontalAlign hori) {
        mText.setHorizontalAlign(hori);
        onChanged(false);
    }

    public HorizontalAlign getHorizontalAlign() {
        return mText.getHorizontalAlign();
    }

    public void setVerticalAlign(VerticalAlign vert) {
        mVerticalAlign = vert;
        onChanged(false);
    }

    public VerticalAlign getVerticalAlign() {
        return mVerticalAlign;
    }

    public void setAlign(HorizontalAlign hori, VerticalAlign vert) {
        mText.setHorizontalAlign(hori);
        mVerticalAlign = vert;
        onChanged(false);
    }
}