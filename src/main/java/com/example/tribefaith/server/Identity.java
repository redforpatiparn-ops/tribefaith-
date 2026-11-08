package com.example.tribefaith.server;

import com.example.tribefaith.data.AttrMod;
import com.example.tribefaith.data.Buffed;
import com.example.tribefaith.data.Religion;
import com.example.tribefaith.data.Tribe;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistries;

/** Stores and applies a player's tribe + religion. */
public final class Identity {
    private static final String KEY_TRIBE = "tribefaith_tribe";
    private static final String KEY_RELIGION = "tribefaith_religion";

    private Identity() {
    }

    // ---------- storage ----------

    private static CompoundTag data(Player p) {
        return p.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
    }

    public static Tribe getTribe(Player p) {
        return Tribe.byId(data(p).getString(KEY_TRIBE));
    }

    public static Religion getReligion(Player p) {
        return Religion.byId(data(p).getString(KEY_RELIGION));
    }

    public static boolean has(Player p) {
        return getTribe(p) != null && getReligion(p) != null;
    }

    public static void set(Player p, Tribe t, Religion r) {
        CompoundTag root = p.getPersistentData();
        CompoundTag tag = root.getCompound(Player.PERSISTED_NBT_TAG);
        tag.putString(KEY_TRIBE, t.id());
        tag.putString(KEY_RELIGION, r.id());
        root.put(Player.PERSISTED_NBT_TAG, tag);
    }

    public static void clear(Player p) {
        CompoundTag root = p.getPersistentData();
        CompoundTag tag = root.getCompound(Player.PERSISTED_NBT_TAG);
        tag.remove(KEY_TRIBE);
        tag.remove(KEY_RELIGION);
        root.put(Player.PERSISTED_NBT_TAG, tag);
    }

    // ---------- buffs ----------

    private static UUID uuid(String source, Attribute attribute) {
        ResourceLocation key = ForgeRegistries.ATTRIBUTES.getKey(attribute);
        return UUID.nameUUIDFromBytes(("tribefaith:" + source + ":" + key).getBytes(StandardCharsets.UTF_8));
    }

    /** Removes old modifiers and adds the ones for the current choice. Safe to call any time. */
    public static void apply(ServerPlayer p, boolean healToFull) {
        for (Attribute a : ForgeRegistries.ATTRIBUTES) {
            AttributeInstance inst = p.getAttribute(a);
            if (inst != null) {
                inst.removeModifier(uuid("tribe", a));
                inst.removeModifier(uuid("religion", a));
            }
        }

        Tribe t = getTribe(p);
        Religion r = getReligion(p);
        if (t != null) {
            addMods(p, "tribe", t);
        }
        if (r != null) {
            addMods(p, "religion", r);
        }

        if (healToFull || p.getHealth() > p.getMaxHealth()) {
            p.setHealth(p.getMaxHealth());
        }
    }

    private static void addMods(ServerPlayer p, String source, Buffed b) {
        for (AttrMod m : b.mods()) {
            AttributeInstance inst = p.getAttribute(m.attribute());
            if (inst == null) {
                continue;
            }
            inst.addPermanentModifier(new AttributeModifier(
                    uuid(source, m.attribute()), "tribefaith_" + source + "_" + b.id(), m.amount(), m.op()));
        }
    }

    /** Potion-like effects, called every 3 seconds. */
    public static void tickEffects(ServerPlayer p) {
        Tribe t = getTribe(p);
        Religion r = getReligion(p);
        if (t != null) {
            t.tickEffects(p);
        }
        if (r != null) {
            r.tickEffects(p);
        }
    }
}
