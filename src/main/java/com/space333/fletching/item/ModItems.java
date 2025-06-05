package com.space333.fletching.item;

import com.space333.fletching.Fletching;
import com.space333.fletching.item.custom.CustomArrowItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item CUSTOM_ARROW = registerArrow("custom_arrow");

    private static Item registerArrow(String name) {
        return Registry.register(Registries.ITEM, Identifier.of(Fletching.MOD_ID, name), new CustomArrowItem(new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Fletching.MOD_ID, name)))));
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Fletching.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Fletching.LOGGER.info("Registering Mod Items for " + Fletching.MOD_ID);
    }
}
