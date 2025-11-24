package com.vomiter.survivorsaquaculture.core.registry;

import com.vomiter.survivorsaquaculture.SurvivorsAquaculture;
import net.dries007.tfc.util.Helpers;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

public class SAquaItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SurvivorsAquaculture.MODID);

    public static final RegistryObject<Item> NEPTUNIAN_PEARL =
            ITEMS.register("neptunian_pearl", () -> new Item(new Item.Properties()));

    public static final Map<TFCMetalNeptunium, RegistryObject<BucketItem>> METAL_FLUID_BUCKETS = Helpers.mapOfKeys(TFCMetalNeptunium.class, metal ->
            register("bucket/metal/" + metal.name(), () -> new BucketItem(SAquaFluids.METAL.get(metal).source(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)))
    );

    public static final Map<TFCMetalNeptunium, Map<TFCMetalNeptunium.ItemType, RegistryObject<Item>>> METAL_ITEMS = new EnumMap<>(TFCMetalNeptunium.class);
    public static final Map<TFCMetalNeptunium, Map<TFCMetalNeptunium.UnfinishedArmorType, RegistryObject<Item>>> UNFINISHED_ARMORS = new EnumMap<>(TFCMetalNeptunium.class);
    static {
        for (TFCMetalNeptunium metal : TFCMetalNeptunium.values()) {
            Map<TFCMetalNeptunium.ItemType, RegistryObject<Item>> map = new EnumMap<>(TFCMetalNeptunium.ItemType.class);
            for (TFCMetalNeptunium.ItemType type : TFCMetalNeptunium.ItemType.values()) {
                map.put(type, ITEMS.register("metal/" + type.name().toLowerCase() + "/" + metal.getSerializedName(), () -> type.create(metal)));
            }
            METAL_ITEMS.put(metal, map);

            if(metal.equals(TFCMetalNeptunium.NEPTUNIAN_STEEL)){
                Map<TFCMetalNeptunium.UnfinishedArmorType, RegistryObject<Item>> map2 = new EnumMap<>(TFCMetalNeptunium.UnfinishedArmorType.class);
                for (TFCMetalNeptunium.UnfinishedArmorType type : TFCMetalNeptunium.UnfinishedArmorType.values()) {
                    map2.put(type, ITEMS.register("metal/unfinished/neptunium_" + type.name().toLowerCase(Locale.ROOT), () -> type.create(metal)));
                }
                UNFINISHED_ARMORS.put(metal, map2);
            }
        }
    }

    private static RegistryObject<Item> register(String name)
    {
        return register(name, () -> new Item(new Item.Properties()));
    }

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item)
    {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), item);
    }
}
