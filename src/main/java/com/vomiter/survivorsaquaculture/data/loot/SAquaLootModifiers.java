package com.vomiter.survivorsaquaculture.data.loot;

import com.mojang.serialization.Codec;
import com.vomiter.survivorsaquaculture.SurvivorsAquaculture;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SAquaLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, SurvivorsAquaculture.MODID);

    public static final RegistryObject<Codec<WormLootModifier>> WORM =
            LOOT_MODIFIERS.register("worm", () -> WormLootModifier.CODEC);
}
