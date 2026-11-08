package com.example.tribefaith.server;

import com.example.tribefaith.TribeFaithMod;
import com.example.tribefaith.net.ModNetwork;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TribeFaithMod.MODID)
public class ServerEvents {

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent e) {
        if (e.getEntity() instanceof ServerPlayer sp) {
            sync(sp);
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent e) {
        if (e.getEntity() instanceof ServerPlayer sp) {
            sync(sp);
        }
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone e) {
        CompoundTag old = e.getOriginal().getPersistentData();
        if (old.contains(Player.PERSISTED_NBT_TAG)) {
            e.getEntity().getPersistentData().put(Player.PERSISTED_NBT_TAG,
                    old.getCompound(Player.PERSISTED_NBT_TAG).copy());
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if (e.phase == TickEvent.Phase.END && e.side.isServer()
                && e.player instanceof ServerPlayer sp
                && sp.isAlive() && sp.tickCount % 60 == 0) {
            Identity.tickEffects(sp);
        }
    }

    @SubscribeEvent
    public static void onCommands(RegisterCommandsEvent e) {
        TribeCommand.register(e.getDispatcher());
    }

    private static void sync(ServerPlayer sp) {
        if (Identity.has(sp)) {
            Identity.apply(sp, false);
        } else {
            ModNetwork.openScreenFor(sp);
        }
    }
}
