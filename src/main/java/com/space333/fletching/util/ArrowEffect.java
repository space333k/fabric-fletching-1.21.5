package com.space333.fletching.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ArrowEffect {
    public static final String DEFAULT = "Default";

    public static final String MARINE = "Marine";
    public static final String PHASING = "Phasing";
    public static final String BOUNCING = "Bouncing";

    public static final String FLAME = "Flame";
    public static final String KNOCKBACK = "Knockback";
    public static final String LIGHTWEIGHT = "Lightweight";
    public static final String FLOATING = "Floating";

    public static final String TELEPORTING = "Teleporting";
    public static final String TELEPORTER = "Teleporter";
    public static final String PIERCING = "Piercing";
    public static final String EXPLODING = "Exploding";
    public static final String ECHOING = "Echoing";
    public static final String SPECTRAL = "Spectral";
    public static final String TIER2 = "Tier2";
    public static final String TIER3 = "Tier3";
    public static final String TIER4 = "Tier4";

    public static final Map<String, Integer> TEXTURE_NAME = new HashMap<>();

    public static void generateTextureName() {
        List<String> feathers = List.of(DEFAULT, MARINE, PHASING, BOUNCING);
        List<String> shafts = List.of(DEFAULT, FLAME, KNOCKBACK, LIGHTWEIGHT, FLOATING);
        List<String> tips = List.of(DEFAULT, TELEPORTING, TELEPORTER, PIERCING, EXPLODING, SPECTRAL, TIER2, TIER3, TIER4);

        int value = 0;
        for (String feather : feathers) {
            if (feather.equals(DEFAULT)) {
                feather = "";
            } else {
                feather = "_" + feather.toLowerCase();
            }
            for (String shaft : shafts) {
                if (shaft.equals(DEFAULT)) {
                    shaft = "";
                } else {
                    shaft = "_" + shaft.toLowerCase();
                }
                for (String tip : tips) {
                    if (tip.equals(DEFAULT)) {
                        tip = "";
                    } else {
                        tip = "_" + tip.toLowerCase();
                    }

                    TEXTURE_NAME.put(feather + shaft + tip, value);
                    value++;
                }
            }
        }
    }

}
