package com.sunhongkao.fish.pet;

import com.sunhongkao.fish.engine.AsActivity;
import com.sunhongkao.fish.stage.ActorState;


public abstract class Pet extends ActorState {
    protected static final String TAG = "Pet";

    public static final String KEY_INDEX = "pet_index";
    public static final String KEY_MASK = "pet_mask";


    public static final String[] PETS = new String[]{
            "snail", "clam", "sword", "breeder", "horse",
            "jelly", "bone", "crab", "mermaid", "whale",
            "turtle", "toy", "lamp", "dolphin", "hermit",
            "fan", "eel", "shark", "angel", "tadpole"
    };

    public static String getSize(int index) {
        if (index == 16) {
            return "_1600_60";
        } else {
            return "_800_80";
        }
    }

    public static Pet newPet(int index) {
        Pet pet = null;

        try {
            StringBuilder name = new StringBuilder(PETS[index]);
            name.setCharAt(0, (char) (name.charAt(0) - 32));
            String path = AsActivity.it().getPackageName() + ".pet." + name;

            pet = (Pet) Class.forName(path).newInstance();
        } catch (Exception e) {
        }

        return pet;
    }


    public Pet() {
        setSize(SIZE_DOWN, SIZE_DOWN);
    }

    public boolean supportMulti() {
        return true;
    }
}