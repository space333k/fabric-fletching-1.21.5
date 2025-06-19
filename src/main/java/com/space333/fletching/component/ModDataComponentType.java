package com.space333.fletching.component;

import com.space333.fletching.Fletching;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

import java.util.function.UnaryOperator;

public class ModDataComponentType {
    public static final ComponentType<String> ARROW_FEATHER = register("arrow_feather",builder -> builder.codec(Codecs.NON_EMPTY_STRING).packetCodec(PacketCodecs.STRING));
    public static final ComponentType<String> ARROW_SHAFT = register("arrow_shaft",builder -> builder.codec(Codecs.NON_EMPTY_STRING).packetCodec(PacketCodecs.STRING));
    public static final ComponentType<String> ARROW_TIP = register("arrow_tip",builder -> builder.codec(Codecs.NON_EMPTY_STRING).packetCodec(PacketCodecs.STRING));

    public static final ComponentType<LoadedProjectileComponent> LOADED_ARROW = register("loaded_arrow",builder -> builder.codec(LoadedProjectileComponent.CODEC).packetCodec(LoadedProjectileComponent.PACKET_CODEC));


    private  static <T>ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(Fletching.MOD_ID, name),
                builderOperator.apply(ComponentType.builder()).build()
        );
    }

    public static void registerDataComponentTypes() {
        Fletching.LOGGER.info("Registering Data Component Types for" + Fletching.MOD_ID);
    }
}
