package com.space333.fletching;

import com.space333.fletching.client.SwitchArrowPayload;
import com.space333.fletching.component.LoadedProjectileComponent;
import com.space333.fletching.component.ModDataComponentType;
import com.space333.fletching.entity.ModEntityType;
import com.space333.fletching.item.ModItems;
import com.space333.fletching.screen.ModScreenHandlers;
import com.space333.fletching.util.ArrowEffect;
import com.space333.fletching.util.ArrowSelection;
import com.space333.fletching.util.ComponentHelper;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.DispenserBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

		PayloadTypeRegistry.playC2S().register(SwitchArrowPayload.ID, SwitchArrowPayload.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(SwitchArrowPayload.ID, (payload, context) -> {
			PlayerEntity player = context.player();
			player.getServer().execute(() -> {
				ItemStack weapon = player.getMainHandStack();
				if(weapon.contains(ModDataComponentType.LOADED_ARROW) && (weapon.get(DataComponentTypes.CHARGED_PROJECTILES) == ChargedProjectilesComponent.DEFAULT || !weapon.contains(DataComponentTypes.CHARGED_PROJECTILES))) {
					LoadedProjectileComponent loadedProjectileComponent = weapon.get(ModDataComponentType.LOADED_ARROW);

					ItemStack nextArrow;
					if(loadedProjectileComponent == null) {
						nextArrow = ArrowSelection.switchArrow(player, ItemStack.EMPTY, weapon);
					}
					else {
						ItemStack arrow = loadedProjectileComponent.getProjectile();
						nextArrow = ArrowSelection.switchArrow(player, arrow, weapon);
					}
					weapon.remove(ModDataComponentType.LOADED_ARROW);
					weapon.set(ModDataComponentType.LOADED_ARROW, LoadedProjectileComponent.of(nextArrow));
				}
			});
		});


	}
}