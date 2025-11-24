package com.vomiter.survivorsaquaculture.data.book.content;

import com.teammetallurgy.aquaculture.init.AquaItems;
import com.vomiter.survivorsaquaculture.SurvivorsAquaculture;
import com.vomiter.survivorsaquaculture.core.registry.SAquaBlocks;
import com.vomiter.survivorsaquaculture.core.registry.SAquaItems;
import com.vomiter.survivorsaquaculture.core.registry.TFCMetalNeptunium;
import com.vomiter.survivorsaquaculture.data.book.PatchouliCategoryProvider;
import com.vomiter.survivorsaquaculture.data.book.PatchouliEntryProvider;
import com.vomiter.survivorsaquaculture.data.book.builder.CategoryJson;
import com.vomiter.survivorsaquaculture.data.book.builder.EntryJson;
import com.vomiter.survivorsaquaculture.data.book.builder.TextBuilder;
import net.dries007.tfc.util.Metal;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.Objects;

public final class BookEN {
    public static final String LANG = "en_us";
    private BookEN() {}

    public static void run(PatchouliCategoryProvider cats, PatchouliEntryProvider entries, GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        generator.addProvider(event.includeClient(), cats);
        generator.addProvider(event.includeClient(), entries);
    }

    public static void generate(GatherDataEvent event) {
        PackOutput output = event.getGenerator().getPackOutput();
        PatchouliCategoryProvider cats = new PatchouliCategoryProvider(output);
        PatchouliEntryProvider entries = new PatchouliEntryProvider(output);

        cats.setLang(LANG);
        entries.setLang(LANG);

        // Category
        assert AquaItems.ATLANTIC_COD.getId() != null;
        cats.category(
                CategoryJson.builder("survivors_aquaculture")
                        .setName("Survivor's Aquaculture")
                        .setDescription("More fishes in the TerraFirmaCraft world.")
                        .setIcon(AquaItems.ATLANTIC_COD.getId().toString())
                        .setSortnum(10)
                        .build()
        );

        int sortNum = 0;

        // Entry 1
        TextBuilder text1_1 = TextBuilder.create()
                .appendWithSpace("Aquaculture fish now follow TFC's climate rules.")
                .appendWithSpace("Each species has preferred water types, temperature ranges, and rainfall conditions.")
                .appendWithSpace("Fish yielding over")
                .appendWithSpace(TextBuilder.create().thing("8 fillets"))
                .appendWithSpace("are large fish and must be caught using large fishing bait.");

        TextBuilder text1_2 = TextBuilder.create()
                .appendWithSpace("Cold-sea species such as")
                .appendWithSpace(TextBuilder.create().thing("Atlantic Cod, Blackfish, Halibut, Herring, Salmon, Pollock, Rainbow Trout"))
                .appendWithSpace("spawn in oceans at")
                .appendWithSpace(TextBuilder.create().thing("≤0°C"))
                .appendWithSpace("with rainfall")
                .appendWithSpace(TextBuilder.create().thing("≥75"))
                .appendWithSpace(". Many of these fish are larger and yield more fillets.");

        TextBuilder text1_3 = TextBuilder.create()
                .appendWithSpace("Saltwater species such as")
                .appendWithSpace(TextBuilder.create().thing("Jellyfish, Red Grouper, Tuna"))
                .appendWithSpace("require salt water and temperatures of at least")
                .appendWithSpace(TextBuilder.create().thing("0°C"))
                .appendWithSpace(".")
                .appendWithSpace(TextBuilder.create().thing("Tuna"))
                .appendWithSpace("is especially large and must be caught using large fishing bait.");

        TextBuilder text1_4 = TextBuilder.create()
                .appendWithSpace("Freshwater fish such as")
                .appendWithSpace(TextBuilder.create().thing("Smallmouth Bass, Bluegill, Brown Trout, Carp, Catfish, Gar, Minnow, Muskellunge, Perch"))
                .appendWithSpace("spawn in fresh water at")
                .appendWithSpace(TextBuilder.create().thing("0–20°C"))
                .appendWithSpace("with no rainfall limits.");

        TextBuilder text1_5 = TextBuilder.create()
                .appendWithSpace("Arid-region fish such as")
                .appendWithSpace(TextBuilder.create().thing("Bayad, Boulti, Capitaine, Synodontis"))
                .appendWithSpace("prefer temperatures of")
                .appendWithSpace(TextBuilder.create().thing("≥13°C"))
                .appendWithSpace("and rainfall up to")
                .appendWithSpace(TextBuilder.create().thing("300"))
                .appendWithSpace(". They are common in warm, dry rivers.");

        TextBuilder text1_6 = TextBuilder.create()
                .appendWithSpace("Jungle species including")
                .appendWithSpace(TextBuilder.create().thing("Arapaima, Piranha, Tambaqui, Arrau Turtle"))
                .appendWithSpace("require dense jungle environments with temperatures of")
                .appendWithSpace(TextBuilder.create().thing("15–30°C"))
                .appendWithSpace("and rainfall")
                .appendWithSpace(TextBuilder.create().thing("≥300"))
                .appendWithSpace(".")
                .appendWithSpace(TextBuilder.create().thing("Arapaima"))
                .appendWithSpace("is a massive fish and must be caught using large fishing bait.");


        entries.entry(
                EntryJson.builder("aqua_fishes")
                        .setName("More Fishes")
                        .setCategory("tfc:survivors_aquaculture")
                        .setIcon(AquaItems.ATLANTIC_COD.getId())
                        .setReadByDefault(true)
                        .setSortnum(++sortNum)
                        .addTextPage(text1_1.toString())
                        .addTextPage(text1_2.toString())
                        .addTextPage(text1_3.toString())
                        .addTextPage(text1_4.toString())
                        .addTextPage(text1_5.toString())
                        .addTextPage(text1_6.toString())
                        .build()
        );

        //Entry2

        TextBuilder text2_1 = TextBuilder.create()
                .text("Most raw fish in the Aquaculture mod cannot be eaten directly or cooked as-is.")
                .appendWithSpace("They need to be processed first before they can be used as proper ingredients, and obtaining ")
                .thing("fish fillet")
                .append(" is the essential first step.");

        TextBuilder text2_2 = TextBuilder.create()
                .text("To get ")
                .thing("fish fillet")
                .append(", simply place any type of ")
                .thing("knife")
                .append(" together with ")
                .thing("raw fish")
                .append(" in a shapeless recipe.")
                .appendWithSpace("This will yield ")
                .thing("fish fillet")
                .append(", which can then be used in various cooking recipes.");

        TextBuilder text2_3 = TextBuilder.create()
                .text("Larger fish that produce more fillets will also yield more ")
                .thing("fish bones")
                .append(" when processed.")
                .appendWithSpace("Fish bones can be crafted into ")
                .thing("bone meal")
                .append(" for fertilizer, or used as ingredients in other recipes.");

        TextBuilder text2_4 = TextBuilder.create()
                .text("Big fish may also contain odd or unexpected items inside their bodies.")
                .appendWithSpace("Among these, the most valuable is the ")
                .thing("Neptunian Pearl")
                .append(", an essential material used for forging ")
                .thing("Neptunian Steel")
                .append(".");

        assert AquaItems.FISH_FILLET.getId() != null;
        assert AquaItems.NEPTUNIUM_FILLET_KNIFE.getId() != null;
        assert AquaItems.FISH_BONES.getId() != null;
        assert SAquaItems.NEPTUNIAN_PEARL.getId() != null;
        entries.entry(
                EntryJson.builder("processing_fish")
                        .setName("Processing Fish")
                        .setCategory("tfc:survivors_aquaculture")
                        .setIcon(AquaItems.NEPTUNIUM_FILLET_KNIFE.getId())
                        .setReadByDefault(true)
                        .setSortnum(++sortNum)
                        .addTextPage(text2_1.toString())
                        .addSpotlightPage(AquaItems.FISH_FILLET.getId().toString() ,text2_2.toString())
                        .addSpotlightPage(AquaItems.FISH_BONES.getId().toString() ,text2_3.toString())
                        .addSpotlightPage(SAquaItems.NEPTUNIAN_PEARL.getId().toString() ,text2_4.toString())
                        .extraRecipeMapping(SAquaItems.NEPTUNIAN_PEARL, 3)
                        .extraRecipeMapping(AquaItems.FISH_BONES, 2)
                        .extraRecipeMapping(AquaItems.FISH_FILLET, 1)
                        .build()
        );

        TextBuilder nameNeptunianSteel       = TextBuilder.create().thing("Neptunian Steel");
        TextBuilder nameNeptunianSteelIngot  = TextBuilder.create().thing("Neptunian Steel Ingot");
        TextBuilder nameNeptuniumIngot       = TextBuilder.create().thing("Neptunium ingot");
        TextBuilder nameBlackSteelIngot      = TextBuilder.create().thing("Black Steel ingot");
        TextBuilder nameNeptunium            = TextBuilder.create().thing("Neptunium");

        // Paragraph 1
        TextBuilder text3_1 = TextBuilder.create()
                .text("You can use")
                .appendWithSpace(nameNeptunianSteel)
                .appendWithSpace("to craft equipment designed specifically for aquatic environments.");

        // Paragraph 2
        TextBuilder text3_2 = TextBuilder.create()
                .append(nameNeptunianSteelIngot)
                .appendWithSpace("is created by welding")
                .appendWithSpace(nameNeptuniumIngot)
                .appendWithSpace("and")
                .appendWithSpace(nameBlackSteelIngot)
                .appendWithSpace("together, making it a highly advanced metal material.");

        // Paragraph 3
        TextBuilder text3_3 = TextBuilder.create()
                .text("Every tool or piece of equipment made from")
                .appendWithSpace(nameNeptunianSteel)
                .appendWithSpace("clearly lists its effects in the item tooltip,")
                .appendWithSpace("allowing you to check their functions directly from the item description.");

        // Paragraph 4
        TextBuilder text3_4 = TextBuilder.create()
                .text("Aside from equipment, both")
                .appendWithSpace(nameNeptunium)
                .appendWithSpace("and")
                .appendWithSpace(nameNeptunianSteel)
                .appendWithSpace("can also be processed into blocks, stairs, and slabs,")
                .appendWithSpace("making it easy to maintain a consistent style in water-themed constructions.");

        assert AquaItems.NEPTUNIUM_PLATE.getId() != null;
        entries.entry(
                EntryJson.builder("neptunian_steel")
                        .setName("Neptunian Steel")
                        .setCategory("tfc:survivors_aquaculture")
                        .setIcon(Objects.requireNonNull(SAquaBlocks.METAL.get(TFCMetalNeptunium.NEPTUNIAN_STEEL).get(Metal.BlockType.BLOCK).getId()))
                        .setReadByDefault(true)
                        .setSortnum(++sortNum)
                        .addTextPage(text3_1.toString())
                        .addWeldingRecipe(new ResourceLocation(SurvivorsAquaculture.MODID, "welding/neptunian_steel_ingot"), text3_2.toString())
                        .addSpotlightPage(AquaItems.NEPTUNIUM_PLATE.getId().toString() ,text3_3.toString())
                        .addTextPage(text3_4.toString())
                        .addSingleBlockPage("", Objects.requireNonNull(SAquaBlocks.METAL.get(TFCMetalNeptunium.NEPTUNIUM).get(Metal.BlockType.BLOCK).getId().toString()))
                        .extraRecipeMapping(SAquaItems.METAL_ITEMS.get(TFCMetalNeptunium.NEPTUNIAN_STEEL).get(TFCMetalNeptunium.ItemType.INGOT), 3)
                        .extraRecipeMapping(SAquaItems.METAL_ITEMS.get(TFCMetalNeptunium.NEPTUNIUM).get(TFCMetalNeptunium.ItemType.INGOT), 3)
                        .build()
        );



        /*
                entries.entry(
                EntryJson.builder("")
                        .setCategory("tfc:survivors_delight")
                        .setReadByDefault(true)
                        .setSortnum(++sortNum)
                        .setName("")
                        .setIcon("")
                        .addTextPage(
                                "A farmer's cabinet is an advanced way to $(l:mechanics/decay)store food$(). "
                                    + "You may press $(item)$(k:key.use)$() on it with tallow bucket or beeswax to treat it."
                                    + "A treated cabinet can preserve food for a longer period."
                        )
                        .build());

         */

        run(cats, entries, event);
    }
}
