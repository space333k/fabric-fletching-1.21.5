package com.space333.fletching.mixin;

import com.space333.fletching.screen.FletchingScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.FletchingTableBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.*;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FletchingTableBlock.class)
public class FletchingTableBlockMixin extends CraftingTableBlock {
    @Unique
    private static final Text SCREEN_TITLE = Text.translatable("container.fletching.fletching_name");

    public FletchingTableBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory(
                (syncId, inventory, player) -> new FletchingScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos)), SCREEN_TITLE
        );
    }

    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void openHandler(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (!world.isClient) {
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
        }
        cir.setReturnValue(ActionResult.SUCCESS);
    }


}
