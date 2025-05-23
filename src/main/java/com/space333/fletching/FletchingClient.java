package com.space333.fletching;

import com.space333.fletching.screen.FletchingScreen;
import com.space333.fletching.screen.ModScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.screen.ScreenHandlerType;

public class FletchingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandlers.FLETCHING_SCREEN_HANDLER, FletchingScreen::new);
    }
}
