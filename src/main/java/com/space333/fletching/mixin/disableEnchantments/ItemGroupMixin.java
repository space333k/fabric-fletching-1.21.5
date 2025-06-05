package com.space333.fletching.mixin.disableEnchantments;

import com.space333.fletching.util.RemoveEnchantments;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Set;

@Mixin(ItemGroup.class)
public class ItemGroupMixin {

    @Shadow private Collection<ItemStack> displayStacks;
    @Shadow private Set<ItemStack> searchTabStacks;

    @Inject(method = "updateEntries", at = @At("TAIL"))
    private void removeDisabledEnchantmentBooks(CallbackInfo ci) {
        filter(displayStacks);
        filter(searchTabStacks);
    }

    @Unique
    private void filter(Collection<ItemStack> displayItems) {
        displayItems.removeIf(RemoveEnchantments::filterStacks);
    }
}