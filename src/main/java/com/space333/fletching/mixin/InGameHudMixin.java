package com.space333.fletching.mixin;

import com.space333.fletching.component.ModDataComponentType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void renderArrowName(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;

        if (client.options.hudHidden || player == null) return;

        ItemStack held = player.getMainHandStack();
        if (!(held.getItem() instanceof RangedWeaponItem)) return;

        if(held.contains(ModDataComponentType.LOADED_ARROW)) {
            ItemStack projectile = held.get(ModDataComponentType.LOADED_ARROW).getProjectile();

            if (projectile.isEmpty()) return;

            Text name = projectile.getName();

            int screenWidth = client.getWindow().getScaledWidth();
            int x = screenWidth / 2 - client.textRenderer.getWidth(name) / 2;
            int y = 10;

            context.drawText(client.textRenderer, name, x, y, 0xFFFFFF, true);
        }
    }
}
