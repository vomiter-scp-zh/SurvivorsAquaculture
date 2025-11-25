package com.vomiter.survivorsaquaculture.data.loot;

import com.vomiter.survivorsaquaculture.SurvivorsAquaculture;
import com.vomiter.survivorsaquaculture.core.fish.AquaFishes;
import com.teammetallurgy.aquaculture.init.AquaItems;
import com.vomiter.survivorsaquaculture.core.registry.SAquaItems;
import net.dries007.tfc.common.blocks.GroundcoverBlockType;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.plant.Plant;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class SAquaFilletLootTableProvider extends LootTableProvider {

    public static void gatherData(GatherDataEvent event) {
        PackOutput output = event.getGenerator().getPackOutput();
        event.getGenerator().addProvider(
                event.includeServer(),
                new SAquaFilletLootTableProvider(output)
        );
    }

    public SAquaFilletLootTableProvider(PackOutput output) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(SAFilletSubProvider::new, LootContextParamSets.EMPTY)
        ));
    }

    // ========================================================================
    // 真正在生 loot table 的地方
    // ========================================================================

    public static class SAFilletSubProvider implements LootTableSubProvider {

        private static final ResourceLocation TREASURE_TABLE =
                new ResourceLocation(SurvivorsAquaculture.MODID, "fillet/common_treasure");

        private static final ResourceLocation JUNK_TABLE =
                new ResourceLocation(SurvivorsAquaculture.MODID, "fillet/common_junk");


        @Override
        public void generate(BiConsumer<ResourceLocation, LootTable.Builder> out) {
            // 先生共用寶物表
            out.accept(TREASURE_TABLE, createCommonTreasureTable());
            out.accept(JUNK_TABLE, createCommonJunkTable());

            // 針對每一種 AquaFishes 生成對應 loot table
            for (AquaFishes fish : AquaFishes.values()) {
                int fillets = fish.getFilletAmount();
                if (fillets <= 0) continue; // 沒片數就略過

                ResourceLocation fishId = fish.id(); // aquaculture:<fish_name>

                ResourceLocation tableId = new ResourceLocation(
                        SurvivorsAquaculture.MODID,
                        "fillet/" + fishId.getNamespace() + "/" + fishId.getPath() // e.g. atlantic_halibut
                );

                out.accept(tableId, createFishFilletTable(fish, fillets));
            }
        }

        private static LootTable.Builder createCommonTreasureTable() {
            LootPool.Builder pool = LootPool.lootPool();
            pool.setRolls(ConstantValue.exactly(1f));
            for (Ore ore: Ore.values()) {
                if(ore.isGraded()) pool.add(LootItem.lootTableItem(TFCItems.GRADED_ORES.get(ore).get(Ore.Grade.NORMAL).get()));
                else pool.add(LootItem.lootTableItem(TFCItems.ORES.get(ore).get()));
            }

            return LootTable.lootTable().withPool(pool);
        }

        private static LootTable.Builder createCommonJunkTable() {
            LootPool.Builder pool = LootPool.lootPool();
            pool.setRolls(ConstantValue.exactly(1f));
            for (Ore ore: Ore.values()) {
                if(ore.isGraded()) pool.add(LootItem.lootTableItem(TFCItems.GRADED_ORES.get(ore).get(Ore.Grade.POOR).get()));
            }

            pool.add(LootItem.lootTableItem(AquaItems.TIN_CAN.get()));
            pool.add(LootItem.lootTableItem(AquaItems.NESSAGE_IN_A_BOTTLE.get()));
            pool.add(LootItem.lootTableItem(TFCBlocks.PLANTS.get(Plant.GIANT_KELP_FLOWER).get()));
            pool.add(LootItem.lootTableItem(TFCBlocks.GROUNDCOVER.get(GroundcoverBlockType.DRIFTWOOD).get()));
            return LootTable.lootTable().withPool(pool);
        }


        private static LootTable.Builder createFishFilletTable(AquaFishes fish, int fillets) {
            LootTable.Builder table = LootTable.lootTable();

            // ====== 基本魚骨 ======
            if (fillets < 7) {
                // fillet < 7：1~2 魚骨
                table.withPool(
                        LootPool.lootPool()
                                .setRolls(UniformGenerator.between(0.0F, 2.0F))
                                .add(LootItem.lootTableItem(AquaItems.FISH_BONES.get()))
                );
            } else {
                // fillet >= 7：2~4 魚骨
                table.withPool(
                        LootPool.lootPool()
                                .setRolls(UniformGenerator.between(2.0F, 4.0F))
                                .add(LootItem.lootTableItem(AquaItems.FISH_BONES.get()))
                );

                // ====== 珍珠／寶物 ======
                // 30 - count / 30 的機率給一個珍珠
                float pearlChance = fillets / 30.0f;
                if (pearlChance < 0f) pearlChance = 0f;
                if (pearlChance > 1f) pearlChance = 1f;

                table.withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(AlternativesEntry.alternatives(
                                        LootItem.lootTableItem(SAquaItems.NEPTUNIAN_PEARL.get())
                                                .when(LootItemRandomChanceCondition.randomChance(pearlChance)),
                                        LootTableReference.lootTableReference(JUNK_TABLE).when(LootItemRandomChanceCondition.randomChance(0.7f)),
                                        LootTableReference.lootTableReference(TREASURE_TABLE)
                                ))
                );
            }

            return table;
        }
    }
}
