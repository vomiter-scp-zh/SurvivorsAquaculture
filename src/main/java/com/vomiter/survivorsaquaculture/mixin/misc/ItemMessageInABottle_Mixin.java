package com.vomiter.survivorsaquaculture.mixin.misc;

import com.teammetallurgy.aquaculture.item.ItemMessageInABottle;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemMessageInABottle.class)
public class ItemMessageInABottle_Mixin {
    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V", shift = At.Shift.AFTER))
    private void addGlassBottleAndPaper(Level world, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir){
        if(world.random.nextInt(4) == 0){
            player.addItem(TFCItems.SILICA_GLASS_BOTTLE.get().getDefaultInstance());
        }
        if(world.random.nextBoolean()) player.addItem(Items.PAPER.getDefaultInstance());
    }
}
