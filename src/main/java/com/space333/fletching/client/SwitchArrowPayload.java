package com.space333.fletching.client;

import com.space333.fletching.Fletching;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SwitchArrowPayload(ItemStack weapon) implements CustomPayload {
    public static final Identifier PACKET_ID = Identifier.of(Fletching.MOD_ID, "switch_arrows");

    public static final CustomPayload.Id<SwitchArrowPayload> ID = new CustomPayload.Id<>(PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, SwitchArrowPayload> CODEC = PacketCodec.tuple(ItemStack.PACKET_CODEC, SwitchArrowPayload::weapon, SwitchArrowPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
