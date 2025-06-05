package com.space333.fletching.util;

import com.space333.fletching.Fletching;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.util.ArrayList;
import java.util.List;

public class RemoveEnchantments {
    public static List<String> DISABLED_ENCHANTMENTS = List.of(
            "minecraft:piercing",
            "minecraft:quick_charge",
            "minecraft:protection",
            "minecraft:flame",
            "minecraft:power",
            "minecraft:infinity",
            "minecraft:punch"
    );

    private static final List<String> INVALID_ENTRIES = new ArrayList<>();

    public static boolean filterStacks(ItemStack stack) {
        for (String s : DISABLED_ENCHANTMENTS) {
            ItemEnchantmentsComponent storedEnchantments = stack.get(DataComponentTypes.STORED_ENCHANTMENTS);
            if (storedEnchantments != null) {
                try {
                    if (storedEnchantments.getEnchantments().stream().anyMatch(holder -> holder.matchesId(Identifier.tryParse(s))))
                        return true;
                } catch (InvalidIdentifierException e) {
                    handleIdentifierException(s, e);
                }
            }
        }
        return false;
    }

    public static void handleIdentifierException(String s, InvalidIdentifierException e) {
        if (!INVALID_ENTRIES.contains(s)) {
            Fletching.LOGGER.error("[Enchantment Disabler] Failed to parse enchantment {}:", s);
            Fletching.LOGGER.error(e.getMessage());
            Fletching.LOGGER.error("Verify that enchantments added in the disabled enchantments list inside the mod config are valid.");
            INVALID_ENTRIES.add(s);
        }
    }

    public static ItemEnchantmentsComponent getItemEnchantments(ItemStack stack) {
        if (stack.contains(DataComponentTypes.STORED_ENCHANTMENTS)) return stack.get(DataComponentTypes.STORED_ENCHANTMENTS);
        else return stack.get(DataComponentTypes.ENCHANTMENTS);
    }
}
