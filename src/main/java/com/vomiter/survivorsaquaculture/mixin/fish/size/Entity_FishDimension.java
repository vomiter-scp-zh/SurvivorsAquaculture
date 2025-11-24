package com.vomiter.survivorsaquaculture.mixin.fish.size;

import com.vomiter.survivorsaquaculture.core.fish.AquaFishes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class Entity_FishDimension {
    @Inject(method = "getDimensions", at = @At("RETURN"), cancellable = true)
    private void scaleDimensions(Pose p_19975_, CallbackInfoReturnable<EntityDimensions> cir) {
        Entity self = (Entity)(Object)this;
        AquaFishes.byEntityType(self.getType()).ifPresent(f -> {
            float s = f.getSizeScale();
            if (s != 1f) cir.setReturnValue(cir.getReturnValue().scale(s));
        });

    }
}
