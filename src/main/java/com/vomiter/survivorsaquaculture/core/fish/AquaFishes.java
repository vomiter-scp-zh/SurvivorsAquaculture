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
    ATLANTIC_COD(AquaItems.ATLANTIC_COD, 6),
    BLACKFISH(AquaItems.BLACKFISH, 2),
    PACIFIC_HALIBUT(AquaItems.PACIFIC_HALIBUT, 12),
    ATLANTIC_HALIBUT(AquaItems.ATLANTIC_HALIBUT, 14),
    ATLANTIC_HERRING(AquaItems.ATLANTIC_HERRING, 1),
    PINK_SALMON(AquaItems.PINK_SALMON, 2),
    POLLOCK(AquaItems.POLLOCK, 2),
    RAINBOW_TROUT(AquaItems.RAINBOW_TROUT, 2),
    BAYAD(AquaItems.BAYAD, 4),
    BOULTI(AquaItems.BOULTI, 1),
    CAPITAINE(AquaItems.CAPITAINE, 10),
    SYNODONTIS(AquaItems.SYNODONTIS, 1),
    SMALLMOUTH_BASS(AquaItems.SMALLMOUTH_BASS, 2),
    BLUEGILL(AquaItems.BLUEGILL, 1),
    BROWN_TROUT(AquaItems.BROWN_TROUT, 2),
    CARP(AquaItems.CARP, 2),
    CATFISH(AquaItems.CATFISH, 6),
    GAR(AquaItems.GAR, 4),
    MINNOW(AquaItems.MINNOW, 0),
    MUSKELLUNGE(AquaItems.MUSKELLUNGE, 3),
    PERCH(AquaItems.PERCH, 1),
    ARAPAIMA(AquaItems.ARAPAIMA, 10),
    PIRANHA(AquaItems.PIRANHA, 1),
    TAMBAQUI(AquaItems.TAMBAQUI, 3),
    BROWN_SHROOMA(AquaItems.BROWN_SHROOMA, 0),
    RED_SHROOMA(AquaItems.RED_SHROOMA, 0),
    JELLYFISH(AquaItems.JELLYFISH, 0),
    RED_GROUPER(AquaItems.RED_GROUPER, 3),
    TUNA(AquaItems.TUNA, 10),
    GOLDFISH(AquaItems.GOLDFISH, 0),

    // 烏龜在原本 FISH_DATA 裡沒有 entry，就給 0 當預設
    BOX_TURTLE(AquaItems.BOX_TURTLE),
    ARRAU_TURTLE(AquaItems.ARRAU_TURTLE),
    STARSHELL_TURTLE(AquaItems.STARSHELL_TURTLE);

    public final RegistryObject<Item> itemRO;
    private final int filletAmount;

    // 預設建構子：沒有特別指定 fillet 的一律 0
    AquaFishes(RegistryObject<Item> fishItem) {
        this(fishItem, 0);
    }

    AquaFishes(RegistryObject<Item> fishItem, int filletAmount) {
        this.itemRO = fishItem;
        this.filletAmount = filletAmount;
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

    public int getFilletAmount() {
        return filletAmount;
    }

    public static int getFilletAmount(Item item) {
        return byItem(item)
                .map(AquaFishes::getFilletAmount)
                .orElse(0);
    }

    public static int getFilletAmount(EntityType<?> type) {
        return byEntityType(type)
                .map(AquaFishes::getFilletAmount)
                .orElse(0);
    }

    public int getAPIFilletAmount(){
        if(this.item() instanceof FishItem fishItem) return AquacultureAPI.FISH_DATA.getFilletAmount(fishItem);
        return 0;
    }

    public int getScale(){
        return Math.max(1, getAPIFilletAmount());
    }

    public float getSizeScale(){
        return (float) Math.sqrt(getScale());
    }
}
