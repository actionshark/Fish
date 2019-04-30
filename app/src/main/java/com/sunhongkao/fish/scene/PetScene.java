package com.sunhongkao.fish.scene;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.VerticalAlign;

import com.sunhongkao.fish.engine.AsActivity;
import com.sunhongkao.fish.engine.AsButton;
import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.AsText;
import com.sunhongkao.fish.engine.Deliver;
import com.sunhongkao.fish.engine.Recorder;
import com.sunhongkao.fish.pet.Pet;
import com.sunhongkao.fish.round.RoundMgr;
import com.sunhongkao.fish.R;


public class PetScene extends BaseScene {
    public static final int INTENT_SHOW = 0;
    public static final int INTENT_ROUND = 1;
    public static final int INTENT_CLONE = 2;

    private static final int PET_MAX = 3;

    private Deliver mRoundDeliver;
    private int mIntent;

    private int mMask = 0;
    private int mNum = 0;
    private int mMax = 0;

    private final Sprite[] mBgs = new Sprite[24];
    private final Sprite[] mSel = new Sprite[24];
    private final AnimatedSprite[] mPets = new AnimatedSprite[20];
    private AsText mText;


    @Override
    public void onCreate(Deliver deliver) {
        deliver.set(KEY_BG, R.drawable.bg_dialog_320_240);
        deliver.set(KEY_TITLE, R.string.pet_title);
        super.onCreate(deliver);

        mRoundDeliver = new Deliver();
        mRoundDeliver.set(RoundMgr.KEY_TYPE, deliver.get(RoundMgr.KEY_TYPE));
        mRoundDeliver.set(RoundMgr.KEY_INDEX, deliver.get(RoundMgr.KEY_INDEX));
        mRoundDeliver.set(Pet.KEY_MASK, 0);

        mIntent = deliver.getInt(Deliver.KEY_INTENT, INTENT_SHOW);

        if (INTENT_ROUND == mIntent) {
            int type = deliver.getInt(RoundMgr.KEY_TYPE, RoundMgr.TYPE_ADVE);
            int index = deliver.getInt(RoundMgr.KEY_INDEX, 0);

            if (type == RoundMgr.TYPE_ADVE && 20 == index || type == RoundMgr.TYPE_PETS) {
                AsEngine.it().pop();
                AsEngine.it().push(new RoundScene(), mRoundDeliver);
                return;
            }

            int mask = Recorder.getEnabledPets();
            int num = 0;
            for (int i = 0; i < Pet.PETS.length; i++) {
                if ((mask & (1 << i)) != 0) {
                    num++;
                }
            }

            mMask = Recorder.getPetMask();

            if (num <= PET_MAX) {
                mRoundDeliver.set(Pet.KEY_MASK, mask);

                AsEngine.it().pop();
                AsEngine.it().push(new RoundScene(), mRoundDeliver);
                return;
            }
        }

        mText = new AsText();
        mText.setAlign(HorizontalAlign.LEFT, VerticalAlign.TOP);
        mText.setTextSize(25);
        mText.setPosition(225, 90);
        mText.setLineWidth(160);
        attachChild(mText);

        AsButton ok = new AsButton() {
            @Override
            public boolean onAreaTouched(TouchEvent event, float x, float y) {
                if (TouchEvent.ACTION_UP != event.getAction()) {
                    AsEngine.it().playSound(R.raw.sd_click);
                    return false;
                }

                onBackClick();
                return true;
            }
        };

        ok.setRegion(AsActivity.it().getRegion(R.drawable.cp_button_89_27));
        ok.setSize(120, 40);
        ok.setCenter(320, 430);
        ok.text().setText(R.string.dialog_ok);
        ok.text().setTextSize(25);

        attachChild(ok);

        switch (mIntent) {
            case INTENT_ROUND:
                mMax = PET_MAX;
                mText.setText(R.string.pet_round);
                break;

            case INTENT_CLONE:
                mMax = 1;
                mText.setText(R.string.pet_clam);
                break;

            case INTENT_SHOW:
                mMax = 0;
                mText.setText(R.string.pet_show);
                break;
        }

        int[] order = new int[]{0, 6, 12, 18, 24, 1, 7, 13, 19, 25,
                4, 10, 16, 22, 28, 5, 11, 17, 23, 29, 20, 26, 21, 27};
        for (int i = 0; i < order.length; i++) {
            initPetbox(i, order[i], i);
        }
    }

