package com.space333.fletching.datagen;

import com.space333.fletching.item.ModItems;
import com.space333.fletching.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(ModTags.Items.ARROW_TIP)
                .add(Items.FLINT)
                .add(Items.ENDER_PEARL)
                .add(Items.CHORUS_FRUIT)
                .add(Items.AMETHYST_SHARD)
                .add(Items.FIRE_CHARGE)
                .add(Items.ECHO_SHARD)
                .add(Items.GLOW_INK_SAC)
                .add(Items.IRON_INGOT)
                .add(Items.DIAMOND)
                .add(Items.NETHERITE_INGOT);

        getOrCreateTagBuilder(ModTags.Items.ARROW_SHAFT)
                .add(Items.STICK)
                .add(Items.BLAZE_ROD)
                .add(Items.BREEZE_ROD)
                .add(Items.BAMBOO)
                .add(Items.END_ROD);

        getOrCreateTagBuilder(ModTags.Items.ARROW_FEATHERS)
                .add(Items.FEATHER)
                .add(Items.PRISMARINE_SHARD)
                .add(Items.PHANTOM_MEMBRANE)
                .add(Items.SLIME_BALL);

        getOrCreateTagBuilder(ItemTags.ARROWS)
                .add(ModItems.SPECIAL_ARROW);
    }
}
