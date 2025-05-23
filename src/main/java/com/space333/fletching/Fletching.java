package com.space333.fletching;

import com.space333.fletching.screen.ModScreenHandlers;
import com.space333.fletching.util.FletchingRecipes;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Fletching implements ModInitializer {
	public static final String MOD_ID = "fletching";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		FletchingRecipes.createAllRecipes();
		ModScreenHandlers.registerScreenHandlers();
	}
}