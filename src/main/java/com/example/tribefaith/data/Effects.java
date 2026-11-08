package com.example.tribefaith.data;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public final class Effects {
    private Effects() {
    }

    /** 15 seconds, refreshed every 3 seconds, so it never flickers and fades quickly when the condition ends. */
    public static void give(ServerPlayer player, MobEffect effect, int amplifier) {
        player.addEffect(new MobEffectInstance(effect, 300, amplifier, true, false, true));
    }
}
