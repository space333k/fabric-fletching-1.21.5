package com.space333.fletching.item.custom;

import com.space333.fletching.Component.ModDataComponentType;
import com.space333.fletching.entity.custom.SpecialArrowEntity;
import com.space333.fletching.util.ComponentHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
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
import java.util.Objects;

public class SpecialArrowsItem extends ArrowItem {
    public SpecialArrowsItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack itemStack = super.getDefaultStack();
        itemStack.set(ModDataComponentType.ARROW_FEATHER, "default");
        itemStack.set(ModDataComponentType.ARROW_SHAFT, "default");
        itemStack.set(ModDataComponentType.ARROW_TIP, "default");
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

        StringBuilder name  = new StringBuilder();
        for(int i = 0; i < 3; i++) {
            if(!effects.get(i).equals("default")) {
                name.append(effects.get(i));
                name.append(" ");
            }
        }

        return Text.translatable(this.translationKey, Text.literal(name.toString()));
    }

}
