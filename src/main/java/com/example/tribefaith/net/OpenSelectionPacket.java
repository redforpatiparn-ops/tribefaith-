package com.example.tribefaith.net;

import com.example.tribefaith.client.ClientHooks;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

/** Server -> client: open the selection screen. */
public class OpenSelectionPacket {
    public OpenSelectionPacket() {
    }

    public static void encode(OpenSelectionPacket msg, FriendlyByteBuf buf) {
    }

    public static OpenSelectionPacket decode(FriendlyByteBuf buf) {
        return new OpenSelectionPacket();
    }

    public static void handle(OpenSelectionPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHooks.openSelectionScreen()));
        ctx.get().setPacketHandled(true);
    }
}
