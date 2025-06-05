package com.space333.fletching.mixin;

import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

    @ModifyConstant(method = "getPullTime", constant = @Constant(floatValue = 1.25F))
    private static float increaseCrossbowDraw(float constant) {
        return 1.50F;
    }

    @ModifyConstant(method = "use", constant = @Constant(floatValue = 1.0F))
    private float removeDivergence(float constant) {
        return 0.0F;
    }

    @Inject(method = "getSpeed", at = @At(value = "RETURN"), cancellable = true)
    private static void increaseSpeed(ChargedProjectilesComponent stack, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue((float) (cir.getReturnValue()*1.5));
    }

    @Inject(method = "getWeaponStackDamage", at = @At(value = "RETURN"), cancellable = true)
    private void increaseDamage(ItemStack projectile, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(cir.getReturnValue() + 1);
    }
}
