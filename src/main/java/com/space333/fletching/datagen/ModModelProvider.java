package com.space333.fletching.datagen;

import com.space333.fletching.item.ModItems;
import com.space333.fletching.util.ArrowEffect;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import net.minecraft.client.render.item.model.ItemModel;

import java.util.Map;

public class ModModelProvider extends FabricModelProvider {


    private final FabricDataOutput output;

    public ModModelProvider(FabricDataOutput output) {
        super(output);
        this.output = output;
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

        for (Map.Entry<String, Integer> entry : ArrowEffect.TEXTURE_NAME.entrySet()) {
            ItemModel.Unbaked unbaked = ItemModels.basic(itemModelGenerator.registerSubModel(ModItems.CUSTOM_ARROW, entry.getKey(), Models.GENERATED));

        }
        itemModelGenerator.output.accept(
                ModItems.CUSTOM_ARROW,
                ItemModels.select(
        )

    }
    /*
    private static void extracted(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.CUSTOM_ARROW, ItemModels.select());
    }

    private void writeBaseModel() {
        Path path = output.getResolver(DataOutput.OutputType.RESOURCE_PACK, Fletching.MOD_ID).resolve(Identifier.of(Fletching.MOD_ID, "assets/yourmod/models/item/dynamic_arrow.json"));
        JsonObject root = new JsonObject();
        root.addProperty("parent", "item/generated");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", "yourmod:item/dynamic_arrow_default");    root.add("textures", textures);
        JsonArray overrides = new JsonArray();
        for (Map.Entry<String, Integer> entry : TIP_VARIANTS.entrySet()) {
            JsonObject predicate = new JsonObject();
            predicate.addProperty("yourmod:tip", entry.getValue());
            JsonObject override = new JsonObject();
            override.add("predicate", predicate);
            override.addProperty("model", "yourmod:item/dynamic_arrow_" + entry.getKey());
            overrides.add(override);
        }
        root.add("overrides", overrides);
        try {
            DataProvider.writeToPath(JsonOps.INSTANCE, root, path);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to write base arrow model", e);
        }
    }

    private void generateVariants(ItemModelGenerator generator) {
        for(String variant: ArrowEffect.TEXTURE_NAME.keySet()) {
            generateArrowTexture(generator, variant);
        }
    }


    public static Identifier generateArrowTexture(ItemModelGenerator model, @Nullable String name) {
        if(name == null) {
            name = DEFAULT.toLowerCase() + "_" + DEFAULT.toLowerCase() + "_" + DEFAULT.toLowerCase() + "_";
        }
        return Models.GENERATED.upload(of(name), TextureMap.layer0(of("item/" + name)), model.modelCollector);
    }

    private static Identifier of(String name) {
        return Identifier.of(Fletching.MOD_ID, name + "arrow");
    }
     */
}
