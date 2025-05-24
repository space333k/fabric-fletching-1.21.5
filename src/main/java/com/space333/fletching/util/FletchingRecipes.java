package com.space333.fletching.util;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FletchingRecipes {

    public static Map<List<Item>, Item> recipes = new HashMap<>();


    public static Item getRecipeOutput(Item feather, Item shaft, Item tip) {
        List<Item> ingredients = List.of(feather, shaft, tip);
        Item output = Items.AIR;

        if(recipes.containsKey(ingredients)) {
            output = recipes.get(ingredients);
        }

        return output;
    }

    public static void createAllRecipes() {
        addRecipe(Items.FEATHER, Items.STICK, Items.FLINT, Items.ARROW);
        addRecipe(Items.FEATHER, Items.BLAZE_ROD, Items.FLINT, Items.SPECTRAL_ARROW);
    }

    private static void addRecipe(Item feather, Item shaft, Item tip, Item output) {
        recipes.put(List.of(feather, shaft, tip), output);
    }




}
