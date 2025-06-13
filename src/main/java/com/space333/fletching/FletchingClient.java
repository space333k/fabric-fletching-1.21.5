package com.space333.fletching;

import com.space333.fletching.client.ArrowProperty;
import com.space333.fletching.entity.ModEntityType;
import com.space333.fletching.entity.client.CustomArrowRenderer;
import com.space333.fletching.screen.FletchingScreen;
import com.space333.fletching.screen.ModScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.item.property.select.SelectProperties;
import net.minecraft.util.Identifier;

public class FletchingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SelectProperties.ID_MAPPER.put(Identifier.of(Fletching.MOD_ID, "custom_arrow"), ArrowProperty.TYPE);
        HandledScreens.register(ModScreenHandlers.FLETCHING_SCREEN_HANDLER, FletchingScreen::new);
        EntityRendererRegistry.register(ModEntityType.SPECIAL_ARROW, CustomArrowRenderer::new);
    }
}
