package com.sunhongkao.fish.scene;

import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.shape.IOnTouchListener;
import org.andengine.input.touch.TouchEvent;

import com.sunhongkao.fish.engine.AsActivity;
import com.sunhongkao.fish.engine.AsButton;
import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.Deliver;
import com.sunhongkao.fish.engine.Recorder;
import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.round.RoundMgr;
import com.sunhongkao.fish.stage.Cell;
import com.sunhongkao.fish.stage.StageItem;
import com.sunhongkao.fish.R;


public class SelectChalScene extends BaseScene implements
        IOnTouchListener {

    private final AsButton[] mRound = new AsButton[4];


    @Override
    public void onCreate(Deliver deliver) {
        deliver.set(KEY_TITLE, R.string.mode_chal);
        super.onCreate(deliver);

        addCell(1, 1, 0, 0);
        addCell(1, 1, 1, 0);
        mTitle.setTextColor(addCell(4, 1, 2, 0).
                getCompleColor().getARGBPackedInt());
        addCell(1, 1, 6, 0);
        addCell(1, 1, 7, 0);

        addCell(1, 1, 0, 1);
        addCell(1, 1, 1, 1);
        addCell(1, 1, 2, 1);
        addCell(1, 1, 3, 1);
        addCell(1, 1, 4, 1);
        addCell(1, 1, 5, 1);
        addCell(1, 1, 6, 1);
        addCell(1, 1, 7, 1);

        addCell(1, 1, 0, 2);
        addCell(1, 1, 1, 2);
        new Blue(addCell(2, 2, 0, 3)).attachSelf();
        addCell(1, 1, 0, 5);
        addCell(1, 1, 1, 5);

        new RoundText(addCell(3, 2, 2, 2), 0);
        new RoundText(addCell(3, 2, 5, 2), 1);
        new RoundText(addCell(3, 2, 2, 4), 2);
        new RoundText(addCell(3, 2, 5, 4), 3);
    }

    @Override
    public void onResume(Deliver deliver) {
        super.onResume(deliver);

        for (int i = 0; i < mRound.length; i++) {
            mRound[i].text().setText(AsActivity.it().getString(R.string.wd_round) + " " + (i + 1) + "\n");
            switch (Recorder.getRoundChalState(i)) {
                case RoundMgr.STATE_UNLOCKED:
                    mRound[i].text().appendText(R.string.wd_unlocked);
                    mRound[i].setOnTouchListener(this);
                    break;

                case RoundMgr.STATE_PASSED:
                    mRound[i].text().appendText(R.string.wd_passed);
                    mRound[i].setOnTouchListener(this);
                    break;

                default:
                    mRound[i].text().appendText(R.string.wd_locked);
                    mRound[i].setOnTouchListener(null);
                    break;
            }
        }
    }

    @Override
    public boolean onTouched(ITouchArea area, TouchEvent event, float x, float y) {
        if (event.getAction() != TouchEvent.ACTION_UP) {
            return false;
        }

        for (int i = 0; i < mRound.length; i++) {
            if (mRound[i] == area) {
                AsEngine.it().playSound(R.raw.sd_click);

                Deliver del = new Deliver(Deliver.KEY_INTENT, PetScene.INTENT_ROUND);
                del.set(RoundMgr.KEY_TYPE, RoundMgr.TYPE_CHAL);
                del.set(RoundMgr.KEY_INDEX, i);
                AsEngine.it().push(new PetScene(), del);

                return true;
            }
        }

        return false;
    }


    private Cell addCell(int width,
                         int height, int left, int top) {

        Cell cell = new Cell();
        cell.setSize(width, height);
        cell.setPosition(left, top);
        cell.setColor(Cell.randColor(), true);
        attachChild(cell);

        return cell;
    }


    private class Blue extends StageItem implements ITurnable {
        public Blue(Cell cell) {
            setResIds(R.drawable.ms_bluester_idle_1600_160);

            setSize(cell.getWidth(), cell.getHeight());
            setPosition(cell.getX(), cell.getY());
        }

        @Override
        public boolean isTurned() {
            return true;
        }

        @Override
        public boolean tryTurn(float dx) {
            return false;
        }
    }


    private class RoundText extends AsButton {
        public RoundText(Cell cell, int index) {
            setSize(cell.getWidth(), cell.getHeight());
            setPosition(cell.getX(), cell.getY());

            text().setTextSize(40);
            text().setTextColor(cell.getCompleColor().getARGBPackedInt());

            SelectChalScene.this.attachChild(this);
            mRound[index] = this;
        }
    }
}