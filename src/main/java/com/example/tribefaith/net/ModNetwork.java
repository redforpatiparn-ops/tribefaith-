package com.example.tribefaith.net;

import com.example.tribefaith.TribeFaithMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public final class ModNetwork {
    private static final String PROTOCOL = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(TribeFaithMod.MODID, "main"),
            () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);

    private ModNetwork() {
    }

    public static void register() {
        int id = 0;
        CHANNEL.registerMessage(id++, OpenSelectionPacket.class,
                OpenSelectionPacket::encode, OpenSelectionPacket::decode, OpenSelectionPacket::handle);
        CHANNEL.registerMessage(id++, SelectionPacket.class,
                SelectionPacket::encode, SelectionPacket::decode, SelectionPacket::handle);
    }

    public static void openScreenFor(ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new OpenSelectionPacket());
    }
}
