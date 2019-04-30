package com.sunhongkao.fish.engine;

import com.sunhongkao.fish.round.RoundMgr;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;


public class Recorder {
    private static SharedPreferences sPref;
    private static Editor sEditor;

    private static final String KEY_MUSIC_VOL = "recoder_music";
    private static final String KEY_SOUND_VOL = "recoder_sound";
    private static final String KEY_ROUND_ADVE = "recoder_round_adve";
    private static final String KEY_ROUND_PETS = "recoder_round_pets";
    private static final String KEY_ROUND_CHAL = "recoder_round_chal";
    private static final String KEY_PET_ENABLED = "recoder_pet_enabled";
    private static final String KEY_PET_MASK = "recoder_pet_mask";
    private static final String KEY_KEEP_SCREEN_ON = "recoder_keep_screen_on";

    private static float sMusicVolume = 0f;
    private static float sSoundVolume = 0f;


    public static void init(Context context) {
        sPref = PreferenceManager.getDefaultSharedPreferences(context);
        sEditor = sPref.edit();
        sEditor.commit();

        if (getRoundAdveState(0) == RoundMgr.STATE_LOCKED) {
            setRoundAdveState(0, RoundMgr.STATE_UNLOCKED);
        }

        sMusicVolume = sPref.getFloat(KEY_MUSIC_VOL, sMusicVolume);
        sSoundVolume = sPref.getFloat(KEY_SOUND_VOL, sSoundVolume);
    }


    public static float getMusicVolume() {
        return sMusicVolume;
    }

    public static void setMusicVolume(float volume) {
        sMusicVolume = volume;
        sEditor.putFloat(KEY_MUSIC_VOL, volume);
        sEditor.commit();
    }

    public static float getSoundVolume() {
        return sSoundVolume;
    }

    public static void setSoundVolume(float volume) {
        sSoundVolume = volume;
        sEditor.putFloat(KEY_SOUND_VOL, volume);
        sEditor.commit();
    }

    public static int getRoundAdveState(int index) {
        return sPref.getInt(KEY_ROUND_ADVE + "_" + index, RoundMgr.STATE_LOCKED);
    }

    public static void setRoundAdveState(int index, int state) {
        sEditor.putInt(KEY_ROUND_ADVE + "_" + index, state);
        sEditor.commit();
    }

    public static int getRoundPetsState(int index) {
        return sPref.getInt(KEY_ROUND_PETS + "_" + index, RoundMgr.STATE_LOCKED);
    }

    public static void setRoundPetsState(int index, int state) {
        sEditor.putInt(KEY_ROUND_PETS + "_" + index, state);
        sEditor.commit();
    }

    public static int getRoundChalState(int index) {
        return sPref.getInt(KEY_ROUND_CHAL + "_" + index, RoundMgr.STATE_LOCKED);
    }

    public static void setRoundChalState(int index, int state) {
        sEditor.putInt(KEY_ROUND_CHAL + "_" + index, state);
        sEditor.commit();
    }

    public static int getEnabledPets() {
        return sPref.getInt(KEY_PET_ENABLED, 0);
    }

    public static boolean isPetEnabled(int index) {
        return (sPref.getInt(KEY_PET_ENABLED, 0) & (1 << index)) != 0;
    }

    public static void enablePet(int index) {
        sEditor.putInt(KEY_PET_ENABLED,
                sPref.getInt(KEY_PET_ENABLED, 0) | (1 << index));
        sEditor.commit();
    }

    public static int getPetMask() {
        return sPref.getInt(KEY_PET_MASK, 0);
    }

    public static void setPetMask(int mask) {
        sEditor.putInt(KEY_PET_MASK, mask);
        sEditor.commit();
    }

    public static boolean isKeepScreenOn() {
        return sPref.getBoolean(KEY_KEEP_SCREEN_ON, false);
    }

    public static void setKeepScreenOn(boolean on) {
        sEditor.putBoolean(KEY_KEEP_SCREEN_ON, on);
        sEditor.commit();
    }
}