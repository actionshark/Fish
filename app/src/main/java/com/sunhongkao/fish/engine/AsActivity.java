package com.sunhongkao.fish.engine;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.IResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import com.sunhongkao.fish.scene.BaseScene;
import com.sunhongkao.fish.scene.HomeScene;
import com.sunhongkao.fish.scene.StartScene;

import android.os.Process;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.KeyEvent;


public class AsActivity extends BaseGameActivity {
    private static AsActivity sInstance;


    private final int WIDTH = 640;
    private final int HEIGHT = 480;
    private int mOrgWidth;
    private int mOrgHeight;

    private SparseArray<ITiledTextureRegion> mRegions
            = new SparseArray<>();


    public static AsActivity it() {
        return sInstance;
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        sInstance = this;

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int wd = dm.widthPixels;
        int ht = dm.heightPixels;

        if (wd * HEIGHT < ht * WIDTH) {
            mOrgWidth = wd;
            mOrgHeight = wd * HEIGHT / WIDTH;
        } else {
            mOrgWidth = ht * WIDTH / HEIGHT;
            mOrgHeight = ht;
        }

        Recorder.init(this);

        IResolutionPolicy rp = new RatioResolutionPolicy(WIDTH, HEIGHT);
        Camera camera = new Camera(0, 0, WIDTH, HEIGHT);

        EngineOptions option = new EngineOptions(true,
                ScreenOrientation.LANDSCAPE_FIXED, rp, camera,
                new WakeLockOptions(this, Recorder.isKeepScreenOn()));

        option.getAudioOptions().setNeedsMusic(true);
        option.getAudioOptions().setNeedsSound(true);

        return option;
    }

    @Override
    public Engine onCreateEngine(EngineOptions options) {
        return new AsEngine(options);
    }

    @Override
    public void onResume() {
        super.onResume();

        AsEngine.it().onActivityResume();
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback callback) {
        callback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback callback) {
        AsEngine.it().push(new HomeScene());

        BaseScene scene = new StartScene();
        AsEngine.it().push(scene);

        callback.onCreateSceneFinished(scene);
    }

    @Override
    public void onPopulateScene(Scene scene, OnPopulateSceneCallback callback) {
        callback.onPopulateSceneFinished();
    }

    @Override
    public void onPause() {
        super.onPause();

        AsEngine.it().onActivityPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Process.killProcess(Process.myPid());
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (AsEngine.it().onKeyUp(keyCode)) {
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    public int getOrgWidth() {
        return mOrgWidth;
    }

    public int getOrgHeight() {
        return mOrgHeight;
    }

    public BitmapTextureAtlas newBitmapTexture(int width, int height) {
        BitmapTextureAtlas texture = new BitmapTextureAtlas(
                getTextureManager(), width, height);
        texture.load();
        return texture;
    }

    public ITiledTextureRegion getRegion(int resId) {
        return getRegions(resId, 1);
    }

    public ITiledTextureRegion getRegions(int resId) {
        return getRegions(resId, 10);
    }

    public ITiledTextureRegion getRegions(int resId, int num) {
        try {
            ITiledTextureRegion region = mRegions.get(resId);

            if (region == null) {
                int[] size = Util.getSize(getString(resId));
                region = BitmapTextureAtlasTextureRegionFactory.
                        createTiledFromResource(newBitmapTexture(size[0], size[1]),
                                this, resId, 0, 0, num, 1);
                mRegions.put(resId, region);
            }

            return region;
        } catch (Exception e) {
            Logger.print(e);
        }

        return null;
    }

    public int getDrawId(String name) {
        return getResources().getIdentifier(name, "drawable", getPackageName());
    }

    public int getStringId(String name) {
        return getResources().getIdentifier(name, "string", getPackageName());
    }

    public int getRawId(String name) {
        return getResources().getIdentifier(name, "raw", getPackageName());
    }

    public String getString(String name) {
        return getString(getStringId(name));
    }

    public String[] getStrings(int... resIds) {
        String[] str = new String[resIds.length];

        for (int i = 0; i < resIds.length; i++) {
            str[i] = getString(resIds[i]);
        }

        return str;
    }
}