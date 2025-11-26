package com.vomiter.survivorsaquaculture.data;

import com.vomiter.survivorsaquaculture.SurvivorsAquaculture;
import com.vomiter.survivorsaquaculture.core.registry.SAquaFishMount;
import com.vomiter.survivorsaquaculture.data.tag.SAquaItemTags;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class SAquaRecipeProvider extends RecipeProvider {
    public SAquaRecipeProvider(PackOutput p_248933_) {
        super(p_248933_);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> out) {
        SAquaFishMount.TFC_FISH_MOUNT_ITEMS.forEach((wood, itemRo) -> {
            assert itemRo.getId() != null;
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, itemRo.get())
                    .pattern("BHB")
                    .pattern("BBB")
                    .define('B', TFCItems.LUMBER.get(wood).get())
                    .define('H', SAquaItemTags.FISH_HOOKS)
                    .unlockedBy("has_lumber", InventoryChangeTrigger.TriggerInstance.hasItems(TFCItems.LUMBER.get(wood).get()))
                    .save(out, new ResourceLocation(SurvivorsAquaculture.MODID, "crafting/" + itemRo.getId().getPath()));
        });
    }
}
