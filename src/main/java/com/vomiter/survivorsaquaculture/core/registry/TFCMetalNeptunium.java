package com.vomiter.survivorsaquaculture.core.registry;

import net.dries007.tfc.common.TFCArmorMaterials;
import net.dries007.tfc.common.TFCTiers;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.registry.RegistryMetal;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.function.Supplier;

public enum TFCMetalNeptunium implements RegistryMetal {
    NEPTUNIUM(1236942, MapColor.DIAMOND),
    NEPTUNIAN_STEEL(1236942, MapColor.DIAMOND);

    private final String serializedName;
    private final int color;
    private final MapColor mapColor;

    TFCMetalNeptunium(int color, MapColor mapColor){
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.color = color;
        this.mapColor = mapColor;
    }

    @Override
    public @NotNull String getSerializedName()
    {
        return serializedName;
    }

    public int getColor()
    {
        return color;
    }

    @Override
    public @NotNull Rarity getRarity()
    {
        return Rarity.EPIC;
    }

    @Override
    public @NotNull Tier toolTier()
    {
        return TFCTiers.BLACK_STEEL;
    }

    @Override
    public @NotNull ArmorMaterial armorTier()
    {
        return TFCArmorMaterials.BLUE_STEEL;
    }

    @Override
    public Metal.@NotNull Tier metalTier()
    {
        return Metal.Tier.TIER_VI;
    }

    @Override
    public @NotNull MapColor mapColor()
    {
        return mapColor;
    }

    @Override
    public @NotNull Supplier<Block> getFullBlock()
    {
        return SAquaBlocks.METAL.get(this).get(Metal.BlockType.BLOCK);
    }

    public enum ItemType implements MetalItemType
    {
        INGOT,
        DOUBLE_INGOT,
        SHEET,
        DOUBLE_SHEET,
        ROD;
    }

    public enum UnfinishedArmorType implements MetalItemType{
        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS;

        public String getSerializedName(){
            return "unfinished_" + this.name().toLowerCase(Locale.ROOT);
        }
    }

}
