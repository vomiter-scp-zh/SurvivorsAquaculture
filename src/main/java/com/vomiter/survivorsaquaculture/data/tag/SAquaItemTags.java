package com.vomiter.survivorsaquaculture.data.tag;

import com.teammetallurgy.aquaculture.init.AquaItems;
import com.teammetallurgy.aquaculture.init.FishRegistry;
import com.vomiter.survivorsaquaculture.SurvivorsAquaculture;
import com.vomiter.survivorsaquaculture.core.registry.SAquaItems;
import com.vomiter.survivorsaquaculture.core.registry.TFCMetalNeptunium;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Metal;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class SAquaItemTags extends ItemTagsProvider {

    public SAquaItemTags(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, SurvivorsAquaculture.MODID, existingFileHelper);
    }

    public static TagKey<Item> create(String path) {
        return TagKey.create(
                Registries.ITEM,
                Objects.requireNonNull(ResourceLocation.tryBuild(SurvivorsAquaculture.MODID, path))
        );
    }

    public static TagKey<Item> create(String namespace, String path) {
        return TagKey.create(
                Registries.ITEM,
                Objects.requireNonNull(ResourceLocation.tryBuild(namespace, path))
        );
    }


    public static final TagKey<Item> AQUA_FISH = create("aquafish");
    public static final TagKey<Item> FISH_HOOKS = create("fishing_hooks");
    public static final Map<TFCMetalNeptunium, Map<TFCMetalNeptunium.ItemType, TagKey<Item>>> METAL_ITEM_FORGE = new EnumMap<>(TFCMetalNeptunium.class);
    public static final Map<TFCMetalNeptunium, TagKey<Item>> METAL_ITEM_TFC = new EnumMap<>(TFCMetalNeptunium.class);

    static {
        for (TFCMetalNeptunium metal : TFCMetalNeptunium.values()) {
            Map<TFCMetalNeptunium.ItemType, TagKey<Item>> map = new EnumMap<>(TFCMetalNeptunium.ItemType.class);
            for (TFCMetalNeptunium.ItemType type : TFCMetalNeptunium.ItemType.values()) {
                map.put(type, create("forge", type.name().toLowerCase(Locale.ROOT) + "s/" + metal.getSerializedName()));
            }
            METAL_ITEM_FORGE.put(metal, map);
            METAL_ITEM_TFC.put(metal, create("tfc", "metal_item/" + metal.getSerializedName()));
        }
    }


    @Override
    protected void addTags(HolderLookup.@NotNull Provider p_256380_) {
        for (Metal.Default metal : Metal.Default.values()) {
            if(metal.hasTools()) {
                var fishHook = TFCItems.METAL_ITEMS.get(metal).get(Metal.ItemType.FISH_HOOK).get();
                tag(FISH_HOOKS).add(fishHook);
            }
        }

        tag(TFCTags.Items.SMALL_FISHING_BAIT)
                .add(AquaItems.WORM.get());

        tag(TFCTags.Items.LARGE_FISHING_BAIT)
                .add(AquaItems.MINNOW.get())
                .add(AquaItems.BLUEGILL.get());

        FishRegistry.fishEntities.forEach(ro -> {
            tag(AQUA_FISH).add(
                    ResourceKey.create(
                            Registries.ITEM,
                            Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(ro.get()))
                    )
            );
        });

        assert AquaItems.NEPTUNIUM_INGOT.getKey() != null;
        tag(TFCTags.Items.PILEABLE_INGOTS).add(
                AquaItems.NEPTUNIUM_INGOT.getKey()
        );

        for (TFCMetalNeptunium metal : TFCMetalNeptunium.values()) {
            for (TFCMetalNeptunium.ItemType type : TFCMetalNeptunium.ItemType.values()) {
                var item = SAquaItems.METAL_ITEMS.get(metal).get(type).getKey();
                if(item != null){
                    var forgeMetalTag = METAL_ITEM_FORGE.get(metal).get(type);
                    tag(forgeMetalTag).add(item);
                    tag(create("forge", type.name().toLowerCase(Locale.ROOT) + "s")).addTag(forgeMetalTag);
                    tag(METAL_ITEM_TFC.get(metal)).add(item);
                    var pileable = type.getPileable();
                    if(pileable != null) tag(pileable).add(item);
                }
            }
        }
    }

    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<TagsProvider.TagLookup<Block>> emptyBlockLookup =
                CompletableFuture.completedFuture(blockTagKey -> Optional.empty());

        generator.addProvider(event.includeServer(), new SAquaItemTags(output, lookupProvider, emptyBlockLookup, helper));
    }

}
