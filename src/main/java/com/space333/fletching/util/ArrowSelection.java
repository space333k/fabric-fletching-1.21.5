package com.space333.fletching.util;

import com.space333.fletching.Fletching;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;

import java.util.function.Predicate;

public class ArrowSelection {

    public static ItemStack switchArrow(LivingEntity shooter, ItemStack arrow, ItemStack weapon) {
        if (!(weapon.getItem() instanceof RangedWeaponItem)) {
            return ItemStack.EMPTY;
        } else {
            if(shooter instanceof PlayerEntity playerShooter) {
                Predicate<ItemStack> predicate = ((RangedWeaponItem)weapon.getItem()).getHeldProjectiles();
                return getNextArrow(arrow, playerShooter, predicate);
            }

        }
        return arrow;
    }

    public static ItemStack getNextArrow(ItemStack arrow, PlayerEntity playerShooter, Predicate<ItemStack> predicate) {
        boolean arrowFound = false;
        boolean firstArrowCheck = true;
        ItemStack firstArrow = arrow.copy();

        Inventory inventory = playerShooter.getInventory();


        ItemStack itemStack = RangedWeaponItem.getHeldProjectile(playerShooter, predicate);
        if (predicate.test(itemStack)) {
            if(arrowFound) {
                return itemStack;
            }

            if(ItemStack.areItemsAndComponentsEqual(arrow, itemStack)) {
                arrowFound = true;
            }

            if(firstArrowCheck) {
                firstArrow = itemStack.copy();
                firstArrowCheck = false;
            }
        }

        for (int i = 0; i < inventory.size(); i++) {
            itemStack = inventory.getStack(i);
            if (predicate.test(itemStack)) {
                if(arrowFound) {
                    return itemStack;
                }

                if(ItemStack.areItemsAndComponentsEqual(arrow, itemStack)) {
                    arrowFound = true;
                }

                if(firstArrowCheck) {
                    firstArrow = itemStack.copy();
                    firstArrowCheck = false;
                }
            }
        }
        return firstArrow;
    }
}
