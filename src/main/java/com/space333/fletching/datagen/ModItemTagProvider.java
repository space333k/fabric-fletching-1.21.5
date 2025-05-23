package com.space333.fletching.datagen;

import com.space333.fletching.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(ModTags.Items.ARROW_TIP)
                .add(Items.FLINT);

        getOrCreateTagBuilder(ModTags.Items.ARROW_SHAFT)
                .add(Items.STICK)
                .add(Items.BLAZE_ROD);

        getOrCreateTagBuilder(ModTags.Items.ARROW_FEATHERS)
                .add(Items.FEATHER);
    }
}
