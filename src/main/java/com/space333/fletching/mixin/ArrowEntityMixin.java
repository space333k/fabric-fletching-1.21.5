package com.space333.fletching.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArrowEntity.class)
public abstract class ArrowEntityMixin extends PersistentProjectileEntity {
    @Shadow protected abstract void setStack(ItemStack stack);

    protected ArrowEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "onHit", at = @At(value = "TAIL"))
    public void addLingeringCloud(LivingEntity target, CallbackInfo ci) {
        ArrowEntity arrow = (ArrowEntity) (Object) this;

        if(arrow.getItemStack().contains(DataComponentTypes.POTION_CONTENTS)) {
            if(arrow.getWorld() instanceof ServerWorld world) {
                this.spawnAreaEffectCloud(arrow, world, this.getItemStack());
            }
            this.setStack(new ItemStack(Items.ARROW));
        }
    }


    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        ArrowEntity arrow = (ArrowEntity) (Object) this;
        if(arrow.getItemStack().contains(DataComponentTypes.POTION_CONTENTS)) {
            if(arrow.getWorld() instanceof ServerWorld world) {
                this.spawnAreaEffectCloud(arrow, world, this.getItemStack());
            }
            this.setStack(new ItemStack(Items.ARROW));
        }
    }

    @Unique
    public void spawnAreaEffectCloud(ArrowEntity arrow, ServerWorld world, ItemStack stack) {
        AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(arrow.getWorld(), arrow.getX(), arrow.getY(), arrow.getZ());
        if (arrow.getOwner() instanceof LivingEntity livingEntity) {
            areaEffectCloudEntity.setOwner(livingEntity);
        }

        areaEffectCloudEntity.setRadius(3.0F);
        areaEffectCloudEntity.setRadiusOnUse(-0.5F);
        areaEffectCloudEntity.setDuration(60);
        areaEffectCloudEntity.setWaitTime(10);
        areaEffectCloudEntity.setRadiusGrowth(-areaEffectCloudEntity.getRadius() / areaEffectCloudEntity.getDuration());
        areaEffectCloudEntity.copyComponentsFrom(stack);
        world.spawnEntity(areaEffectCloudEntity);
    }

}
