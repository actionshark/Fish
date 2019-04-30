package com.sunhongkao.fish.engine;

import org.andengine.util.math.MathUtils;

import com.sunhongkao.fish.iface.ITurnable;
import com.sunhongkao.fish.iface.IUpdater;
import com.sunhongkao.fish.scene.RoundScene;
import com.sunhongkao.fish.stage.StageItem;


public class SeekAction implements IUpdater {
    public interface Seeker {
        boolean shouldSeek();

        void seek(StageItem seekee);

        void seek(float x, float y);

        void onSeeked(StageItem seekee);

        float getSeekerX();

        float getSeekerY();

        void onSeekMove(float dx, float dy);
    }


    private Seeker mSeeker;
    public StageItem seekee;

    public boolean seeking = false;
    public float seekProb = 0.03f;

    public float targetX = 0;
    public float targetY = 0;
    public float speedX = SeekAssist.SPEED_NORMAL;
    public float speedY = SeekAssist.SPEED_NORMAL;


    public SeekAction(Seeker seeker) {
        mSeeker = seeker;
    }

    @Override
    public void onUpdate() {
        if (mSeeker.shouldSeek()) {
            if (seeking) {
                onSeek();
            } else if (MathUtils.random(seekProb)) {
                seek(MathUtils.random(0, 640), MathUtils.
                        random(0, RoundScene.tankBottom()));
            }
        }
    }

    public void seek(StageItem seekee) {
        if (seekee != null) {
            seeking = true;
            this.seekee = seekee;
        }
    }

    public void seek(float tx, float ty) {
        seeking = true;
        seekee = null;
        targetX = tx;
        targetY = ty;
    }

    private void onSeek() {
        if (seekee != null) {
            if (seekee.hasParent()) {
                targetX = seekee.getCx();
                targetY = seekee.getCy();
            } else {
                seeking = false;
                seekee = null;
                return;
            }
        }

        float dx = targetX - mSeeker.getSeekerX();
        float dy = targetY - mSeeker.getSeekerY();
        int near = 0;

        if (mSeeker instanceof ITurnable) {
            if (((ITurnable) mSeeker).tryTurn(dx)) {
                return;
            }
        }

        if (dx > speedX) {
            dx = speedX;
        } else if (dx < -speedX) {
            dx = -speedX;
        } else {
            ++near;
        }

        if (Util.equals(speedY, 0)) {
            if (seekee == null || Math.abs(dy) <= (seekee.getHeight() +
                    ((StageItem) mSeeker).getHeight()) / 5) {

                ++near;
            }

            dy = 0;
        } else if (dy > speedY) {
            dy = speedY;
        } else if (dy < -speedY) {
            dy = -speedY;
        } else {
            ++near;
        }

        mSeeker.onSeekMove(dx, dy);

        if (near >= 2) {
            StageItem tmp = seekee;

            if (Util.equals(dx, 0) && Util.equals(dy, 0)) {
                seeking = false;
                seekee = null;
            }

            mSeeker.onSeeked(tmp);
        }
    }
}