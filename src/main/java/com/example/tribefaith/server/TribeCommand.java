package com.example.tribefaith.server;

import com.example.tribefaith.data.Religion;
import com.example.tribefaith.data.Tribe;
import com.example.tribefaith.net.ModNetwork;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public final class TribeCommand {
    private TribeCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("tribe")
                .then(Commands.literal("info").executes(ctx -> {
                    ServerPlayer p = ctx.getSource().getPlayerOrException();
                    Tribe t = Identity.getTribe(p);
                    Religion r = Identity.getReligion(p);
                    Component msg = (t == null || r == null)
                            ? Component.literal("ยังไม่ได้เลือกเผ่า/ศาสนา")
                            : Component.literal("เผ่า: " + t.displayName() + " | ศาสนา: " + r.displayName());
                    ctx.getSource().sendSuccess(() -> msg, false);
                    return 1;
                }))
                .then(Commands.literal("reset")
                        .requires(s -> s.hasPermission(2))
                        .executes(ctx -> reset(ctx.getSource(), ctx.getSource().getPlayerOrException()))
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(ctx -> reset(ctx.getSource(), EntityArgument.getPlayer(ctx, "target"))))));
    }

    private static int reset(CommandSourceStack src, ServerPlayer target) {
        Identity.clear(target);
        Identity.apply(target, false);
        ModNetwork.openScreenFor(target);
        src.sendSuccess(() -> Component.literal("รีเซ็ตเผ่า/ศาสนาของ " + target.getName().getString() + " แล้ว"), true);
        return 1;
    }
}
