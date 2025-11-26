package com.vomiter.survivorsaquaculture;

import com.mojang.logging.LogUtils;
import com.teammetallurgy.aquaculture.client.renderer.entity.FishMountRenderer;
import com.teammetallurgy.aquaculture.entity.FishMountEntity;
import com.vomiter.survivorsaquaculture.client.ClientForgeEventHandler;
import com.vomiter.survivorsaquaculture.core.registry.SAquaBlocks;
import com.vomiter.survivorsaquaculture.core.registry.SAquaFishMount;
import com.vomiter.survivorsaquaculture.core.registry.SAquaFluids;
import com.vomiter.survivorsaquaculture.core.registry.SAquaItems;
import com.vomiter.survivorsaquaculture.data.SAquaRecipeProvider;
import com.vomiter.survivorsaquaculture.data.asset.SAquaFishMountModel;
import com.vomiter.survivorsaquaculture.data.tag.SAquaEntityTypeTags;
import com.vomiter.survivorsaquaculture.data.tag.SAquaItemTags;
import com.vomiter.survivorsaquaculture.core.fish.size.RenderFishSize;
import com.vomiter.survivorsaquaculture.data.asset.SAquaSimpleMetalBlockStates;
import com.vomiter.survivorsaquaculture.data.book.content.BookEN;
import com.vomiter.survivorsaquaculture.data.loot.SAquaCropLootModifierProvider;
import com.vomiter.survivorsaquaculture.data.loot.SAquaFilletLootTableProvider;
import com.vomiter.survivorsaquaculture.data.loot.SAquaLootModifiers;
import com.vomiter.survivorsaquaculture.data.size.SAquaSizeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SurvivorsAquaculture.MODID)
public class SurvivorsAquaculture
{
    //TODO: FISH MOUNT -> cancel name tag drawing, use overlay

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
        SAquaFishMount.register(modBus);
        SAquaLootModifiers.LOOT_MODIFIERS.register(modBus);

        modBus.addListener(SAquaCreativeTabCompat::onBuildCreativeTab);
        modBus.addListener(this::onGatherData);
        modBus.addListener(SAquaEntityTypeTags::gatherData);
        modBus.addListener(SAquaItemTags::gatherData);
        modBus.addListener(SAquaSimpleMetalBlockStates::gatherData);
        modBus.addListener(SAquaCropLootModifierProvider::gatherData);
        modBus.addListener(SAquaFilletLootTableProvider::gatherData);
        modBus.addListener(SAquaSizeProvider::gatherData);
        modBus.addListener(SAquaFishMountModel::onGatherData);

        modBus.addListener(BookEN::generate);
        if(FMLEnvironment.dist.isClient()){
            ClientForgeEventHandler.init();
            modBus.addListener(this::registerEntityRenders);
            MinecraftForge.EVENT_BUS.addListener(RenderFishSize::onRenderLivingPre);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    private void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        for (RegistryObject<EntityType<FishMountEntity>> fishMount : SAquaFishMount.TFC_FISH_MOUNT_ENTITIES.values()) {
            event.registerEntityRenderer(fishMount.get(), FishMountRenderer::new);
        }

    }

    private void onGatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        PackOutput packOutput = generator.getPackOutput();
        generator.addProvider(event.includeServer(), new SAquaRecipeProvider(packOutput));
    }

}
