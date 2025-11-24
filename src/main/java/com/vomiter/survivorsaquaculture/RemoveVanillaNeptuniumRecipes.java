package com.vomiter.survivorsaquaculture;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.vomiter.survivorsaquaculture.mixin.RecipeManagerAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = SurvivorsAquaculture.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RemoveVanillaNeptuniumRecipes {
    private static final List<String> RAW_TARGETS = List.of(
            "axe",
            "boots",
            "block",
            "bow",
            "chestplate",
            "fillet_knife",
            "fishing_rod",
            "helmet",
            "hoe",
            "leggings",
            "pickaxe",
            "nugget",
            "shovel",
            "sword",
            "ingot_from_neptunium_block"
    );
    private static final List<ResourceLocation>TARGETS = new ArrayList<>();
    static {
        RAW_TARGETS.forEach(s -> TARGETS.add(new ResourceLocation(Aquaculture.MOD_ID, "neptunium_" + s)));
        TARGETS.add(new ResourceLocation(Aquaculture.MOD_ID, "neptunes_bounty"));
    }
    
    private static List<Recipe<?>> getRebuildWithoutNeptunium(AddReloadListenerEvent event){
        var serverResources = event.getServerResources();
        List<Recipe<?>> rebuilt = new ArrayList<>();
        if (serverResources == null) return rebuilt;

        var manager  = serverResources.getRecipeManager();
        var accessor = (RecipeManagerAccessor) manager;

        for (Map<ResourceLocation, Recipe<?>> byId
                : accessor.getRecipes().values()) {
            for (Map.Entry<ResourceLocation, Recipe<?>> e : byId.entrySet()) {
                if (!TARGETS.contains(e.getKey())) {
                    rebuilt.add(e.getValue());
                }
            }
        }
        return rebuilt;
    }

    @SubscribeEvent
    public static void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(new SimplePreparableReloadListener<Void>() {
            @Override protected @NotNull Void prepare(net.minecraft.server.packs.resources.@NotNull ResourceManager rm,
                                                      net.minecraft.util.profiling.@NotNull ProfilerFiller pf) { return null; }

            @Override protected void apply(@NotNull Void v,
                                           net.minecraft.server.packs.resources.@NotNull ResourceManager rm,
                                           net.minecraft.util.profiling.@NotNull ProfilerFiller pf) {
                var serverResources = event.getServerResources();
                if (serverResources == null) return;
                var manager  = serverResources.getRecipeManager();
                var accessor = (RecipeManagerAccessor) manager;
                accessor.callReplaceRecipes(getRebuildWithoutNeptunium(event));
            }
        });
    }
}
