package com.vomiter.survivorsaquaculture;

import com.vomiter.survivorsaquaculture.core.registry.SAquaBlocks;
import com.vomiter.survivorsaquaculture.core.registry.SAquaFishMount;
import com.vomiter.survivorsaquaculture.core.registry.SAquaItems;
import com.vomiter.survivorsaquaculture.core.registry.TFCMetalNeptunium;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Objects;

public final class SAquaCreativeTabCompat {
    private static final ResourceKey<CreativeModeTab> AQUA_TAB_KEY =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, Objects.requireNonNull(ResourceLocation.tryBuild("aquaculture", "tab")));

    public static void onBuildCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(AQUA_TAB_KEY)) {
            event.accept(new ItemStack(SAquaItems.NEPTUNIAN_PEARL.get()));
            for (TFCMetalNeptunium metal : TFCMetalNeptunium.values()) {
                SAquaItems.METAL_ITEMS.get(metal).values().forEach(
                        ro -> event.accept(new ItemStack(ro.get())));
                SAquaItems.UNFINISHED_ARMORS.getOrDefault(metal, new HashMap<>()).values().forEach(
                        ro -> event.accept(new ItemStack(ro.get())));
                SAquaBlocks.METAL.get(metal).values().forEach(
                        ro -> event.accept(new ItemStack(ro.get().asItem())));
            }
            for (RegistryObject<Item> object : SAquaFishMount.TFC_FISH_MOUNT_ITEMS.values()) {
                event.accept(new ItemStack(object.get()));
            }

        }
    }
}
