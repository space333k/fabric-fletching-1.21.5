package com.space333.fletching.mixin.disableEnchantments;

import com.space333.fletching.util.RemoveEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;
import java.util.Optional;

@Mixin(EnchantRandomlyLootFunction.class)
public class EnchantRandomlyLootFunctionMixin {

    @Shadow @Final private Optional<RegistryEntryList<Enchantment>> options;

    @ModifyArg(
            method = "process",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Util;getRandomOrEmpty(Ljava/util/List;Lnet/minecraft/util/math/random/Random;)Ljava/util/Optional;"
            ),
            index = 0
    )
    private List<RegistryEntry<Enchantment>> filter(List<RegistryEntry<Enchantment>> selections) {
        if (options.isEmpty()) {
            return selections.stream().filter(RegistryEntry -> RegistryEntry.isIn(EnchantmentTags.ON_RANDOM_LOOT)).toList();
        }
        else {
            return selections.stream().filter(RegistryEntry -> RemoveEnchantments.DISABLED_ENCHANTMENTS.stream().noneMatch(s -> {
                try {
                    return RegistryEntry.matchesId(Identifier.tryParse(s));
                } catch (InvalidIdentifierException e) {
                    RemoveEnchantments.handleIdentifierException(s ,e);
                    return false;
                }
            })).toList();
        }
    }
}