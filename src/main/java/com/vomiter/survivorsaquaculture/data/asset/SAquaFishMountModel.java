package com.vomiter.survivorsaquaculture.data.asset;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.vomiter.survivorsaquaculture.SurvivorsAquaculture;
import com.vomiter.survivorsaquaculture.core.registry.SAquaFishMount;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

public class SAquaFishMountModel extends BlockStateProvider {
    ExistingFileHelper helper;
    public SAquaFishMountModel(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SurvivorsAquaculture.MODID, existingFileHelper);
        helper = existingFileHelper;
    }

    @Override
    public @NotNull String getName(){
        return "Fish Mount Models";
    }

    public static void onGatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        generator.addProvider(event.includeClient(), new SAquaFishMountModel(generator.getPackOutput(), event.getExistingFileHelper()));
    }

    @Override
    protected void registerStatesAndModels() {
        helper.trackGenerated(new ResourceLocation(Aquaculture.MOD_ID, "block/fish_mount"), PackType.CLIENT_RESOURCES, ".json", "models");
        SAquaFishMount.TFC_FISH_MOUNT_ITEMS.forEach((wood, itemRo) -> {
            assert itemRo.getId() != null;
            helper.trackGenerated(new ResourceLocation(TerraFirmaCraft.MOD_ID, "block/" + Wood.BlockType.PLANKS.nameFor(wood)), PackType.CLIENT_RESOURCES, ".png", "textures");
            ModelFile model = models().withExistingParent("block/" + itemRo.getId().getPath(), new ResourceLocation(Aquaculture.MOD_ID, "block/fish_mount")).texture("wood", new ResourceLocation(TerraFirmaCraft.MOD_ID, "block/" + Wood.BlockType.PLANKS.nameFor(wood)));
            simpleBlock(SAquaFishMount.TFC_FISH_MOUNT_BLOCKS.get(wood).get(), model);
            itemModels().withExistingParent("item/" + itemRo.getId().getPath(), new ResourceLocation(SurvivorsAquaculture.MODID, "block/" + itemRo.getId().getPath()));
        });

    }

}
