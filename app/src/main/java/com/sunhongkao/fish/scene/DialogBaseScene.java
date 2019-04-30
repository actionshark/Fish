package com.sunhongkao.fish.scene;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;

import com.sunhongkao.fish.engine.AsActivity;
import com.sunhongkao.fish.engine.Deliver;
import com.sunhongkao.fish.engine.PixelTextureSource;
import com.sunhongkao.fish.R;


public abstract class DialogBaseScene extends BaseScene {
    public static final String KEY_CAPTURE = "dialogbasescene_capture";


    @Override
    public void onCreate(Deliver deliver) {
        deliver.remove(KEY_BG);
        super.onCreate(deliver);

        int[] pixels = deliver.getIntArray(KEY_CAPTURE);
        if (pixels != null) {
            TextureRegion region = TextureRegionFactory.createFromSource(
                    AsActivity.it().newBitmapTexture(640, 480),
                    new PixelTextureSource(pixels, 0, 0, 640, 480),
                    0, 0);
            Sprite capture = new Sprite(0, 0, 640, 480, region,
                    AsActivity.it().getVertexBufferObjectManager());
            attachChild(capture);
        }

        mBg = new Sprite(64, 48, 512, 384,
                AsActivity.it().getRegion(R.drawable.bg_dialog_320_240),
                AsActivity.it().getVertexBufferObjectManager());
        attachChild(mBg);

        if (mTitle != null) {
            mTitle.setPosition(320, 80);
        }
    }

    @Override
    public void onMenuClick() {
    }
}