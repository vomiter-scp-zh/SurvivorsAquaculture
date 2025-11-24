package com.vomiter.survivorsaquaculture.data.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teammetallurgy.aquaculture.init.AquaItems;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class WormLootModifier extends LootModifier {

    public static final Codec<WormLootModifier> CODEC = RecordCodecBuilder.create(inst ->
            codecStart(inst)
                    .and(Codec.INT.fieldOf("min").forGetter(m -> m.min))
                    .and(Codec.INT.fieldOf("max").forGetter(m -> m.max))
                    .apply(inst, WormLootModifier::new)
    );

    private final int min;
    private final int max;

    protected WormLootModifier(LootItemCondition[] conditions, int min, int max) {
        super(conditions);
        this.min = min;
        this.max = max;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        int count = Mth.nextInt(context.getRandom(), min, max);
        if (count > 0) {
            generatedLoot.add(new ItemStack(AquaItems.WORM.get(), count));
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

}
