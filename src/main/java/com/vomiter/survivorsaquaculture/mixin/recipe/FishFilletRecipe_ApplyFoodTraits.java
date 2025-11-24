package com.vomiter.survivorsaquaculture.mixin.recipe;

import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.item.crafting.FishFilletRecipe;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.IFood;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FishFilletRecipe.class, remap = false)
public abstract class FishFilletRecipe_ApplyFoodTraits extends CustomRecipe {

    public FishFilletRecipe_ApplyFoodTraits(ResourceLocation p_252125_, CraftingBookCategory p_249010_) {
        super(p_252125_, p_249010_);
    }

    @Inject(
            method = "assemble(Lnet/minecraft/world/inventory/CraftingContainer;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;",
            at = @At("RETURN"),
            cancellable = true
    )
    private void applyTFCFoodTraitsAndGiveByproduct(CraftingContainer craftingInventory, RegistryAccess registryAccess, CallbackInfoReturnable<ItemStack> cir){
        ItemStack originalReturnStack = cir.getReturnValue();
        ItemStack fish = ItemStack.EMPTY;
        for (int i = 0; i < craftingInventory.getContainerSize(); ++i) {
            ItemStack stackSlot = craftingInventory.getItem(i);
            if (!stackSlot.isEmpty()) {
                Item item = stackSlot.getItem();
                if (AquacultureAPI.FISH_DATA.hasFilletAmount(item)) {
                    fish = stackSlot.copy();
                }
            }
        }
        IFood ingredientFood = FoodCapability.get(fish);
        IFood resultFood = FoodCapability.get(originalReturnStack);
        if(ingredientFood == null || resultFood == null) return;
        if(ingredientFood.isRotten()) {
            cir.setReturnValue(ItemStack.EMPTY);
            return;
        }

        resultFood.setCreationDate(ingredientFood.getCreationDate());
        ingredientFood.getTraits().forEach(t -> {
            FoodCapability.applyTrait(resultFood, t);
        });

        cir.setReturnValue(originalReturnStack);
    }

}
