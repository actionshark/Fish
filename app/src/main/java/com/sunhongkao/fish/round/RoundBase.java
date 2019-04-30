package com.sunhongkao.fish.round;

import java.util.ArrayList;
import java.util.List;

import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.ITouchEventCallback;
import org.andengine.util.math.MathUtils;

import com.sunhongkao.fish.engine.AsActivity;
import com.sunhongkao.fish.engine.AsButton;
import com.sunhongkao.fish.engine.AsEngine;
import com.sunhongkao.fish.engine.Deliver;
import com.sunhongkao.fish.engine.SeekAssist;
import com.sunhongkao.fish.engine.Util;
import com.sunhongkao.fish.fish.Fish;
import com.sunhongkao.fish.fish.FishMgr;
import com.sunhongkao.fish.fish.FishMgr.FishListener;
import com.sunhongkao.fish.iface.IUpdater;
import com.sunhongkao.fish.mons.Boss;
import com.sunhongkao.fish.mons.Monster;
import com.sunhongkao.fish.mons.MonsterMgr;
import com.sunhongkao.fish.pet.Pet;
import com.sunhongkao.fish.pet.Tadpole;
import com.sunhongkao.fish.scene.BaseScene;
import com.sunhongkao.fish.scene.DialogScene;
import com.sunhongkao.fish.scene.RoundScene;
import com.sunhongkao.fish.stage.Actor;
import com.sunhongkao.fish.stage.Food;
import com.sunhongkao.fish.stage.FoodMgr;
import com.sunhongkao.fish.stage.Gun;
import com.sunhongkao.fish.stage.MoneyMgr;
import com.sunhongkao.fish.stage.PropBox;
import com.sunhongkao.fish.stage.StageGift;
import com.sunhongkao.fish.stage.StageItem;
import com.sunhongkao.fish.stage.PropBox.PropListener;
import com.sunhongkao.fish.R;


