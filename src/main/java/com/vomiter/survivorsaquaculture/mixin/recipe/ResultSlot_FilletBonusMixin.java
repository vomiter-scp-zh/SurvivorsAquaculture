package com.vomiter.survivorsaquaculture.mixin.recipe;

import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResultSlot.class)
public abstract class ResultSlot_FilletBonusMixin {

    @Shadow @Final
    private CraftingContainer craftSlots;

    @Shadow @Final
    private Player player;

    // 合成前偵測到的魚種（假設一次只有一種魚會被消耗）
    @Unique
    private Item saqua$fishItemBefore;

    // 合成前該魚的總數量
    @Unique
    private int saqua$fishCountBefore;

    /**
     * 在 onTake 一開始記錄「這次合成前」魚的種類與總數量
     */
    @Inject(method = "onTake", at = @At("HEAD"))
    private void saqua$captureFishBefore(Player pPlayer, ItemStack crafted, CallbackInfo ci) {
        if (player.level().isClientSide) return;

        saqua$fishItemBefore = null;
        saqua$fishCountBefore = 0;

        for (int i = 0; i < craftSlots.getContainerSize(); ++i) {
            ItemStack stack = craftSlots.getItem(i);
            if (!stack.isEmpty() && AquacultureAPI.FISH_DATA.hasFilletAmount(stack.getItem())) {
                saqua$fishItemBefore = stack.getItem();
                break;
            }
        }

        if (saqua$fishItemBefore == null) {
            return;
        }

        int total = 0;
        for (int i = 0; i < craftSlots.getContainerSize(); ++i) {
            ItemStack stack = craftSlots.getItem(i);
            if (!stack.isEmpty() && stack.getItem() == saqua$fishItemBefore) {
                total += stack.getCount();
            }
        }

        saqua$fishCountBefore = total;
    }

    @Inject(method = "onTake", at = @At("TAIL"))
    private void saqua$rollBonus(Player pPlayer, ItemStack crafted, CallbackInfo ci) {
        if (player.level().isClientSide) return;
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        if (saqua$fishItemBefore == null || saqua$fishCountBefore <= 0) return;

        Item fishItem = saqua$fishItemBefore;
        int beforeCount = saqua$fishCountBefore;

        saqua$fishItemBefore = null;
        saqua$fishCountBefore = 0;

        int afterCount = 0;
        for (int i = 0; i < craftSlots.getContainerSize(); ++i) {
            ItemStack stack = craftSlots.getItem(i);
            if (!stack.isEmpty() && stack.getItem() == fishItem) {
                afterCount += stack.getCount();
            }
        }

        int consumed = beforeCount - afterCount;
        if (consumed <= 0) {
            return;
        }

        ResourceLocation fishId = ForgeRegistries.ITEMS.getKey(fishItem);
        if (fishId == null) return;

        ResourceLocation lootId = new ResourceLocation(
                "survivorsaquaculture",
                "fillet/" + fishId.getNamespace() + "/" + fishId.getPath()
        );

        ServerLevel level = serverPlayer.serverLevel();
        LootTable lootTable = level.getServer()
                .getLootData()
                .getLootTable(lootId);

        if (lootTable == LootTable.EMPTY) {
            // 沒有對應 loot table 就安靜失敗
            return;
        }

        LootParams params = new LootParams.Builder(level)
                .create(LootContextParamSets.EMPTY);

        for (int i = 0; i < consumed; ++i) {
            lootTable.getRandomItems(params, stack -> {
                if (!stack.isEmpty()) {
                    player.addItem(stack);
                }
            });
        }
    }
}
