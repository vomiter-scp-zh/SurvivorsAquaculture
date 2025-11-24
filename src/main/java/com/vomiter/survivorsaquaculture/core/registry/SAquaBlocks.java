package com.vomiter.survivorsaquaculture.core.registry;

import com.vomiter.survivorsaquaculture.SurvivorsAquaculture;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class SAquaBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SurvivorsAquaculture.MODID);
    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SurvivorsAquaculture.MODID);
    public static void register(IEventBus modBus){
        BLOCKS.register(modBus);
        BLOCK_ITEMS.register(modBus);
    }

    public static final Map<TFCMetalNeptunium, Map<Metal.BlockType, RegistryObject<Block>>> METAL = new HashMap<>();
    public static final Map<TFCMetalNeptunium, RegistryObject<LiquidBlock>> METAL_FLUIDS = Helpers.mapOfKeys(TFCMetalNeptunium.class, metal ->
            registerNoItem("fluid/metal/" + metal.name(), () -> new LiquidBlock(SAquaFluids.METAL.get(metal).source(), BlockBehaviour.Properties.copy(Blocks.LAVA).noLootTable()))
    );

    static {
        for (TFCMetalNeptunium metal : TFCMetalNeptunium.values()) {
            Map<Metal.BlockType, RegistryObject<Block>> map = new HashMap<>();
            for (Metal.BlockType blockType : Metal.BlockType.values()) {
                if(blockType.has(Metal.Default.BISMUTH)) {
                    var block = BLOCKS.register(blockType.createName(metal), blockType.create(metal));
                    BLOCK_ITEMS.register(
                            blockType.createName(metal),
                            () -> blockType.createBlockItem(new Item.Properties()).apply(block.get())
                    );
                    map.put(blockType, block);
                }
            }
            METAL.put(metal, map);
        }
    }

    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> blockSupplier)
    {
        return register(name, blockSupplier, (Function<T, ? extends BlockItem>) null);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, Item.Properties blockItemProperties)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, blockItemProperties));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory)
    {
        return RegistrationHelpers.registerBlock(BLOCKS, BLOCK_ITEMS, name, blockSupplier, blockItemFactory);
    }

}
