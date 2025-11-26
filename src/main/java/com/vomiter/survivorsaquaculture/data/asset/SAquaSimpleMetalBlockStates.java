package com.vomiter.survivorsaquaculture.data.asset;

import com.vomiter.survivorsaquaculture.SurvivorsAquaculture;
import com.vomiter.survivorsaquaculture.core.registry.SAquaBlocks;
import com.vomiter.survivorsaquaculture.core.registry.TFCMetalNeptunium;
import net.dries007.tfc.util.Metal;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class SAquaSimpleMetalBlockStates extends BlockStateProvider {

    public SAquaSimpleMetalBlockStates(PackOutput output, ExistingFileHelper efh) {
        super(output, SurvivorsAquaculture.MODID, efh);
    }

    @Override
    protected void registerStatesAndModels() {
        SAquaBlocks.METAL.forEach(
                (metal, typeMap)
                        -> typeMap.forEach((blockType, regObj) -> {
            if (blockType != Metal.BlockType.BLOCK
                    && blockType != Metal.BlockType.BLOCK_SLAB
                    && blockType != Metal.BlockType.BLOCK_STAIRS) return;

            Block block = regObj.get();
            String name = key(block);
            ResourceLocation fullTex = tex("block", metal);

            switch (blockType) {
                case BLOCK -> genFullBlock(name, block, fullTex);
                case BLOCK_SLAB -> genSlab(name, (SlabBlock) block, fullTex);
                case BLOCK_STAIRS -> genStairs(name, (StairBlock) block, fullTex);
                default -> {}
            }
        }));
    }

    /* ---------- helpers ---------- */

    private String bm(String modelName) {
        return modelName.startsWith("block/") ? modelName : "block/" + modelName;
    }

    private String key(Block b) {
        var id = ForgeRegistries.BLOCKS.getKey(b);
        return id == null ? "unknown" : id.getPath();
    }

    private ResourceLocation tex(String part, TFCMetalNeptunium metalEnum) {
        String metal = metalEnum.getSerializedName();
        return modLoc("block/metal/" + part + "/" + metal);
    }

    private void writeItemModel(Block block) {
        var id = ForgeRegistries.BLOCKS.getKey(block);
        String itemName = id.getPath();
        itemModels().withExistingParent("item/" + itemName, modLoc("block/" + itemName));
    }

    private void genFullBlock(String name, Block block, ResourceLocation texture) {
        ModelFile model = models().cubeAll(bm(name), texture);
        simpleBlock(block, model);
        writeItemModel(block);
    }

    private void genSlab(String name, SlabBlock block, ResourceLocation fullTex) {
        ModelFile slab    = models().slab(bm(name), fullTex, fullTex, fullTex);
        ModelFile slabTop = models().slabTop(bm(name) + "_top", fullTex, fullTex, fullTex);
        String fullName = baseNameFromSlab(bm(name));
        ModelFile full = models().cubeAll(fullName, fullTex);

        slabBlock(block, slab, slabTop, full);
        writeItemModel(block);
    }

    private void genStairs(String name, StairBlock block, ResourceLocation fullTex) {
        ModelFile stairs = models().stairs(bm(name), fullTex, fullTex, fullTex);
        ModelFile inner = models().stairsInner(bm(name) + "_inner", fullTex, fullTex, fullTex);
        ModelFile outer = models().stairsOuter(bm(name) + "_outer", fullTex, fullTex, fullTex);

        stairsBlock(block, stairs, inner, outer);
        writeItemModel(block);
    }

    private String baseNameFromSlab(String slabName) {
        return slabName.endsWith("_slab") ? slabName.substring(0, slabName.length() - 5) : slabName;
    }

    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        generator.addProvider(true, new SAquaSimpleMetalBlockStates(output, helper));
    }

}
