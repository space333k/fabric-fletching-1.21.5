package com.space333.fletching.entity;

import com.space333.fletching.Fletching;
import com.space333.fletching.entity.custom.SpecialArrowEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEntityType {
    public static final EntityType<SpecialArrowEntity> SPECIAL_ARROW = Registry.register(
            Registries.ENTITY_TYPE,
            getKey("special_arrow"),
            EntityType.Builder.<SpecialArrowEntity>create(SpecialArrowEntity::new, SpawnGroup.MISC)
                    .dropsNothing()
                    .dimensions(0.5F, 0.5F)
                    .eyeHeight(0.13F)
                    .maxTrackingRange(4)
                    .trackingTickInterval(1)
                    .build(getKey("special_arrow"))
    );

    public static RegistryKey<EntityType<?>> getKey(String id) {
        return RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(Fletching.MOD_ID, id));
    }

    public static void registerModEntities() {
        Fletching.LOGGER.info("Registering Mod Entities for " + Fletching.MOD_ID);
    }
}
