package com.space333.fletching.mixin.potion;

import com.space333.fletching.Fletching;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Items.class)
public class ItemsMixin {
    @ModifyArg(method = "<clinit>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item$Settings;maxCount(I)Lnet/minecraft/item/Item$Settings;", ordinal = 119))
    private static int onPotion(int old) {
        Fletching.LOGGER.info("register" + old);
        return 16;
    }
    @ModifyArg(method = "<clinit>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item$Settings;maxCount(I)Lnet/minecraft/item/Item$Settings;", ordinal = 143))
    private static int onSplashPotion(int old) {
        return 16;
    }
    @ModifyArg(method = "<clinit>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item$Settings;maxCount(I)Lnet/minecraft/item/Item$Settings;", ordinal = 144))
    private static int onLingeringPotion(int old) {
        return 16;
    }
}
