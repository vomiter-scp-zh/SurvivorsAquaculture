package com.vomiter.survivorsaquaculture.mixin.fish.goal;

import net.dries007.tfc.common.entities.ai.GetHookedGoal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MoveToBlockGoal.class)
public interface MoveToBlockGoal_Accessor {
    @Accessor("mob")
    PathfinderMob getMob();

}
