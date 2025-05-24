package com.space333.fletching.screen;

import com.space333.fletching.Fletching;
import com.space333.fletching.util.ModTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class FletchingScreen extends HandledScreen<FletchingScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(Fletching.MOD_ID,"textures/gui/container/fletching_table.png");
    private static final Identifier INGREDIENT_TEXTURE = Identifier.of(Fletching.MOD_ID,"container/fletching_table/ingredient_slots");
    private static final Identifier POTION_TEXTURE = Identifier.of(Fletching.MOD_ID,"container/fletching_table/potion_slot");
    private static final Identifier SPLASH_TEXTURE = Identifier.of(Fletching.MOD_ID,"container/fletching_table/splash_slot");
    private static final Identifier LINGERING_TEXTURE = Identifier.of(Fletching.MOD_ID,"container/fletching_table/lingering_slot");

    private static final int SLOT_X = 29;
    private static final int SLOT_Y = 16;

    private static long GUI_CHANGE_DELAY = 1;

    public FletchingScreenHandler handler;

    public FletchingScreen(FletchingScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.handler = handler;
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = 29;
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        super.drawForeground(context, mouseX, mouseY);
        ItemStack featherStack = handler.input.getStack(0);
        ItemStack shaftStack = handler.input.getStack(1);
        ItemStack tipStack = handler.input.getStack(2);
        ItemStack cursorStack = handler.getCursorStack();


        long time = System.currentTimeMillis();
        long cycle;

        boolean isPotion = tipStack.getItem() == Items.POTION || tipStack.getItem() == Items.LINGERING_POTION || tipStack.getItem() == Items.SPLASH_POTION;
        boolean isIngredient = featherStack.isIn(ModTags.Items.ARROW_FEATHERS) || shaftStack.isIn(ModTags.Items.ARROW_SHAFT) || tipStack.isIn(ModTags.Items.ARROW_TIP);
        boolean isCursorIngredient = cursorStack.isIn(ModTags.Items.ARROW_FEATHERS) || cursorStack.isIn(ModTags.Items.ARROW_SHAFT) || cursorStack.isIn(ModTags.Items.ARROW_TIP);
        boolean isCursorArrow = cursorStack.isIn(ItemTags.ARROWS);
        boolean isCursorPotion = cursorStack.getItem() == Items.POTION || cursorStack.getItem() == Items.LINGERING_POTION || cursorStack.getItem() == Items.SPLASH_POTION;


        if(featherStack.isEmpty() && (shaftStack.isIn(ItemTags.ARROWS) || isPotion || isCursorArrow || isCursorPotion)) {
            cycle = (time/(1000L * GUI_CHANGE_DELAY)) % 3;
        }
        else if(isIngredient || isCursorIngredient) {
            cycle = 3;
        }
        else {
            cycle = (time/(1000L * GUI_CHANGE_DELAY)) % 6;
        }

        if(cycle == 0) {
            context.drawGuiTexture(RenderLayer::getGuiTextured, POTION_TEXTURE, SLOT_X, SLOT_Y, 18*3, 18*3);
        }
        else if (cycle == 1) {
            context.drawGuiTexture(RenderLayer::getGuiTextured, SPLASH_TEXTURE, SLOT_X, SLOT_Y, 18*3, 18*3);
        }
        else if (cycle == 2) {
            context.drawGuiTexture(RenderLayer::getGuiTextured, LINGERING_TEXTURE, SLOT_X, SLOT_Y, 18*3, 18*3);
        }
        else if (cycle >= 3) {
            context.drawGuiTexture(RenderLayer::getGuiTextured, INGREDIENT_TEXTURE, SLOT_X, SLOT_Y, 18*3, 18*3);
        }




    }

    @Override
    protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {
        int i = this.x;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(RenderLayer::getGuiTextured, TEXTURE, i, j, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
