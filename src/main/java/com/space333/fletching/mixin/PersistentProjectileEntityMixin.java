package com.space333.fletching.mixin;

import com.space333.fletching.entity.custom.SpecialArrowEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.space333.fletching.util.ArrowEffect.BOUNCING;

@Mixin(PersistentProjectileEntity.class)
public class PersistentProjectileEntityMixin {

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isAir()Z"))
    public boolean isPhasing(BlockState instance) {
        PersistentProjectileEntity self = (PersistentProjectileEntity) (Object) this;

        if(self instanceof SpecialArrowEntity arrow) {
            if(arrow.hasEffect(BOUNCING) && arrow.getVelocity().length() >= 0.01) {
                return true;
            }
        }
        return instance.isAir();
    }
}
