package com.vomiter.survivorsaquaculture.core.registry;

import net.dries007.tfc.common.TFCTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static com.vomiter.survivorsaquaculture.core.registry.TFCMetalNeptunium.ItemType.*;

public interface MetalItemType {
    default TagKey<Item> getPileable(){
        return this.equals(INGOT)? TFCTags.Items.PILEABLE_INGOTS:
                this.equals(DOUBLE_INGOT)? TFCTags.Items.PILEABLE_DOUBLE_INGOTS:
                        this.equals(SHEET)? TFCTags.Items.PILEABLE_SHEETS:
                                null;
    }

    default Item create(TFCMetalNeptunium metal){
        return new Item(new Item.Properties());
    };
}
