package com.vomiter.survivorsaquaculture.data.size;

import com.vomiter.survivorsaquaculture.core.fish.AquaFishes;
import net.dries007.tfc.common.capabilities.size.Size;
import net.dries007.tfc.common.capabilities.size.Weight;

public class SAquaSize {
    final SAquaSizeProvider provider;
    public SAquaSize(SAquaSizeProvider provider){
        this.provider = provider;
    }

    void save(){
        for (AquaFishes fish : AquaFishes.values()) {
            int amount = fish.getFilletAmount();
            SAquaSizeProvider.Builder builder = provider.newEntry(fish.id()).ingredient(fish.item());
            if(amount < 2) builder.weight(Weight.LIGHT).size(Size.SMALL);
            else if (amount < 4) builder.weight(Weight.MEDIUM).size(Size.SMALL);
            else if (amount < 8) builder.weight(Weight.HEAVY).size(Size.NORMAL);
            else builder.weight(Weight.VERY_HEAVY).size(Size.LARGE);
            builder.save();
        }
    }

}