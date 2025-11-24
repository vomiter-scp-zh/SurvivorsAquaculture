package com.vomiter.survivorsaquaculture.core.fish;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FishAttributes {
    @SubscribeEvent
    public static void onFinalizeSpawn(MobSpawnEvent.FinalizeSpawn e) {
        LivingEntity le = e.getEntity();
        AquaFishes.byEntityType(le.getType()).ifPresent(f -> {
            float s = f.getSizeScale();
            if (s != 1f) setMaxHealth(le, le.getMaxHealth() * s);
        });
    }

    private static void setMaxHealth(LivingEntity le, double newMax) {
        var inst = le.getAttribute(Attributes.MAX_HEALTH);
        if (inst != null) {
            inst.setBaseValue(newMax);
            if (le.getHealth() < le.getMaxHealth()) le.setHealth((float) le.getMaxHealth());
        }
    }
}
