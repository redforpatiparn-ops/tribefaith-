package com.example.tribefaith.net;

import com.example.tribefaith.data.Religion;
import com.example.tribefaith.data.Tribe;
import com.example.tribefaith.server.Identity;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

/** Client -> server: the player's choice. */
public class SelectionPacket {
    private final String tribe;
    private final String religion;

    public SelectionPacket(String tribe, String religion) {
        this.tribe = tribe;
        this.religion = religion;
    }

    public static void encode(SelectionPacket msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.tribe, 32);
        buf.writeUtf(msg.religion, 32);
    }

    public static SelectionPacket decode(FriendlyByteBuf buf) {
        return new SelectionPacket(buf.readUtf(32), buf.readUtf(32));
    }

    public static void handle(SelectionPacket msg, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context c = ctx.get();
        c.enqueueWork(() -> {
            ServerPlayer player = c.getSender();
            if (player == null || Identity.has(player)) {
                return; // already chosen: ignore (prevents cheating via fake packets)
            }
            Tribe t = Tribe.byId(msg.tribe);
            Religion r = Religion.byId(msg.religion);
            if (t == null || r == null) {
                return;
            }
            Identity.set(player, t, r);
            Identity.apply(player, true);
            player.sendSystemMessage(Component
                    .literal("คุณเลือกเผ่า " + t.displayName() + " และศรัทธา " + r.displayName())
                    .withStyle(ChatFormatting.GOLD));
        });
        c.setPacketHandled(true);
    }
}
