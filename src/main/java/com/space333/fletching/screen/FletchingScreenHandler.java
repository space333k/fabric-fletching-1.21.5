package com.space333.fletching.screen;

import com.space333.fletching.item.ModItems;
import com.space333.fletching.util.ComponentHelper;
import com.space333.fletching.util.ModTags;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.*;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;

public class FletchingScreenHandler extends ScreenHandler {
    public static final int INPUT1_X = 30;
    public static final int INPUT1_Y = 53;
    public static final int OUTPUT_X = 124;
    public static final int OUTPUT_Y = 35;
    public static final int INPUT_WIDTH = 18;
    public static final int INPUT_HEIGHT = 18;

    public static final int INPUT1_ID = 0;
    public static final int INPUT2_ID = 1;
    public static final int INPUT3_ID = 2;
    public static final int OUTPUT_ID = 3;

    private static final int INVENTORY_START = 4;
    private static final int INVENTORY_END = 31;
    private static final int HOTBAR_START = 31;
    private static final int HOTBAR_END = 40;

    private static final int ARROW_OUTPUT_RATIO = 4;
    private static final int POTION_ARROW_RATIO = 1;
    private static final int SPLASH_ARROW_RATIO = 8;
    private static final int LINGERING_ARROW_RATIO = 64;

    private boolean isCrafting;
    private boolean isTipping;

    private final Inventory result = new CraftingResultInventory();
    final Inventory input = new SimpleInventory(3) {
        @Override
        public void markDirty() {
            super.markDirty();
            FletchingScreenHandler.this.onContentChanged(this);
        }
    };
    private final ScreenHandlerContext context;

