package com.space333.fletching.util;

import com.space333.fletching.Component.ModDataComponentType;
import com.space333.fletching.item.ModItems;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.*;

import static com.space333.fletching.util.ArrowEffect.*;

public class ComponentHelper {

    public static Map<Item, String> FEATHER_ID_MAP = new HashMap<>();
    public static Map<Item, String> SHAFT_ID_MAP = new HashMap<>();
    public static Map<Item, String> TIP_ID_MAP = new HashMap<>();

    public static Map<Item, List<String>> VANILLA_TO_CUSTOM = Map.of(
            Items.ARROW, List.of(DEFAULT, DEFAULT, DEFAULT, "no_potion"),
            Items.SPECTRAL_ARROW, List.of(DEFAULT, DEFAULT, SPECTRAL, "no_potion"),
            Items.TIPPED_ARROW, List.of(DEFAULT, DEFAULT, SPECTRAL, "potion")
    );


    public static ItemStack createArrow(Item feather, Item shaft, Item tip) {
        ItemStack output = ItemStack.EMPTY;

        if(isValidRecipe(feather, shaft, tip)) {
            output = new ItemStack(ModItems.CUSTOM_ARROW);
            output = addComponents(feather, shaft, tip, output);
        }
        return output;
    }

    public static ItemStack addComponents(Item feather, Item shaft, Item tip, ItemStack arrow) {
        if(!arrow.contains(ModDataComponentType.ARROW_FEATHER)) {
            arrow.set(ModDataComponentType.ARROW_FEATHER, FEATHER_ID_MAP.get(feather));
        }
        if(!arrow.contains(ModDataComponentType.ARROW_SHAFT)) {
            arrow.set(ModDataComponentType.ARROW_SHAFT, SHAFT_ID_MAP.get(shaft));
        }
        if(!arrow.contains(ModDataComponentType.ARROW_TIP)) {
            arrow.set(ModDataComponentType.ARROW_TIP, TIP_ID_MAP.get(tip));
        }

        return replaceToVanilla(arrow);
    }

    public static ItemStack replaceToVanilla(ItemStack arrow) {
        List<String> effects = new ArrayList<>();

        if(arrow.contains(ModDataComponentType.ARROW_FEATHER)) {
            effects.add(arrow.get(ModDataComponentType.ARROW_FEATHER));
        }
        if(arrow.contains(ModDataComponentType.ARROW_SHAFT)) {
            effects.add(arrow.get(ModDataComponentType.ARROW_SHAFT));
        }
        if(arrow.contains(ModDataComponentType.ARROW_TIP)) {
            effects.add(arrow.get(ModDataComponentType.ARROW_TIP));
        }
        if(arrow.contains(DataComponentTypes.POTION_CONTENTS)) {
            effects.add("potion");
        }
        else {
            effects.add("no_potion");
        }

        if(VANILLA_TO_CUSTOM.containsValue(effects)) {
            Item item = getKey(VANILLA_TO_CUSTOM, effects);

            if(item != Items.AIR) {
                ItemStack newArrow = new ItemStack(item, arrow.getCount());
                if(item == Items.TIPPED_ARROW) {
                    newArrow.set(DataComponentTypes.POTION_CONTENTS, arrow.get(DataComponentTypes.POTION_CONTENTS));
                }
                arrow = newArrow;
            }
        }

        return arrow;
    }

    public static Item getKey(Map<Item, List<String>> map, List<String> effects) {
        for (Map.Entry<Item, List<String>> entry : map.entrySet()) {
            if (entry.getValue().equals(effects)) {
                return entry.getKey();
            }
        }
        return Items.AIR;
    }

    public static List<String> getComponents(ItemStack itemStack) {
        String feather = itemStack.getOrDefault(ModDataComponentType.ARROW_FEATHER, DEFAULT);
        String shaft = itemStack.getOrDefault(ModDataComponentType.ARROW_SHAFT, DEFAULT);
        String tip = itemStack.getOrDefault(ModDataComponentType.ARROW_TIP, DEFAULT);

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
        FEATHER_ID_MAP.put(Items.FEATHER, DEFAULT);
        FEATHER_ID_MAP.put(Items.PRISMARINE_SHARD, MARINE);
        FEATHER_ID_MAP.put(Items.PHANTOM_MEMBRANE, PHASING);
        FEATHER_ID_MAP.put(Items.SLIME_BALL, BOUNCING);
    }

    private static void createShaftIDs() {
        SHAFT_ID_MAP.put(Items.STICK, DEFAULT);
        SHAFT_ID_MAP.put(Items.BLAZE_ROD, FLAME);
        SHAFT_ID_MAP.put(Items.BREEZE_ROD, KNOCKBACK);
        SHAFT_ID_MAP.put(Items.BAMBOO, LIGHTWEIGHT);
        SHAFT_ID_MAP.put(Items.END_ROD, FLOATING);
    }

    private static void createTipIDs() {
        TIP_ID_MAP.put(Items.FLINT, DEFAULT);
        TIP_ID_MAP.put(Items.ENDER_PEARL, TELEPORTING);
        TIP_ID_MAP.put(Items.CHORUS_FRUIT, TELEPORTER);
        TIP_ID_MAP.put(Items.AMETHYST_SHARD, PIERCING);
        TIP_ID_MAP.put(Items.FIRE_CHARGE, EXPLODING);
        //TIP_ID_MAP.put(Items.ECHO_SHARD, ECHOING);
        TIP_ID_MAP.put(Items.GLOW_INK_SAC, SPECTRAL);
        TIP_ID_MAP.put(Items.GLOWSTONE_DUST, SPECTRAL);
        TIP_ID_MAP.put(Items.IRON_INGOT, TIER2);
        TIP_ID_MAP.put(Items.DIAMOND, TIER3);
        TIP_ID_MAP.put(Items.NETHERITE_INGOT, TIER4);
    }

    public static ItemStack randomArrowGenerator() {
        ItemStack arrow = new ItemStack(ModItems.CUSTOM_ARROW);
        Random rand = new Random();
        arrow.set(ModDataComponentType.ARROW_FEATHER, FEATHER_ID_MAP.values().stream().toList().get(rand.nextInt(FEATHER_ID_MAP.size())));
        arrow.set(ModDataComponentType.ARROW_SHAFT, SHAFT_ID_MAP.values().stream().toList().get(rand.nextInt(SHAFT_ID_MAP.size())));
        arrow.set(ModDataComponentType.ARROW_TIP, TIP_ID_MAP.values().stream().toList().get(rand.nextInt(TIP_ID_MAP.size())));

        return replaceToVanilla(arrow);
    }
}
