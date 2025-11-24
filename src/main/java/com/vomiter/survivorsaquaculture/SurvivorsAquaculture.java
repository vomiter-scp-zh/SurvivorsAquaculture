package com.vomiter.survivorsaquaculture;

import com.mojang.logging.LogUtils;
import com.vomiter.survivorsaquaculture.core.registry.SAquaBlocks;
import com.vomiter.survivorsaquaculture.core.registry.SAquaFluids;
import com.vomiter.survivorsaquaculture.core.registry.SAquaItems;
import com.vomiter.survivorsaquaculture.data.SAquaEntityTypeTags;
import com.vomiter.survivorsaquaculture.data.SAquaItemTags;
import com.vomiter.survivorsaquaculture.core.fish.size.RenderFishSize;
import com.vomiter.survivorsaquaculture.data.SAquaSimpleMetalBlockStates;
import com.vomiter.survivorsaquaculture.data.book.content.BookEN;
import com.vomiter.survivorsaquaculture.data.loot.ModLootModifierProvider;
import com.vomiter.survivorsaquaculture.data.loot.ModLootModifiers;
import net.dries007.tfc.util.Metal;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SurvivorsAquaculture.MODID)
public class SurvivorsAquaculture
{

    //TODO: change stack of fishes
    //TODO: add salt water to effect criteria
    //TODO: add aqua fish in neptune vision highlight
    //TODO: add looting for skillet processing
    //TODO: make bottle letter give back a bottle and a paper in certain chance

    // Define mod id in a common place for everything to reference
    public static final String MODID = "survivorsaquaculture";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static Logger getLogger(){return LOGGER;}

    public SurvivorsAquaculture(FMLJavaModLoadingContext context)
    {
        IEventBus modBus = context.getModEventBus();
        SAquaItems.ITEMS.register(modBus);
        SAquaFluids.FLUID_TYPES.register(modBus);
        SAquaFluids.FLUIDS.register(modBus);
        SAquaBlocks.register(modBus);
        ModLootModifiers.LOOT_MODIFIERS.register(modBus);

        modBus.addListener(SAquaCreativeTabCompat::onBuildCreativeTab);
        modBus.addListener(SAquaEntityTypeTags::gatherData);
        modBus.addListener(SAquaItemTags::gatherData);
        modBus.addListener(SAquaSimpleMetalBlockStates::gatherData);
        modBus.addListener(ModLootModifierProvider::gatherData);
        modBus.addListener(BookEN::generate);
        if(FMLEnvironment.dist.isClient()){
            MinecraftForge.EVENT_BUS.addListener(RenderFishSize::onRenderLivingPre);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

}
