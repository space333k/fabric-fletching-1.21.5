package com.space333.fletching.mixin.disableEnchantments;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.space333.fletching.util.RemoveEnchantments;
import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @WrapOperation(
            method = "method_60106",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z"
            )
    )
    private static <E> boolean preventAdditionIfDisabled(List<EnchantmentLevelEntry> instance, E e, Operation<Boolean> original) {
        EnchantmentLevelEntry eh = (EnchantmentLevelEntry) e;
        if (
                RemoveEnchantments.DISABLED_ENCHANTMENTS.stream().anyMatch(s -> {
                    try {
                        return eh.enchantment().matchesId(Identifier.tryParse(s));
                    } catch (InvalidIdentifierException ex) {
                        RemoveEnchantments.handleIdentifierException(s ,ex);
                        return false;
                    }
                })
        ) {
            return false;
        }
        return original.call(instance, e);
    }



}
