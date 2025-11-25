package com.vomiter.survivorsaquaculture.data;

import com.teammetallurgy.aquaculture.init.FishRegistry;
import com.vomiter.survivorsabilities.core.SAEffects;
import com.vomiter.survivorsabilities.core.effect.SenseEffect;
import com.vomiter.survivorsabilities.data.SAEntityTypeTags;
import com.vomiter.survivorsaquaculture.SurvivorsAquaculture;
import com.vomiter.survivorsaquaculture.core.fish.AquaFishes;
import net.dries007.tfc.common.TFCTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class SAquaEntityTypeTags extends EntityTypeTagsProvider {

    private static final List<String> FRESH_WATER_FISH_LIST = List.of(
            "smallmouth_bass",
            "bluegill",
            "brown_trout",
            "carp",
            "catfish",
            "gar",
            "minnow",
            "muskellunge",
            "perch"
    );

    private static final List<String> ARID_FISH_LIST = List.of(
            "bayad",
            "boulti",
            "capitaine",
            "synodontis"
    );

    private static final List<String> ARCTIC_OCEAN_FISH_LIST = List.of(
            "atlantic_cod",
            "blackfish",
            "pacific_halibut",
            "atlantic_halibut",
            "atlantic_herring",
            "pink_salmon",
            "pollock",
            "rainbow_trout"
    );

    private static final List<String> SALT_WATER_FISH_LIST = List.of(
            "jellyfish",
            "red_grouper",
            "tuna"
    );

    private static final List<String> JUNGLE_FISH_LIST = List.of(
            "arapaima",
            "arrau_turtle",
            "piranha",
            "tambaqui"
    );

    private static final List<String> SWAMP_FISH_LIST = List.of(
            "box_turtle"
    );

    private static final List<String> MUSHROOM_ISLAND_FISH_LIST = List.of(
            "brown_shrooma",
            "red_shrooma"
    );

    private static final List<String> TWILIGHT_FOREST_FISH_LIST = List.of(
            "starshell_turtle"
    );

    private static final List<String> ANYWHERE_FISH_LIST = List.of(
            "goldfish"
    );

    // === Tags ===
    public static final TagKey<EntityType<?>> FRESH_WATER_FISH = create("fresh_water_fish");
    public static final TagKey<EntityType<?>> ARID_FISH = create("arid_fish");
    public static final TagKey<EntityType<?>> ARCTIC_OCEAN_FISH = create("arctic_ocean_fish");
    public static final TagKey<EntityType<?>> SALT_WATER_FISH = create("salt_water_fish");
    public static final TagKey<EntityType<?>> JUNGLE_FISH = create("jungle_fish");
    public static final TagKey<EntityType<?>> SWAMP_FISH = create("swamp_fish");
    public static final TagKey<EntityType<?>> MUSHROOM_ISLAND_FISH = create("mushroom_island_fish");
    public static final TagKey<EntityType<?>> TWILIGHT_FOREST_FISH = create("twilight_forest_fish");
    public static final TagKey<EntityType<?>> ANYWHERE_FISH = create("anywhere_fish");

    public static List<TagKey<EntityType<?>>> FISH_CATEGORIES = List.of(
            FRESH_WATER_FISH,
            ARID_FISH,
            ARCTIC_OCEAN_FISH,
            SALT_WATER_FISH,
            JUNGLE_FISH,
            SWAMP_FISH,
            MUSHROOM_ISLAND_FISH,
            TWILIGHT_FOREST_FISH,
            ANYWHERE_FISH
    );

    public SAquaEntityTypeTags(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> provider,
            ExistingFileHelper helper
    ) {
        super(output, provider, SurvivorsAquaculture.MODID, helper);
    }

    public static TagKey<EntityType<?>> create(String path) {
        return TagKey.create(
                Registries.ENTITY_TYPE,
                Objects.requireNonNull(ResourceLocation.tryBuild(SurvivorsAquaculture.MODID, path))
        );
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        addByList(FRESH_WATER_FISH, FRESH_WATER_FISH_LIST);
        addByList(ARID_FISH, ARID_FISH_LIST);
        addByList(ARCTIC_OCEAN_FISH, ARCTIC_OCEAN_FISH_LIST);
        addByList(SALT_WATER_FISH, SALT_WATER_FISH_LIST);
        addByList(JUNGLE_FISH, JUNGLE_FISH_LIST);
        addByList(SWAMP_FISH, SWAMP_FISH_LIST);
        addByList(MUSHROOM_ISLAND_FISH, MUSHROOM_ISLAND_FISH_LIST);
        addByList(TWILIGHT_FOREST_FISH, TWILIGHT_FOREST_FISH_LIST);
        addByList(ANYWHERE_FISH, ANYWHERE_FISH_LIST);

        for (AquaFishes fish : AquaFishes.values()) {
            if(fish.getFilletAmount() < 7){
                fish.entityType().ifPresent(e -> tag(SenseEffect.getTargetTag(SenseEffect.SenseType.NEPTUNE, SenseEffect.GlowColor.WHITE)).add(e));
            }
            else {
                fish.entityType().ifPresent(e -> tag(SenseEffect.getTargetTag(SenseEffect.SenseType.NEPTUNE, SenseEffect.GlowColor.YELLOW)).add(e));
            }

        }

        FishRegistry.fishEntities.forEach(ro -> {
        });
    }

    private void addByList(TagKey<EntityType<?>> tag, List<String> names) {
        FishRegistry.fishEntities.forEach(holder -> {
            assert holder.getKey() != null;
            final ResourceLocation key = holder.getKey().location();
            if ("aquaculture".equals(key.getNamespace())) {
                final String path = key.getPath();
                if (names.contains(path)) {
                    tag(tag).add(holder.get());
                }
            }
        });
    }

    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();
        generator.addProvider(event.includeServer(), new SAquaEntityTypeTags(output, lookupProvider, helper));
    }
}
