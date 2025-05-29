package com.space333.fletching;

import com.space333.fletching.entity.ModEntityType;
import com.space333.fletching.entity.client.SpecialArrowRenderer;
import com.space333.fletching.screen.FletchingScreen;
import com.space333.fletching.screen.ModScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class FletchingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandlers.FLETCHING_SCREEN_HANDLER, FletchingScreen::new);
        EntityRendererRegistry.register(ModEntityType.SPECIAL_ARROW, SpecialArrowRenderer::new);
    }
}
