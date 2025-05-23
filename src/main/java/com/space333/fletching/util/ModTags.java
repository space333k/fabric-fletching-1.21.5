package com.space333.fletching.util;

import com.space333.fletching.Fletching;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> ARROW_TIP = createTag("arrow_tip");
        public static final TagKey<Item> ARROW_SHAFT = createTag("arrow_shaft");
        public static final TagKey<Item> ARROW_FEATHERS = createTag("arrow_feathers");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(Fletching.MOD_ID, name));
        }
    }

}
