package com.example.tribefaith.client;

import net.minecraft.client.Minecraft;

/** Only ever loaded on the client (called through DistExecutor). */
public final class ClientHooks {
    private ClientHooks() {
    }

    public static void openSelectionScreen() {
        Minecraft.getInstance().setScreen(new SelectionScreen());
    }
}
