package com.space333.fletching.mixin.disableEnchantments;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.EnchantmentScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnchantmentScreenHandler.class)
public class EnchantmentScreenHandlerMixin {

    @Redirect(method = "onContentChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEnchantable()Z"))
    private boolean cantEnchantBowCrossbow(ItemStack instance) {
        if(instance.getItem() == Items.BOW || instance.getItem() == Items.CROSSBOW) {
            return false;
        }
        return instance.isEnchantable();
    }
}
