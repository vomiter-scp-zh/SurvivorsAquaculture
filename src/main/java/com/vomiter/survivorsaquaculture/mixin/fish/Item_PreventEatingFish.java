package com.vomiter.survivorsaquaculture.mixin.fish;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.vomiter.survivorsaquaculture.data.tag.SAquaItemTags;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(Item.class)
public class Item_PreventEatingFish {
    @ModifyExpressionValue(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEdible()Z"))
    private boolean setNotEdible(
            boolean original
    ){
        Item self = (Item)(Object)this;
        if(new ItemStack(self).is(SAquaItemTags.AQUA_FISH)) return false;
        return original;
    }
}