    private void initPetbox(int index, int postion, int name) {
        int region;
        if (index < 20 && Recorder.isPetEnabled(index)) {
            region = R.drawable.cp_petbox_open_89_76;
        } else {
            region = R.drawable.cp_petbox_close_89_76;
        }

        mBgs[index] = new Sprite(postion % 6 * 90 + 63, postion / 6 * 66 + 81, 62, 60,
                AsActivity.it().getRegion(region),
                AsActivity.it().getVertexBufferObjectManager());
        attachChild(mBgs[index]);

        if (index < 20 && Recorder.isPetEnabled(index)) {
            float x = mBgs[index].getX();
            float y = mBgs[index].getY();
            float width = mBgs[index].getWidth();
            float height = mBgs[index].getHeight();

            mSel[index] = new Sprite(x, y, width, height,
                    AsActivity.it().getRegion(R.drawable.cp_petbox_select_89_76),
                    AsActivity.it().getVertexBufferObjectManager());
            mSel[index].setVisible(false);
            attachChild(mSel[index]);

            if ((mMask & (1 << index)) != 0) {
                mBgs[index].setVisible(false);
                mSel[index].setVisible(true);
            }

            mPets[index] = new AnimatedSprite(x + width * 0.1f, y + width * 0.1f, width * 0.8f, height * 0.8f,
                    AsActivity.it().getRegions(AsActivity.it().
                            getDrawId("pt_" + Pet.PETS[name] + "_idle" + Pet.getSize(name))),
                    AsActivity.it().getVertexBufferObjectManager()) {

                @Override
                public boolean onAreaTouched(TouchEvent event, float x, float y) {
                    if (event.getAction() != TouchEvent.ACTION_UP) {
                        return false;
                    }

                    for (int i = 0; i < mPets.length; i++) {
                        if (mPets[i] == this) {
                            AsEngine.it().playSound(R.raw.sd_click);

                            if ((mMask & (1 << i)) != 0) {
                                mMask &= ~(1 << i);
                                mNum--;

                                mBgs[i].setVisible(true);
                                mSel[i].setVisible(false);
                            } else if (mNum < mMax) {
                                mMask |= 1 << i;
                                mNum++;

                                mBgs[i].setVisible(false);
                                mSel[i].setVisible(true);
                            }

                            mText.setText(AsActivity.it().getString("pet_" + Pet.PETS[i]));
                            return true;
                        }
                    }

                    return true;
                }
            };

            mPets[index].animate(AsEngine.it().getMPF());
            attachChild(mPets[index]);
        }
    }

    @Override
    public void onBackClick() {
        AsEngine.it().playSound(R.raw.sd_click);

        switch (mIntent) {
            case INTENT_ROUND:
                Recorder.setPetMask(mMask);

                mRoundDeliver.set(Pet.KEY_MASK, mMask);
                AsEngine.it().pop();
                AsEngine.it().push(new RoundScene(), mRoundDeliver);
                break;

            case INTENT_CLONE:
                for (int i = 0; i < 30; i++) {
                    if ((mMask & (1 << i)) != 0) {
                        Deliver del = new Deliver();
                        del.set(Deliver.KEY_RESULT, Deliver.RST_CLONE);
                        del.set(Pet.KEY_INDEX, i);
                        AsEngine.it().pop(del);
                        return;
                    }
                }

                AsEngine.it().pop();
                break;

            case INTENT_SHOW:
                AsEngine.it().pop();
                break;
        }
    }
}