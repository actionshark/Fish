package com.sunhongkao.fish.engine;

import java.util.ArrayList;
import java.util.List;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.util.ScreenGrabber;
import org.andengine.entity.util.ScreenGrabber.IScreenGrabberCallback;

import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.KeyEvent;

import com.sunhongkao.fish.scene.BaseScene;
import com.sunhongkao.fish.scene.DialogBaseScene;


public class AsEngine extends LimitedFPSEngine {
    private static AsEngine sInstance;

    private final List<BaseScene> mScenes = new ArrayList<BaseScene>();

    private static final int FPS = 25;
    private static final long MPF = 1000 / FPS;

    private int mMusicId;
    private Music mMusic;

    private final SparseArray<Sound> mSounds = new SparseArray<Sound>();


    public static AsEngine it() {
        return sInstance;
    }

    public AsEngine(EngineOptions options) {
        super(options, FPS);

        sInstance = this;

        getMusicManager().setMasterVolume(Recorder.getMusicVolume());
        getSoundManager().setMasterVolume(Recorder.getSoundVolume());
    }

    public void onActivityResume() {
        if (mScenes.size() > 0) {
            mScenes.get(mScenes.size() - 1).onResume(new Deliver());
        }

        if (mMusic != null) {
            mMusic.resume();
        }
    }

    public void onActivityPause() {
        if (mScenes.size() > 0) {
            mScenes.get(mScenes.size() - 1).onPause();
        }

        if (mMusic != null) {
            mMusic.pause();
        }
    }

    public boolean onKeyUp(int keyCode) {
        if (mScenes.size() > 0) {
            final BaseScene scene = mScenes.get(mScenes.size() - 1);

            if (keyCode == KeyEvent.KEYCODE_MENU) {
                runOnUpdateThread(new Runnable() {
                    @Override
                    public void run() {
                        scene.onMenuClick();
                    }
                });

                return true;
            } else if (keyCode == KeyEvent.KEYCODE_BACK) {
                runOnUpdateThread(new Runnable() {
                    @Override
                    public void run() {
                        scene.onBackClick();
                    }
                });

                return true;
            }
        }

        return false;
    }

    public void push(BaseScene scene) {
        push(scene, null);
    }

    public void push(final BaseScene scene, Deliver deliver) {
        final Deliver del = deliver == null ? new Deliver() : deliver;
        final BaseScene prev = mScenes.size() > 0 ? mScenes.get(mScenes.size() - 1) : null;

        if (prev != null && scene instanceof DialogBaseScene) {
            final ScreenGrabber grabber = new ScreenGrabber();

            prev.attachChild(grabber);
            grabber.grab(0, 0, AsActivity.it().getOrgWidth(),
                    AsActivity.it().getOrgHeight(), new IScreenGrabberCallback() {

                        @Override
                        public void onScreenGrabbed(Bitmap bmp) {
                            prev.detachChild(grabber);

                            Bitmap bm = Bitmap.createScaledBitmap(bmp, 640, 480, false);
                            bmp.recycle();

                            int[] pixels = new int[(640 + 1) * (480 + 1)];
                            bm.getPixels(pixels, 0, 640, 0, 0, 640, 480);
                            bm.recycle();

                            del.set(DialogBaseScene.KEY_CAPTURE, pixels);
                            trans(prev, scene, del);
                        }

                        @Override
                        public void onScreenGrabFailed(Exception ex) {
                            prev.detachChild(grabber);

                            trans(prev, scene, del);
                        }
                    });
        } else {
            trans(prev, scene, del);
        }
    }

    private void trans(BaseScene prev, BaseScene next, Deliver deliver) {
        if (prev != null) {
            prev.onPause();
        }

        mScenes.add(next);
        setScene(next);

        next.onCreate(deliver);
        next.onResume(new Deliver());
    }

    public void pop() {
        pop(null);
    }

    public void pop(Deliver deliver) {
        int size = mScenes.size();

        if (size > 0) {
            BaseScene scene = mScenes.remove(--size);
            scene.onPause();
            scene.onDestroy();

            if (size > 0) {
                if (deliver == null) {
                    deliver = new Deliver();
                }

                scene = mScenes.get(size - 1);
                setScene(scene);
                scene.onResume(deliver);
            }
        }

        if (mScenes.size() < 1) {
            AsActivity.it().finish();
        }
    }

    public int getFPS() {
        return FPS;
    }

    public long getMPF() {
        return MPF;
    }

    public void playMusic(int resId) {
        playMusic(resId, true);
    }

    public void playMusic(int resId, boolean loop) {
        if (mMusicId == resId) {
            return;
        }

        if (mMusic != null) {
            mMusic.release();
        }

        mMusicId = resId;
        try {
            mMusic = MusicFactory.createMusicFromResource(
                    getMusicManager(), AsActivity.it(), resId);
            getMusicManager().setMasterVolume(getMusicManager().getMasterVolume());
            mMusic.setLooping(loop);
            mMusic.play();
        } catch (Exception e) {
        }
    }

    public void playSound(final int resId) {
        Sound sound = mSounds.get(resId);
        if (sound == null) {
            sound = SoundFactory.createSoundFromResource(getSoundManager(),
                    AsActivity.it(), resId);
            mSounds.put(resId, sound);
        }

        final Sound sd = sound;
        runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                if (sd.isLoaded()) {
                    sd.play();
                } else {
                    runOnUpdateThread(this, 0.1f);
                }
            }
        });
    }
}