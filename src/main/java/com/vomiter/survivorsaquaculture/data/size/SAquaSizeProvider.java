package com.vomiter.survivorsaquaculture.data.size;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vomiter.survivorsaquaculture.SurvivorsAquaculture;
import net.dries007.tfc.common.capabilities.size.Size;
import net.dries007.tfc.common.capabilities.size.Weight;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * 輸出：data/<modid>/tfc/item_sizes/<namespace>/<path>.json
 */
public class SAquaSizeProvider implements DataProvider {
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        var provider = new SAquaSizeProvider(generator.getPackOutput(), SurvivorsAquaculture.MODID);
        generator.addProvider(event.includeServer(), provider);
    }

    private static final String FOLDER = "tfc/item_sizes";

    private final PackOutput output;
    private final String modid;
    private final Map<ResourceLocation, JsonObject> entries = new LinkedHashMap<>();

    public SAquaSizeProvider(PackOutput output, String modid) {
        this.output = output;
        this.modid = modid;
        new SAquaSize(this).save();
    }

    public Builder newEntry(ResourceLocation itemId) {
        return new Builder(itemId);
    }

    public void putRaw(ResourceLocation itemId, JsonObject json) {
        entries.put(itemId, json);
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cachedOutput) {
        final PackOutput.PathProvider pathProvider =
                output.createPathProvider(PackOutput.Target.DATA_PACK, FOLDER);

        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (Map.Entry<ResourceLocation, JsonObject> e : entries.entrySet()) {
            Path path = pathProvider.json(e.getKey());
            futures.add(DataProvider.saveStable(cachedOutput, e.getValue(), path));
        }
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "TFC Item Size Data: " + modid;
    }

    // ---------- Builder ----------

    public class Builder {
        private final ResourceLocation id;
        private JsonElement ingredientJson;
        private Size size;
        private Weight weight;
        private Integer maxStackSize;
        private final List<Consumer<JsonObject>> extras = new ArrayList<>();

        private Builder(ResourceLocation id) { this.id = id; }

        public Builder ingredient(Ingredient ing){
            this.ingredientJson = ing.toJson(); return this;
        }
        public Builder ingredient(ItemLike item){
            this.ingredientJson = Ingredient.of(item).toJson(); return this;
        }
        public Builder ingredient(ItemLike... items){
            this.ingredientJson = Ingredient.of(items).toJson(); return this;
        }
        public Builder ingredient(ItemStack stack){
            this.ingredientJson = Ingredient.of(stack).toJson(); return this;
        }
        public Builder ingredient(TagKey<Item> tag){
            this.ingredientJson = Ingredient.of(tag).toJson(); return this;
        }
        public Builder ingredientJson(JsonElement json){
            this.ingredientJson = json; return this;
        }

        public Builder size(Size size) {
            this.size = size; return this;
        }

        public Builder weight(Weight weight) {
            this.weight = weight; return this;
        }

        public void save() {
            entries.put(id, build());
        }

        private JsonObject build() {
            JsonObject json = new JsonObject();
            if (ingredientJson == null) {
                throw new IllegalStateException("Missing required 'ingredient' for " + id);
            }
            json.add("ingredient", ingredientJson);

            if (size != null) {
                json.addProperty("size", size.name);
            }
            if (weight != null) {
                json.addProperty("weight", weight.name);
            }
            return json;
        }
    }
}