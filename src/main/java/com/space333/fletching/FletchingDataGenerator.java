package com.space333.fletching;

import com.space333.fletching.datagen.ModItemTagProvider;
import com.space333.fletching.datagen.ModModelProvider;
import com.space333.fletching.util.ArrowEffect;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class FletchingDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		ArrowEffect.generateTextureName();

		pack.addProvider(ModItemTagProvider::new);
		pack.addProvider(ModModelProvider::new);
	}
}
