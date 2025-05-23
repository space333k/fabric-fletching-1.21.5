package com.space333.fletching.screen;

import com.space333.fletching.Fletching;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ModScreenHandlers {
    public static final ScreenHandlerType<FletchingScreenHandler> FLETCHING_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(Fletching.MOD_ID, "fletching_screen_handler"),
                    new ScreenHandlerType<>(FletchingScreenHandler::new, FeatureFlags.DEFAULT_ENABLED_FEATURES));

    public static void registerScreenHandlers() {
        Fletching.LOGGER.info("Registering Screen Handlers for " + Fletching.MOD_ID);
    }
}
