package com.space333.fletching.mixin.disableEnchantments;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.space333.fletching.util.RemoveEnchantments;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.loot.function.SetEnchantmentsLootFunction;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SetEnchantmentsLootFunction.class)
public class SetEnchantmentsLootFunctionMixin {

    @WrapWithCondition(
            method = {
                    "method_57656",
                    "method_60297"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/component/type/ItemEnchantmentsComponent$Builder;set(Lnet/minecraft/registry/entry/RegistryEntry;I)V"
            )
    )
    private static boolean preventSetEnchantmentIfDisabled(ItemEnchantmentsComponent.Builder instance, RegistryEntry<Enchantment> enchantment, int level) {
        return RemoveEnchantments.DISABLED_ENCHANTMENTS.stream().noneMatch(s -> {
            try {
                return enchantment.matchesId(Identifier.tryParse(s));
            } catch (InvalidIdentifierException e) {
                RemoveEnchantments.handleIdentifierException(s ,e);
                return false;
            }
        });
    }
}