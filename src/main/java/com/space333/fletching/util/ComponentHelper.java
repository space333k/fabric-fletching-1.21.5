package com.space333.fletching.util;

import com.space333.fletching.Component.ModDataComponentType;
import com.space333.fletching.item.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentHelper {

    public static Map<Item, String> FEATHER_ID_MAP = new HashMap<>();
    public static Map<Item, String> SHAFT_ID_MAP = new HashMap<>();
    public static Map<Item, String> TIP_ID_MAP = new HashMap<>();


    public static ItemStack createComponents(Item feather, Item shaft, Item tip) {
        ItemStack output = ItemStack.EMPTY;

        if(isValidRecipe(feather, shaft, tip)) {
            output = new ItemStack(ModItems.SPECIAL_ARROW);
            if(!output.contains(ModDataComponentType.ARROW_FEATHER)) {
                output.set(ModDataComponentType.ARROW_FEATHER, FEATHER_ID_MAP.get(feather));
            }
            if(!output.contains(ModDataComponentType.ARROW_SHAFT)) {
                output.set(ModDataComponentType.ARROW_SHAFT, SHAFT_ID_MAP.get(shaft));
            }
            if(!output.contains(ModDataComponentType.ARROW_TIP)) {
                output.set(ModDataComponentType.ARROW_TIP, TIP_ID_MAP.get(tip));
            }

        }


        return output;
    }

    public static boolean hasEffect(ItemStack itemStack,String effect) {
        List<String> list = getComponents(itemStack);

        return list.contains(effect);

    }

    public static List<String> getComponents(ItemStack itemStack) {
        String feather = itemStack.getOrDefault(ModDataComponentType.ARROW_FEATHER, "default");
        String shaft = itemStack.getOrDefault(ModDataComponentType.ARROW_SHAFT, "default");
        String tip = itemStack.getOrDefault(ModDataComponentType.ARROW_TIP, "default");

        return List.of(feather, shaft, tip);
    }

    public static boolean isValidRecipe(Item feather, Item shaft, Item tip) {
        return FEATHER_ID_MAP.containsKey(feather) ||
                SHAFT_ID_MAP.containsKey(shaft) ||
                TIP_ID_MAP.containsKey(tip);
    }

    public static void createAllArrowIDs() {
        createFeatherIDs();
        createShaftIDs();
        createTipIDs();
    }

    private static void createFeatherIDs() {
        FEATHER_ID_MAP.put(Items.FEATHER, "default");
        FEATHER_ID_MAP.put(Items.PRISMARINE_SHARD, "marine");
        FEATHER_ID_MAP.put(Items.PHANTOM_MEMBRANE, "phasing");
        FEATHER_ID_MAP.put(Items.SLIME_BALL, "bouncing");
    }

    private static void createShaftIDs() {
        SHAFT_ID_MAP.put(Items.STICK, "default");
        SHAFT_ID_MAP.put(Items.BLAZE_ROD, "flame");
        SHAFT_ID_MAP.put(Items.BREEZE_ROD, "knockback");
        SHAFT_ID_MAP.put(Items.BAMBOO, "lightweight");
        SHAFT_ID_MAP.put(Items.END_ROD, "floating");
    }

    private static void createTipIDs() {
        TIP_ID_MAP.put(Items.FLINT, "default");
        TIP_ID_MAP.put(Items.ENDER_PEARL, "teleporting");
        TIP_ID_MAP.put(Items.CHORUS_FRUIT, "teleporter");
        TIP_ID_MAP.put(Items.AMETHYST_SHARD, "piercing");
        TIP_ID_MAP.put(Items.FIRE_CHARGE, "exploding");
        TIP_ID_MAP.put(Items.ECHO_SHARD, "echoing");
        TIP_ID_MAP.put(Items.GLOW_INK_SAC, "spectral");
        TIP_ID_MAP.put(Items.IRON_INGOT, "tier 2");
        TIP_ID_MAP.put(Items.DIAMOND, "tier 3");
        TIP_ID_MAP.put(Items.NETHERITE_INGOT, "tier 4");
    }
}
