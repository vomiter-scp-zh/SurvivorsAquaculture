package com.vomiter.survivorsaquaculture.mixin.neptunium;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teammetallurgy.aquaculture.item.neptunium.NeptuniumArmor;
import com.vomiter.survivorsabilities.core.SAEffects;
import com.vomiter.survivorsabilities.core.effect.SenseEffect;
import net.dries007.tfc.common.TFCTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = NeptuniumArmor.class, remap = false)
public abstract class NeptuniumArmor_EffectMixin extends ArmorItem {
    public NeptuniumArmor_EffectMixin(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
        super(p_40386_, p_266831_, p_40388_);
    }

    @WrapOperation(method = "onArmorTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z", ordinal = 0))
    private boolean swapNightVisionEffect(Player instance, MobEffectInstance mobEffectInstance, Operation<Boolean> original){
        return instance.addEffect(new MobEffectInstance(SAEffects.SENSES.get(SenseEffect.SenseType.NEPTUNE).get(), 20, 0, false, false, false));
    }

    @WrapOperation(method = "onArmorTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isEyeInFluidType(Lnet/minecraftforge/fluids/FluidType;)Z"))
    private boolean swapWaterDefinition(Player instance, FluidType fluidType, Operation<Boolean> original){
        Vec3 eyePos = instance.getEyePosition();
        BlockPos eyeBlockPos = BlockPos.containing(eyePos);
        FluidState eyeFluid = instance.level().getFluidState(eyeBlockPos);
        if (eyeFluid.is(TFCTags.Fluids.ANY_WATER)) {
            return true;
        }
        return original.call(instance, fluidType);
    }
}
