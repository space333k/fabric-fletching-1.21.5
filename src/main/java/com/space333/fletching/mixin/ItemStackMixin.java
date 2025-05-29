package com.space333.fletching.mixin;

import com.space333.fletching.Component.ModDataComponentType;
import com.space333.fletching.item.ModItems;
import com.space333.fletching.util.ComponentHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "appendTooltip", at = @At(value = "TAIL"))
    private void addUpgrades(
            Item.TooltipContext context,
            TooltipDisplayComponent displayComponent,
            @Nullable PlayerEntity player,
            TooltipType type,
            Consumer<Text> textConsumer,
            CallbackInfo ci
    ) {
        ItemStack self = ((ItemStack)(Object)this);
        /*
        if(self.isOf(ModItems.SPECIAL_ARROW)) {
            List<String> components = ComponentHelper.getComponents(self);
            textConsumer.accept(Text.literal(components.get(0)).formatted(Formatting.DARK_GREEN));
            textConsumer.accept(Text.literal(components.get(1)).formatted(Formatting.DARK_GREEN));
            textConsumer.accept(Text.literal(components.get(2)).formatted(Formatting.DARK_GREEN));
        }
         */
    }
}
