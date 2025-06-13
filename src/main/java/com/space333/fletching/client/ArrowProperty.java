package com.space333.fletching.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.space333.fletching.Fletching;
import com.space333.fletching.util.ArrowEffect;
import com.space333.fletching.util.ComponentHelper;
import net.minecraft.client.render.item.property.select.SelectProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.space333.fletching.util.ArrowEffect.DEFAULT;

public record ArrowProperty() implements SelectProperty<Integer> {
    public static final Codec<Integer> VALUE_CODEC = Codec.INT;
    public static final Type<ArrowProperty, Integer> TYPE = Type.create(
            MapCodec.unit(new ArrowProperty()), VALUE_CODEC
    );

    @Override
    public @Nullable Integer getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity user, int seed, ItemDisplayContext displayContext) {
        List<String> list = ComponentHelper.getComponents(stack);

        StringBuilder name = new StringBuilder();
        for(String effect: list) {
            if (!effect.equals(DEFAULT)) {
                name.append("_");
                name.append(effect.toLowerCase());
            }
        }

        return ArrowEffect.TEXTURE_NAME.get(name.toString());
    }

    @Override
    public Codec<Integer> valueCodec() {
        return VALUE_CODEC;
    }

    @Override
    public Type<ArrowProperty, Integer> getType() {
        return TYPE;
    }
}
