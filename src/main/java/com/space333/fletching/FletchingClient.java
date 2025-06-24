package com.space333.fletching;

import com.space333.fletching.client.ArrowProperty;
import com.space333.fletching.client.SwitchArrowPayload;
import com.space333.fletching.component.ModDataComponentType;
import com.space333.fletching.entity.ModEntityType;
import com.space333.fletching.entity.client.CustomArrowRenderer;
import com.space333.fletching.screen.FletchingScreen;
import com.space333.fletching.screen.ModScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.item.property.select.SelectProperties;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class FletchingClient implements ClientModInitializer {
    public static KeyBinding changeShootingArrow = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "text.fletching.keybinding.changeShootingArrow",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            "text.fletching.keybinding.category"
    ));

    private static boolean alreadyChangedArrow = false;

    @Override
    public void onInitializeClient() {
        SelectProperties.ID_MAPPER.put(Identifier.of(Fletching.MOD_ID, "custom_arrow"), ArrowProperty.TYPE);
        HandledScreens.register(ModScreenHandlers.FLETCHING_SCREEN_HANDLER, FletchingScreen::new);
        EntityRendererRegistry.register(ModEntityType.SPECIAL_ARROW, CustomArrowRenderer::new);



        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            boolean changeArrowPressed = changeShootingArrow.isPressed();
            if (changeArrowPressed && !alreadyChangedArrow) {
                assert client.player != null;
                ItemStack weapon = client.player.getMainHandStack();
                if (weapon.getItem() instanceof RangedWeaponItem) {
                    ClientPlayNetworking.send(new SwitchArrowPayload(weapon));
                }
            }
            alreadyChangedArrow = changeArrowPressed;
        });

    }
}
