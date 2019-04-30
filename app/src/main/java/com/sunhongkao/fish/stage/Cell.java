package com.sunhongkao.fish.stage;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.util.color.Color;
import org.andengine.util.math.MathUtils;

import com.sunhongkao.fish.engine.AsActivity;


public class Cell extends Rectangle {
    private static final float UNIT = 80;
    private static final float GAP = 3.6f;


    public Cell() {
        super(0, 0, UNIT, UNIT, AsActivity.it().getVertexBufferObjectManager());
    }

    public static Color randColor() {
        return new Color(MathUtils.random(0f, 1f),
                MathUtils.random(0f, 1f), MathUtils.random(0f, 1f));
    }

    public void setColor(Color color, boolean pure) {
        if (pure) {
            float red = color.getRed();
            float gre = color.getGreen();
            float blu = color.getBlue();

            if (red <= gre && red <= blu) {
                color.setRed(0);
            } else if (gre <= red && gre <= blu) {
                color.setGreen(0);
            } else {
                color.setBlue(0);
            }
        }

        setColor(color);
    }

    public void setSize(int width, int height) {
        setWidth(UNIT * width - GAP - GAP);
        setHeight(UNIT * height - GAP - GAP);
    }

    public void setPosition(int left, int top) {
        setX(UNIT * left + GAP);
        setY(UNIT * top + GAP);
    }

    public Color getCompleColor() {
        return new Color(1 - getRed(), 1 - getGreen(), 1 - getBlue());
    }
}