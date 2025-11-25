package com.vomiter.survivorsaquaculture.mixin.recipe;

import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.init.AquaItems;
import com.vomiter.survivorsaquaculture.SurvivorsAquaculture;
import com.vomiter.survivorsaquaculture.core.registry.SAquaItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResultSlot.class)
public abstract class ResultSlot_FilletBonusMixin {

    @Shadow @Final
    private CraftingContainer craftSlots;

    @Shadow @Final
    private Player player;

    @Inject(method = "onTake", at = @At("TAIL"))
    private void giveBonus(Player p_150638_, ItemStack crafted, CallbackInfo ci) {
        if (player.level().isClientSide) return;

        // 1. 找出有 fillet 資料的魚
        ItemStack fish = ItemStack.EMPTY;
        for (int i = 0; i < craftSlots.getContainerSize(); ++i) {
            ItemStack slot = craftSlots.getItem(i);
            if (!slot.isEmpty() && AquacultureAPI.FISH_DATA.hasFilletAmount(slot.getItem())) {
                fish = slot.copy();
                break;
            }
        }
        if (fish.isEmpty()) return;

        // 2. 依魚種取得 fillet 數量
        int filletCount = AquacultureAPI.FISH_DATA.getFilletAmount(fish.getItem());
        if (filletCount <= 0) return;

        // 3. roll 次數 = floor(filletCount / 4)
        int rolls = filletCount / 4;
        if (rolls <= 0) return;

        // 4. 依魚的 registry name 決定 loot table
        var fishId = ForgeRegistries.ITEMS.getKey(fish.getItem());
        if (fishId == null) return;

        ResourceLocation lootId = new ResourceLocation(
                "survivorsaquaculture",
                "fillet/" + fishId.getNamespace() + "/" + fishId.getPath()
        );

        if (!(player instanceof ServerPlayer serverPlayer)) return;

        ServerLevel serverLevel = serverPlayer.serverLevel();
        LootTable lootTable = serverLevel.getServer()
                .getLootData()
                .getLootTable(lootId);

        if (lootTable == LootTable.EMPTY) {
            // 找不到對應的 loot table 就直接結束，不炸遊戲
            return;
        }

        LootParams params = new LootParams.Builder(serverLevel)
                .create(LootContextParamSets.EMPTY);

        for (int i = 0; i < rolls; ++i) {
            lootTable.getRandomItems(params, stack -> {
                if (!stack.isEmpty()) {
                    player.addItem(stack);
                }
            });
        }
    }
}
