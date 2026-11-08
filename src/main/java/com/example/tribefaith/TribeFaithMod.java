package com.example.tribefaith;

import com.example.tribefaith.net.ModNetwork;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TribeFaithMod.MODID)
public class TribeFaithMod {
    public static final String MODID = "tribefaith";

    public TribeFaithMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ModNetwork::register);
    }
}
