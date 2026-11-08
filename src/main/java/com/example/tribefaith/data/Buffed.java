package com.example.tribefaith.data;

import java.util.List;
import net.minecraft.server.level.ServerPlayer;

public interface Buffed {
    String id();

    String displayName();

    String description();

    /** Permanent attribute changes (hearts, speed, armor, ...). */
    List<AttrMod> mods();

    /** Called every 3 seconds on the server for potion-like effects. */
    void tickEffects(ServerPlayer player);
}
