package com.space333.fletching.mixin;

import com.space333.fletching.FletchingClient;
import com.space333.fletching.component.LoadedProjectileComponent;
import com.space333.fletching.component.ModDataComponentType;
import com.space333.fletching.util.ArrowSelection;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(BowItem.class)
public abstract class BowItemMixin extends RangedWeaponItem {
    @Unique
    private static boolean alreadyChangedArrow = false;

    public BowItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setCurrentHand(Lnet/minecraft/util/Hand;)V"))
    public void chooseProjectile(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack weapon = user.getStackInHand(hand);
        ItemStack nextArrow = ArrowSelection.switchArrow(user, ItemStack.EMPTY, weapon);
        weapon.set(ModDataComponentType.LOADED_ARROW, LoadedProjectileComponent.of(nextArrow));
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClient) {
            boolean changeArrowPressed = FletchingClient.changeShootingArrow.isPressed();
            if(changeArrowPressed && !alreadyChangedArrow && stack.contains(ModDataComponentType.LOADED_ARROW)) {
                LoadedProjectileComponent loadedProjectileComponent = stack.get(ModDataComponentType.LOADED_ARROW);

                ItemStack nextArrow;
                if(loadedProjectileComponent == null) {
                    nextArrow = ArrowSelection.switchArrow(user, ItemStack.EMPTY, stack);
                }
                else {
                    ItemStack arrow = loadedProjectileComponent.getProjectile();
                    nextArrow = ArrowSelection.switchArrow(user, arrow, stack);
                }
                stack.remove(ModDataComponentType.LOADED_ARROW);
                stack.set(ModDataComponentType.LOADED_ARROW, LoadedProjectileComponent.of(nextArrow));
            }
            alreadyChangedArrow = changeArrowPressed;
        }

    }

    @Redirect(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getProjectileType(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"))
    private ItemStack loadProjectile(PlayerEntity instance, ItemStack weapon) {
        if(weapon.contains(ModDataComponentType.LOADED_ARROW)) {
            ItemStack projectile = Objects.requireNonNull(weapon.get(ModDataComponentType.LOADED_ARROW)).getProjectile();
            weapon.remove(ModDataComponentType.LOADED_ARROW);
            return projectile;
        }
        return instance.getProjectileType(weapon);
    }

}