public abstract class RoundBase implements IUpdater,
        ITouchEventCallback, FishListener, PropListener {

    protected Deliver mDeliver;

    protected int mRoundType = 0;
    protected int mRoundIndex = 0;
    protected int mPetMask = 0;

    protected PropBox[] mPropBoxs;
    protected AsButton mMoneyText;

    protected boolean mHintEnabled = false;
    protected final List<Actor> mDownActor = new ArrayList<>();
    protected Pet mClone;

    protected int mMonsterLine = Util.TIMES_LONG * 10;
    protected int mMonsterCnt = -mMonsterLine;


    public RoundBase() {
    }

    public void init(int type, int index) {
        mRoundType = type;
        mRoundIndex = index;
    }

    public void onCreate(Deliver deliver) {
        mDeliver = deliver;

        mPetMask = deliver.getInt(Pet.KEY_MASK, 0);
        mPropBoxs = (PropBox[]) deliver.get(RoundMgr.KEY_PROP_BOX);
        mMoneyText = (AsButton) deliver.get(RoundMgr.KEY_MONEY_TEXT);

        for (PropBox propBox : mPropBoxs) {
            propBox.addListener(this);
        }

        MoneyMgr.init(mMoneyText, 200);
        FoodMgr.init(mPropBoxs[1], mPropBoxs[2], mPropBoxs[3]);
        Gun.init(mPropBoxs[5]);

        FishMgr.reset();
        FishMgr.addListener(this);
        MonsterMgr.reset();

        for (int i = 0; i < Pet.PETS.length; i++) {
            if ((mPetMask & (1 << i)) != 0) {
                Pet pet = Pet.newPet(i);
                pet.initPosition();
                pet.attachSelf();

                if (pet instanceof Tadpole) {
                    mClone = pet;
                }

//                if (pet.supportMulti()) {
//                    pet = Pet.newPet(i);
//                    pet.initPosition();
//                    pet.attachSelf();
//                }
            }
        }
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void onDestroy() {
    }

    public void onUpdate() {
        List<Monster> list = MonsterMgr.getAll();

        if (list.size() > 0 && !(list.size() == 1 &&
                list.get(0) instanceof Boss)) {

            if (MathUtils.random(0.1f)) {
                mMonsterCnt++;
            }
        } else {
            mMonsterCnt++;
        }

        if (mMonsterCnt > mMonsterLine) {
            mMonsterCnt = 0;
            wantMonster();
        }

        if (MonsterMgr.getAll().isEmpty()) {
            AsEngine.it().playMusic(AsActivity.it().
                    getRawId("ms_tank_" + getMajor()));
        } else {
            AsEngine.it().playMusic(R.raw.ms_monster);
        }

        for (int i = mDownActor.size() - 1; i >= 0; i--) {
            Actor actor = mDownActor.get(i);

            if (actor.getCy() >= RoundScene.tankBottom()) {
                actor.setCy(RoundScene.tankBottom());
                mDownActor.remove(i);
            } else {
                actor.setY(actor.getY() + SeekAssist.SPEED_DOWN);
            }
        }

        if (MathUtils.random(0.002f)) {
            AsEngine.it().playSound(R.raw.sd_bubble);
        }
    }

    public abstract String getName();

    protected abstract int getGunCost();

    protected abstract int getEggCost();

    protected abstract void wantMonster();

    public int getMajor() {
        return mRoundIndex;
    }

    protected void addActor(Actor actor) {
        addActor(actor, 0);
    }

    protected void addActor(final Actor actor, float delay) {
        AsEngine.it().runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                if (AsEngine.it().getScene() instanceof RoundScene) {
                    actor.initPosition();
                    actor.setCy(0);
                    actor.attachSelf();

                    if (actor.canUpDown()) {
                        actor.seek(actor.getCx(), actor.getHeight() * 3);
                    } else {
                        mDownActor.add(actor);
                    }

                    AsEngine.it().playSound(R.raw.sd_fish_grow);
                }
            }
        }, delay);
    }

    protected void addMonster(Monster monster) {
        AsEngine.it().playSound(R.raw.sd_monster_hint);
        monster.initPosition();
        new MonsterHint(monster).attachSelf();
    }

    private void openInner(int index, int cost, int resId) {
        if (mPropBoxs[index].getState() == PropBox.STATE_READY) {
            mPropBoxs[index].setStop(StageGift.STOP_REPEAT);
            mPropBoxs[index].setResIds(resId);
            mPropBoxs[index].setPoint(cost);
            mPropBoxs[index].open(true);
        }
    }

    protected void open(Integer... indexs) {
        int major = getMajor();

        for (Integer index : indexs) {
            if (index == 0) {
                if (major < 3) {
                    openInner(index, 100, R.drawable.fs_guppy_idle_800_80);
                } else {
                    openInner(index, 200, R.drawable.fs_breed_idle_800_80);
                }
            } else if (index == 1) {
                FoodMgr.openType();
            } else if (index == 2) {
                FoodMgr.openNum();
            } else if (index == 3) {
                if (major == 1) {
                    openInner(index, 250, R.drawable.fd_drug_400_40);
                } else if (major == 2) {
                    openInner(index, 750, R.drawable.fs_tong_idle_800_80);
                } else {
                    openInner(index, 1000, R.drawable.fs_carn_idle_800_80);
                }
            } else if (index == 4) {
                if (major == 1) {
                    openInner(index, 750, R.drawable.fs_potor_idle_800_80);
                } else if (major == 2) {
                    openInner(index, 2000, R.drawable.fs_pearler_idle_800_80);
                } else if (major == 3) {
                    openInner(index, 10000, R.drawable.fs_ultra_idle_800_80);
                }
            } else if (index == 5) {
                Gun.open(getGunCost());
            } else if (index == 6) {
                if (mPropBoxs[index].getState() == PropBox.STATE_READY) {
                    mPropBoxs[index].setStop(StageGift.STOP_STEP);
                    mPropBoxs[index].setResIds(R.drawable.cp_prop_egg_138_39, 3);
                    mPropBoxs[index].setPoint(getEggCost());
                    mPropBoxs[index].open(false);
                }
            }
        }
    }

    public void openAll() {
        open(6);
    }

    public boolean hintEnabled() {
        return mHintEnabled;
    }

    public void enableHint(boolean hint) {
        mHintEnabled = hint;

        if (hint) {
            openAll();
        }
    }

    protected void onFailed() {
        AsEngine.it().playSound(R.raw.sd_failed);

        BaseScene scene = new DialogScene();

        Deliver deliver = new Deliver();
        deliver.set(BaseScene.KEY_TITLE, R.string.dialog_hint);
        deliver.set(DialogScene.KEY_MSG, AsActivity.it().
                getString(R.string.dialog_failed));
        deliver.set(DialogScene.KEY_BTNS, AsActivity.it().
                getStrings(R.string.dialog_exit, R.string.dialog_restart));
        deliver.set(DialogScene.KEY_RSTS, new int[]{Deliver.RST_EXIT,
                Deliver.RST_RESTART, Deliver.RST_EXIT});

        AsEngine.it().push(scene, deliver);
    }

    protected void onFinish() {
        AsEngine.it().playMusic(R.raw.ms_win, false);

        BaseScene scene = new DialogScene();

        Deliver deliver = new Deliver();
        deliver.set(BaseScene.KEY_TITLE, R.string.dialog_hint);
        deliver.set(DialogScene.KEY_MSG, AsActivity.it().
                getString(R.string.dialog_completed));
        deliver.set(DialogScene.KEY_BTNS, AsActivity.it().
                getStrings(R.string.dialog_ok));
        deliver.set(DialogScene.KEY_RSTS, new int[]{
                Deliver.RST_EXIT, Deliver.RST_EXIT});

        AsEngine.it().push(scene, deliver);
    }

    public boolean cloneble() {
        return mClone != null;
    }

    public void onClone(int index) {
        Pet pet = Pet.newPet(index);
        if (pet == null) {
            return;
        }

        if (pet.canUpDown()) {
            pet.setCenter(mClone.getCx(), mClone.getCy());
        } else {
            pet.initPosition();
        }

        mClone.detachSelf();
        pet.attachSelf();
        mClone = pet;
    }

    @Override
    public boolean onTouchEvent(TouchEvent event) {
        if (event.getAction() == TouchEvent.ACTION_UP) {
            Food food = FoodMgr.newFood();

            if (food != null) {
                food.setCenter(event.getX(), event.getY());
                food.attachSelf();
                return true;
            }
        }

        return false;
    }

    @Override
    public void onPropEvent(PropBox propBox) {
        if (propBox == mPropBoxs[6] && propBox.getCurIndex() < 2) {
            propBox.upgrade();
        }
    }

    @Override
    public void onFishEvent(Fish fish, int event) {
        if (event == FishMgr.EVENT_REMOVE && FishMgr.
                showNum() + FishMgr.hideNum() < 1) {

            onFailed();
        }
    }


    protected class MonsterHint extends StageItem {
        private Monster mMonster;
        private int mIndex = 0;


        MonsterHint(Monster monster) {
            mMonster = monster;

            setResId(R.drawable.cp_monster_hint_80_80);

            if (mHintEnabled && mIndex % 8 < 4) {
                setSize(monster.getWidth(), monster.getHeight());
                setPosition(monster.getX(), monster.getY());
            } else {
                setVisible(false);
            }
        }

        @Override
        public void onUpdate() {
            mIndex++;

            if (mIndex == Util.TIMES_SHORT / 2) {
                AsEngine.it().playSound(R.raw.sd_monster_hint);
            } else if (mIndex > Util.TIMES_SHORT) {
                mMonster.attachSelf();
                detachSelf();
            }
        }
    }
}