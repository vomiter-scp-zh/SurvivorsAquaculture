package com.vomiter.survivorsaquaculture.core.fish;

import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.init.AquaItems;
import com.teammetallurgy.aquaculture.item.FishItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;

public enum AquaFishes {
    ATLANTIC_COD(AquaItems.ATLANTIC_COD),
    BLACKFISH(AquaItems.BLACKFISH),
    PACIFIC_HALIBUT(AquaItems.PACIFIC_HALIBUT),
    ATLANTIC_HALIBUT(AquaItems.ATLANTIC_HALIBUT),
    ATLANTIC_HERRING(AquaItems.ATLANTIC_HERRING),
    PINK_SALMON(AquaItems.PINK_SALMON),
    POLLOCK(AquaItems.POLLOCK),
    RAINBOW_TROUT(AquaItems.RAINBOW_TROUT),
    BAYAD(AquaItems.BAYAD),
    BOULTI(AquaItems.BOULTI),
    CAPITAINE(AquaItems.CAPITAINE),
    SYNODONTIS(AquaItems.SYNODONTIS),
    SMALLMOUTH_BASS(AquaItems.SMALLMOUTH_BASS),
    BLUEGILL(AquaItems.BLUEGILL),
    BROWN_TROUT(AquaItems.BROWN_TROUT),
    CARP(AquaItems.CARP),
    CATFISH(AquaItems.CATFISH),
    GAR(AquaItems.GAR),
    MINNOW(AquaItems.MINNOW),
    MUSKELLUNGE(AquaItems.MUSKELLUNGE),
    PERCH(AquaItems.PERCH),
    ARAPAIMA(AquaItems.ARAPAIMA),
    PIRANHA(AquaItems.PIRANHA),
    TAMBAQUI(AquaItems.TAMBAQUI),
    BROWN_SHROOMA(AquaItems.BROWN_SHROOMA),
    RED_SHROOMA(AquaItems.RED_SHROOMA),
    JELLYFISH(AquaItems.JELLYFISH),
    RED_GROUPER(AquaItems.RED_GROUPER),
    TUNA(AquaItems.TUNA),

    GOLDFISH(AquaItems.GOLDFISH),
    BOX_TURTLE(AquaItems.BOX_TURTLE),
    ARRAU_TURTLE(AquaItems.ARRAU_TURTLE),
    STARSHELL_TURTLE(AquaItems.STARSHELL_TURTLE);

    public final RegistryObject<Item> itemRO;

    AquaFishes(RegistryObject<Item> fishItem) {
        this.itemRO = fishItem;
    }

    public Item item() {
        return itemRO.get();
    }

    public ResourceLocation id() {
        var id = ForgeRegistries.ITEMS.getKey(item());
        return Objects.requireNonNull(id, "Item not registered yet for " + name());
    }

    public Optional<EntityType<?>> entityType() {
        EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(id());
        return Optional.ofNullable(type);
    }

    public boolean hasEntity() {
        return entityType().isPresent();
    }

    private static final Map<Item, AquaFishes> BY_ITEM = new IdentityHashMap<>();
    private static final Map<EntityType<?>, AquaFishes> BY_TYPE = new IdentityHashMap<>();
    private static void buildIndex() {
        BY_ITEM.clear();
        BY_TYPE.clear();
        for (AquaFishes f : values()) {
            BY_ITEM.put(f.item(), f);
            f.entityType().ifPresent(type -> BY_TYPE.put(type, f));
        }
    }
    static {
        buildIndex();
    }

    public static Optional<AquaFishes> byItem(Item item) {
        return Optional.ofNullable(BY_ITEM.get(item));
    }

    public static Optional<AquaFishes> byEntityType(EntityType<?> type){
        return Optional.ofNullable(BY_TYPE.get(type));
    }

    public int getFilletAmount(){
        if(this.item() instanceof FishItem fishItem) return AquacultureAPI.FISH_DATA.getFilletAmount(fishItem);
        return 0;
    }

    public int getScale(){
        return Math.max(1, getFilletAmount());
    }

    public float getSizeScale(){
        return (float) Math.sqrt(getScale());
    }
}