    public FletchingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public FletchingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ModScreenHandlers.FLETCHING_SCREEN_HANDLER, syncId);
        this.context = context;
        getInputSlots();
        getResultSlot();

        this.addPlayerSlots(playerInventory, 8, 84);
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        ItemStack featherStack = inventory.getStack(INPUT1_ID);
        ItemStack shaftStack = inventory.getStack(INPUT2_ID);
        ItemStack tipStack = inventory.getStack(INPUT3_ID);

        isCrafting = false;
        isTipping = false;

        if(!featherStack.isEmpty() && !shaftStack.isEmpty() && !tipStack.isEmpty()) {
            updateCraftingArrow(featherStack, shaftStack, tipStack);
            isCrafting = true;
        }
        else if(featherStack.isEmpty() && !shaftStack.isEmpty() && !tipStack.isEmpty()) {
            updateTippedArrow(shaftStack, tipStack);
            isTipping = true;
        }
        else {
            this.result.setStack(0, ItemStack.EMPTY);
        }



    }

    private void updateCraftingArrow(ItemStack featherStack, ItemStack shaftStack, ItemStack tipStack) {
        ItemStack outputStack = ItemStack.EMPTY;
        int inputCount = Math.min(featherStack.getCount(), shaftStack.getCount());
        inputCount = Math.min(tipStack.getCount(), inputCount);
        int outputCount;
        if(inputCount > 64/ARROW_OUTPUT_RATIO) {
            outputCount = 64;
        }
        else {
            outputCount = inputCount * ARROW_OUTPUT_RATIO;
        }

        outputStack = ComponentHelper.createComponents(featherStack.getItem(), shaftStack.getItem(), tipStack.getItem());
        outputStack.setCount(outputCount);

        this.result.setStack(0, outputStack);
    }

    private void updateTippedArrow(ItemStack arrowStack, ItemStack potionStack) {
        ItemStack outputStack = arrowStack.copy();
        if(arrowStack.isIn(ItemTags.ARROWS)) {
            int arrowCount = arrowStack.getCount();
            if(potionStack.getItem() == Items.POTION) {
                int potionCount = potionStack.getCount();
                PotionContentsComponent potionContentsComponent = potionStack.get(DataComponentTypes.POTION_CONTENTS);
                outputStack.setCount(Math.min(arrowCount, potionCount * POTION_ARROW_RATIO));
                outputStack.remove(DataComponentTypes.POTION_CONTENTS);
                outputStack.set(DataComponentTypes.POTION_CONTENTS, potionContentsComponent);
            }
            else if(potionStack.getItem() == Items.SPLASH_POTION) {
                int potionCount = potionStack.getCount();
                PotionContentsComponent potionContentsComponent = potionStack.get(DataComponentTypes.POTION_CONTENTS);
                outputStack.setCount(Math.min(arrowCount, potionCount * SPLASH_ARROW_RATIO));
                outputStack.remove(DataComponentTypes.POTION_CONTENTS);
                outputStack.set(DataComponentTypes.POTION_CONTENTS, potionContentsComponent);
            }
            else if(potionStack.getItem() == Items.LINGERING_POTION) {
                int potionCount = potionStack.getCount();
                PotionContentsComponent potionContentsComponent = potionStack.get(DataComponentTypes.POTION_CONTENTS);
                outputStack.setCount(Math.min(arrowCount, potionCount * LINGERING_ARROW_RATIO));
                outputStack.remove(DataComponentTypes.POTION_CONTENTS);
                outputStack.set(DataComponentTypes.POTION_CONTENTS, potionContentsComponent);
            }
        }

        this.result.setStack(0, outputStack);
    }

    public void onTakeItem(PlayerEntity player, ItemStack itemStack) {
        int outputCount = itemStack.getCount();

        if(isCrafting) {
            int inputTake = outputCount/ARROW_OUTPUT_RATIO;

            for(int i = 0; i <= INPUT3_ID; i++) {
                this.input.removeStack(i, inputTake);
            }
        }
        else if(isTipping) {
            ItemStack arrowStack = this.input.getStack(INPUT2_ID);
            ItemStack potionStack = this.input.getStack(INPUT3_ID);

            if(potionStack.getItem() == Items.POTION) {
                arrowStack.decrement(outputCount);
                potionStack.decrement((int) Math.ceil((double)outputCount/POTION_ARROW_RATIO));
            }
            else if(potionStack.getItem() == Items.SPLASH_POTION) {
                arrowStack.decrement(outputCount);
                potionStack.decrement((int) Math.ceil((double)outputCount/SPLASH_ARROW_RATIO));
            }
            else if(potionStack.getItem() == Items.LINGERING_POTION) {
                arrowStack.decrement(outputCount);
                potionStack.decrement((int) Math.ceil((double)outputCount/LINGERING_ARROW_RATIO));
            }
            this.input.setStack(INPUT2_ID, arrowStack);
            this.input.setStack(INPUT3_ID, potionStack);
        }


    }

    private void getInputSlots() {
        this.addSlot(new Slot(this.input, INPUT1_ID, INPUT1_X, INPUT1_Y) {
            @Override
            public boolean canInsert(ItemStack itemStack) {
                return itemStack.isIn(ModTags.Items.ARROW_FEATHERS);
            }
        });
        this.addSlot(new Slot(this.input, INPUT2_ID, INPUT1_X + INPUT_WIDTH, INPUT1_Y - INPUT_HEIGHT) {
            @Override
            public boolean canInsert(ItemStack itemStack) {
                return itemStack.isIn(ModTags.Items.ARROW_SHAFT) || itemStack.isIn(ItemTags.ARROWS);
            }
        });
        this.addSlot(new Slot(this.input, INPUT3_ID, INPUT1_X + 2*INPUT_WIDTH, INPUT1_Y - 2*INPUT_HEIGHT) {
            @Override
            public boolean canInsert(ItemStack itemStack) {
                boolean isPotion = itemStack.getItem() == Items.POTION || itemStack.getItem() == Items.LINGERING_POTION || itemStack.getItem() == Items.SPLASH_POTION;
                return itemStack.isIn(ModTags.Items.ARROW_TIP) || isPotion;
            }
        });
    }

    private void getResultSlot() {
        this.addSlot(new Slot(this.result, OUTPUT_ID, OUTPUT_X, OUTPUT_Y) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack itemStack) {
                FletchingScreenHandler.this.onTakeItem(player, itemStack);
            }
        });
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2 != null && slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot == OUTPUT_ID) {
                itemStack2.getItem().onCraftByPlayer(itemStack2, player);
                if (!this.insertItem(itemStack2, INVENTORY_START, INVENTORY_END, true)) {
                    return ItemStack.EMPTY;
                }

                slot2.onQuickTransfer(itemStack2, itemStack);
            } else if (slot >= INVENTORY_START && slot < HOTBAR_END) {
                if (!this.insertItem(itemStack2, INPUT1_ID, OUTPUT_ID, false)) {
                    if (slot < HOTBAR_START) {
                        if (!this.insertItem(itemStack2, HOTBAR_START, HOTBAR_END, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.insertItem(itemStack2, INVENTORY_START, INVENTORY_END, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.insertItem(itemStack2, INVENTORY_START, HOTBAR_END, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            } else {
                slot2.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot2.onTakeItem(player, itemStack);
            if (slot == OUTPUT_ID) {
                player.dropItem(itemStack2, false);
            }
        }

        return itemStack;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, pos) -> this.dropInventory(player, this.input));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, Blocks.FLETCHING_TABLE);
    }
}
