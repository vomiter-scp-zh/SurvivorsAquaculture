package com.vomiter.survivorsaquaculture.mixin.recipe;

import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.init.AquaItems;
import com.vomiter.survivorsaquaculture.core.registry.SAquaItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResultSlot.class)
public abstract class ResultSlot_FilletBonusMixin {
    @Shadow
    @Final
    private CraftingContainer craftSlots;
    @Shadow @Final private Player player;

    @Inject(method = "onTake", at = @At("TAIL"))
    private void giveBonus(Player p_150638_, ItemStack crafted, CallbackInfo ci) {
        if (player.level().isClientSide) return;

        ItemStack fish = ItemStack.EMPTY;
        for (int i = 0; i < craftSlots.getContainerSize(); ++i) {
            ItemStack slot = craftSlots.getItem(i);
            if (!slot.isEmpty() && AquacultureAPI.FISH_DATA.hasFilletAmount(slot.getItem())) {
                fish = slot.copy();
                break;
            }
        }
        if (fish.isEmpty()) return;

        int count = crafted.getCount();
        player.addItem(new ItemStack(AquaItems.FISH_BONES.get(), count / 4));

        float chance = (30f - count) / 30f;
        if (player.getRandom().nextFloat() < chance) {
            player.addItem(new ItemStack(SAquaItems.NEPTUNIAN_PEARL.get()));
        }
    }
}
