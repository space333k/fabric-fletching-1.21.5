package com.space333.fletching.mixin;

import com.space333.fletching.FletchingClient;
import com.space333.fletching.client.SwitchArrowPayload;
import com.space333.fletching.component.LoadedProjectileComponent;
import com.space333.fletching.component.ModDataComponentType;
import com.space333.fletching.util.ArrowSelection;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
    @Unique
    private static boolean alreadyChangedArrow = false;


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

    @Inject(method = "onStoppedUsing", at = @At(value = "HEAD"))
    public void removeComponent(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfoReturnable<Boolean> cir) {
        if(stack.get(DataComponentTypes.CHARGED_PROJECTILES) == ChargedProjectilesComponent.DEFAULT || !stack.contains(DataComponentTypes.CHARGED_PROJECTILES)) {
            if(stack.contains(ModDataComponentType.LOADED_ARROW)) {
                stack.remove(ModDataComponentType.LOADED_ARROW);
            }
        }
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;shootAll(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;FFLnet/minecraft/entity/LivingEntity;)V"))
    public void removeProjectile(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack weapon = user.getStackInHand(hand);
        weapon.remove(ModDataComponentType.LOADED_ARROW);
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setCurrentHand(Lnet/minecraft/util/Hand;)V"))
    public void chooseProjectile(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack weapon = user.getStackInHand(hand);
        ItemStack nextArrow = ArrowSelection.switchArrow(user, ItemStack.EMPTY, weapon);
        weapon.set(ModDataComponentType.LOADED_ARROW, LoadedProjectileComponent.of(nextArrow));
    }

    @Redirect(method = "loadProjectiles", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getProjectileType(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"))
    private static ItemStack loadProjectile(LivingEntity shooter, ItemStack weapon) {
        if(weapon.contains(ModDataComponentType.LOADED_ARROW)) {
            return Objects.requireNonNull(weapon.get(ModDataComponentType.LOADED_ARROW)).getProjectile();
        }
        return shooter.getProjectileType(weapon);
    }
}
