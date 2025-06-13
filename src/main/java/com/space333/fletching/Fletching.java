package com.space333.fletching;

import com.space333.fletching.client.ArrowProperty;
import com.space333.fletching.component.ModDataComponentType;
import com.space333.fletching.entity.ModEntityType;
import com.space333.fletching.item.ModItems;
import com.space333.fletching.screen.ModScreenHandlers;
import com.space333.fletching.util.ArrowEffect;
import com.space333.fletching.util.ComponentHelper;
import net.fabricmc.api.ModInitializer;

import net.minecraft.block.DispenserBlock;
import net.minecraft.client.render.item.property.select.ContextDimensionProperty;
import net.minecraft.client.render.item.property.select.SelectProperties;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Fletching implements ModInitializer {
	public static final String MOD_ID = "fletching";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final ChunkTicketType ARROW_TICKET = Registry.register(Registries.TICKET_TYPE, "custom_arrow", new ChunkTicketType(40L, false, ChunkTicketType.Use.LOADING_AND_SIMULATION));


	@Override
	public void onInitialize() {
		ComponentHelper.createAllArrowIDs();
		ArrowEffect.generateTextureName();

		ModScreenHandlers.registerScreenHandlers();
		ModDataComponentType.registerDataComponentTypes();
		ModItems.registerModItems();
		ModEntityType.registerModEntities();

		DispenserBlock.registerProjectileBehavior(ModItems.CUSTOM_ARROW);
	}
}