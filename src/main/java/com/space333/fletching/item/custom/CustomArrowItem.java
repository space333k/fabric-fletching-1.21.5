package com.space333.fletching.item.custom;

import com.space333.fletching.component.ModDataComponentType;
import com.space333.fletching.entity.custom.SpecialArrowEntity;
import com.space333.fletching.util.ComponentHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.space333.fletching.util.ArrowEffect.*;

public class CustomArrowItem extends ArrowItem {
    public CustomArrowItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack itemStack = super.getDefaultStack();
        itemStack.set(ModDataComponentType.ARROW_FEATHER, DEFAULT);
        itemStack.set(ModDataComponentType.ARROW_SHAFT, DEFAULT);
        itemStack.set(ModDataComponentType.ARROW_TIP, DEFAULT);
        return itemStack;
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
        return new SpecialArrowEntity(world, shooter, stack.copyWithCount(1), shotFrom);
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        SpecialArrowEntity arrowEntity = new SpecialArrowEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1), null);
        arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        return arrowEntity;
    }

    @Override
    public Text getName(ItemStack stack) {
        List<String> effects = ComponentHelper.getComponents(stack);
        String tier = "I";

        StringBuilder name  = new StringBuilder();
        if(stack.contains(DataComponentTypes.POTION_CONTENTS)) {
            name.append("Tipped ");
        }

        for(int i = 0; i < 3; i++) {
            if(!effects.get(i).equals(DEFAULT)) {
                if(effects.get(i).equals(TIER2)) {
                    tier = "II";
                }
                else if(effects.get(i).equals(TIER3)) {
                    tier = "III";
                }
                else if(effects.get(i).equals(TIER4)) {
                    tier = "IIII";
                }
                else {
                    name.append(effects.get(i));
                    name.append(" ");
                }
            }
        }

        return Text.translatable(this.translationKey, Text.literal(name.toString()), Text.literal(tier));
    }

}
