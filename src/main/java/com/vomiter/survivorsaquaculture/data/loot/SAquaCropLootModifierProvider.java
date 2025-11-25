package com.vomiter.survivorsaquaculture.data.loot;

import com.vomiter.survivorsaquaculture.SurvivorsAquaculture;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class SAquaCropLootModifierProvider extends GlobalLootModifierProvider {
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        generator.addProvider(event.includeServer(), new SAquaCropLootModifierProvider(output));
    }


    public SAquaCropLootModifierProvider(PackOutput output) {
        super(output, SurvivorsAquaculture.MODID);
    }

    @Override
    protected void start() {
        // 共用條件：使用鋤頭
        LootItemCondition.Builder hoeCond =
                MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.HOES));

        // 共用條件：70% 機率
        LootItemCondition.Builder chanceCond =
                LootItemRandomChanceCondition.randomChance(0.7f);

        // --- TFC CROPS ---
        for (Supplier<? extends Block> sup : TFCBlocks.CROPS.values()) {
            Block block = sup.get();
            addForBlock(block, "crop", hoeCond, chanceCond);
        }

        // --- TFC DEAD_CROPS ---
        for (Supplier<? extends Block> sup : TFCBlocks.DEAD_CROPS.values()) {
            Block block = sup.get();
            addForBlock(block, "dead_crop", hoeCond, chanceCond);
        }
    }

    private void addForBlock(Block block, String suffix,
                             LootItemCondition.Builder hoeCond,
                             LootItemCondition.Builder chanceCond) {
        var key = ForgeRegistries.BLOCKS.getKey(block);
        if (key == null) return;

        // 從 block 的 state 裡找名稱叫 "age" 的 IntegerProperty
        IntegerProperty ageProp = null;
        for (Property<?> prop : block.defaultBlockState().getProperties()) {
            if (prop instanceof IntegerProperty ip && "age".equals(prop.getName())) {
                ageProp = ip;
                break;
            }
        }

        // 沒有 age 屬性就不要加「成熟」條件（也可以選擇直接 return）
        LootItemCondition.Builder blockCond;
        if (ageProp != null) {
            int maxAge = ageProp.getPossibleValues().stream()
                    .max(Integer::compareTo)
                    .orElse(0);

            blockCond = LootItemBlockStatePropertyCondition
                    .hasBlockStateProperties(block)
                    .setProperties(
                            StatePropertiesPredicate.Builder.properties()
                                    .hasProperty(ageProp, maxAge) // 只在 age == max 的時候觸發
                    );
        } else {
            // fallback：只檢查方塊類型，不管成熟度
            blockCond = LootItemBlockStatePropertyCondition
                    .hasBlockStateProperties(block);
        }

        // JSON 檔名：例如 "barley_crop_worm"
        String name = key.getPath() + "_" + suffix + "_worm";

        add(name,
                new WormLootModifier(
                        new LootItemCondition[]{
                                blockCond.build(),    // 這個方塊 +（如果有）age==max
                                hoeCond.build(),      // 用鋤頭
                                chanceCond.build()    // 70% 機率
                        },
                        1,
                        3
                )
        );
    }
}
