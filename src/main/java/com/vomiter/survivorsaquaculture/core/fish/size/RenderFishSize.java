package com.vomiter.survivorsaquaculture.core.fish.size;

import com.vomiter.survivorsaquaculture.SAquaConfig;
import com.vomiter.survivorsaquaculture.core.fish.AquaFishes;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.RenderLivingEvent;

public class RenderFishSize {
    public static void onRenderLivingPre(RenderLivingEvent.Pre<?, ?> e) {
        if(!SAquaConfig.COMMON.ADJUST_FISH_SIZE.get()) return;
        EntityType<?> type = e.getEntity().getType();
        AquaFishes.byEntityType(type).ifPresent(f -> {
            float s = f.getSizeScale();
            if (s != 1f) e.getPoseStack().scale(s, s, s);
        });
    }
}
