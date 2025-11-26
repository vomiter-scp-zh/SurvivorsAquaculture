package com.vomiter.survivorsaquaculture.core.registry;

import com.teammetallurgy.aquaculture.entity.FishMountEntity;
import com.teammetallurgy.aquaculture.item.FishMountItem;
import com.vomiter.survivorsaquaculture.SurvivorsAquaculture;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumMap;
import java.util.Map;

public class SAquaFishMount {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SurvivorsAquaculture.MODID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SurvivorsAquaculture.MODID);
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, SurvivorsAquaculture.MODID);


    public static final Map<Wood, RegistryObject<Block>> TFC_FISH_MOUNT_BLOCKS = new EnumMap<>(Wood.class);
    public static Map<Wood, RegistryObject<EntityType<FishMountEntity>>> TFC_FISH_MOUNT_ENTITIES = new EnumMap<>(Wood.class);
    public static Map<Wood, RegistryObject<Item>> TFC_FISH_MOUNT_ITEMS = new EnumMap<>(Wood.class);
    public static void register(IEventBus modBus){
        ENTITIES.register(modBus);
        ITEMS.register(modBus);
        BLOCKS.register(modBus);
    }
    static {
        for (Wood wood : Wood.values()) {
            String name = "fish_mount/" + wood.getSerializedName();
            var fishMount = ENTITIES.register(
                    name,
                    () -> EntityType.Builder.<FishMountEntity>of(FishMountEntity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .setCustomClientFactory(FishMountEntity::new)
                            .build(SurvivorsAquaculture.MODID + ":" + name));

            TFC_FISH_MOUNT_ENTITIES.put(wood, fishMount);
            TFC_FISH_MOUNT_ITEMS.put(wood, ITEMS.register(name, ()-> new FishMountItem(fishMount)));
            TFC_FISH_MOUNT_BLOCKS.put(wood, BLOCKS.register(name, ()->new Block(BlockBehaviour.Properties.of())));
        }
    }

}
