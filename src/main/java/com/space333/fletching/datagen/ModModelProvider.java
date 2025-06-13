package com.space333.fletching.datagen;

import com.space333.fletching.Fletching;
import com.space333.fletching.client.ArrowProperty;
import com.space333.fletching.item.ModItems;
import com.space333.fletching.util.ArrowEffect;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.SelectItemModel;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

        ItemModel.Unbaked unbakedDefault = ItemModels.basic(ModelIds.getItemModelId(ModItems.CUSTOM_ARROW));

        List<SelectItemModel.SwitchCase<Integer>> cases = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : ArrowEffect.TEXTURE_NAME.entrySet()) {
            Identifier id = itemModelGenerator.registerSubModel(ModItems.CUSTOM_ARROW, entry.getKey(), Models.GENERATED);
            ItemModel.Unbaked unbaked = ItemModels.basic(id);
            cases.add(ItemModels.switchCase(entry.getValue(), unbaked));
        }


        itemModelGenerator.output.accept(
                ModItems.CUSTOM_ARROW,
                ItemModels.select(
                        new ArrowProperty(),
                        cases
                )
        );



    }
}
