package com.space333.fletching.entity.client;

import com.space333.fletching.Fletching;
import com.space333.fletching.entity.custom.SpecialArrowEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class CustomArrowRenderer extends ProjectileEntityRenderer<SpecialArrowEntity, ProjectileEntityRenderState> {
    public static final Identifier TEXTURE = Identifier.of(Fletching.MOD_ID,"textures/entity/projectiles/custom_arrow.png");

    public CustomArrowRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected Identifier getTexture(ProjectileEntityRenderState state) {
        return TEXTURE;
    }

    public ProjectileEntityRenderState createRenderState() {
        return new ProjectileEntityRenderState();
    }
}
