package com.vomiter.survivorsaquaculture.data.book;

import net.dries007.tfc.TerraFirmaCraft;
import net.minecraft.resources.ResourceLocation;

final class PatchouliConstants {
    static final String MODID = TerraFirmaCraft.MOD_ID;
    static final String BOOK_ID = "field_guide"; // the book folder id
    static final String LANG = "en_us"; // Patchouli language folder

    static ResourceLocation bookFolderRL() {
        return new ResourceLocation(MODID, "patchouli_books/" + BOOK_ID);
    }
}