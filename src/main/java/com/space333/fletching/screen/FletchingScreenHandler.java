package com.space333.fletching.screen;

import com.space333.fletching.Fletching;
import com.space333.fletching.util.FletchingRecipes;
import com.space333.fletching.util.ModTags;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

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

    private int inputUsage;

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
        ItemStack FeatherStack = inventory.getStack(INPUT1_ID);
        ItemStack ShaftStack = inventory.getStack(INPUT2_ID);
        ItemStack TipStack = inventory.getStack(INPUT3_ID);
        ItemStack outputStack = ItemStack.EMPTY;

        int inputCount = Math.min(FeatherStack.getCount(), ShaftStack.getCount());
        inputCount = Math.min(TipStack.getCount(), inputCount);
        int outputCount = 0;
        if(inputCount > 64/ARROW_OUTPUT_RATIO) {
            outputCount = 64;
        }
        else {
            outputCount = inputCount * ARROW_OUTPUT_RATIO;
        }

        Item output = FletchingRecipes.getRecipeOutput(FeatherStack.getItem(), ShaftStack.getItem(), TipStack.getItem());

        if (output != Items.AIR) {
            outputStack = new ItemStack(output, outputCount);
        }

        this.inputUsage = inputCount;
        this.result.setStack(0, outputStack);
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
                return itemStack.isIn(ModTags.Items.ARROW_SHAFT);
            }
        });
        this.addSlot(new Slot(this.input, INPUT3_ID, INPUT1_X + 2*INPUT_WIDTH, INPUT1_Y - 2*INPUT_HEIGHT) {
            @Override
            public boolean canInsert(ItemStack itemStack) {
                return itemStack.isIn(ModTags.Items.ARROW_TIP);
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

    public void onTakeItem(PlayerEntity player, ItemStack itemStack) {
        int inputTake = itemStack.getCount()/ARROW_OUTPUT_RATIO;

        for(int i = 0; i <= INPUT3_ID; i++) {
            this.input.removeStack(i, inputTake);
        }

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
