package com.space333.fletching.component;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.function.Consumer;

public final class LoadedProjectileComponent implements TooltipAppender {
    public static final LoadedProjectileComponent DEFAULT = new LoadedProjectileComponent(ItemStack.EMPTY);
    public static final Codec<LoadedProjectileComponent> CODEC = ItemStack.CODEC
            .xmap(LoadedProjectileComponent::new, loadedArrowComponent -> loadedArrowComponent.projectile);
    public static final PacketCodec<RegistryByteBuf, LoadedProjectileComponent> PACKET_CODEC = ItemStack.PACKET_CODEC
            .xmap(LoadedProjectileComponent::new, component -> component.projectile);
    private final ItemStack projectile;

    private LoadedProjectileComponent(ItemStack projectiles) {
        this.projectile = projectiles;
    }

    public static LoadedProjectileComponent of(ItemStack projectile) {
        return new LoadedProjectileComponent(projectile.copy());
    }

    public boolean contains(Item item) {
        return this.projectile.isOf(item);
    }

    public ItemStack getProjectile() {
        return this.projectile;
    }

    public boolean isEmpty() {
        return this.projectile.isEmpty();
    }

    public String toString() {
        return "ChargedProjectiles[items=" + this.projectile + "]";
    }

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
        ItemStack itemStack = null;
        int i = 0;

        if (itemStack == null) {
            itemStack = this.projectile;
            i = 1;
        } else if (ItemStack.areEqual(itemStack, this.projectile)) {
            i++;
        } else {
            appendProjectileTooltip(context, textConsumer, itemStack, i);
            itemStack = this.projectile;
            i = 1;
        }

        if (itemStack != null) {
            appendProjectileTooltip(context, textConsumer, itemStack, i);
        }
    }

    private static void appendProjectileTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, ItemStack projectile, int count) {
        if (count == 1) {
            textConsumer.accept(Text.translatable("item.minecraft.crossbow.projectile.single", projectile.toHoverableText()));
        } else {
            textConsumer.accept(Text.translatable("item.minecraft.crossbow.projectile.multiple", count, projectile.toHoverableText()));
        }

        TooltipDisplayComponent tooltipDisplayComponent = projectile.getOrDefault(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplayComponent.DEFAULT);
        projectile.appendTooltip(
                context, tooltipDisplayComponent, null, TooltipType.BASIC, tooltip -> textConsumer.accept(Text.literal("  ").append(tooltip).formatted(Formatting.GRAY))
        );
    }
}
