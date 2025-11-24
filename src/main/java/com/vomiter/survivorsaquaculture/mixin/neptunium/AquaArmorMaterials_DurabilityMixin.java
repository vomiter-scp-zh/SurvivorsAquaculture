package com.vomiter.survivorsaquaculture.mixin.neptunium;

import com.teammetallurgy.aquaculture.api.AquaArmorMaterials;
import net.dries007.tfc.common.TFCArmorMaterials;
import net.minecraft.world.item.ArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = com.teammetallurgy.aquaculture.api.AquaArmorMaterials.class, remap = false)
public abstract class AquaArmorMaterials_DurabilityMixin {

    @Inject(method = "getDurabilityForType", at = @At("HEAD"), cancellable = true, remap = true)
    private void overrideDurability(ArmorItem.Type type, CallbackInfoReturnable<Integer> cir) {
        var self = (AquaArmorMaterials) (Object) this;
        if (self.equals(AquaArmorMaterials.NEPTUNIUM)) {
            cir.setReturnValue(TFCArmorMaterials.BLUE_STEEL.getDurabilityForType(type));
        }
    }
}
