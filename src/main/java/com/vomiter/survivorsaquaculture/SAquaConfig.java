package com.vomiter.survivorsaquaculture;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class SAquaConfig {

    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON;

    static {
        Pair<Common, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = pair.getLeft();
        COMMON_SPEC = pair.getRight();
    }

    public static class Common {

        public final ForgeConfigSpec.BooleanValue ADJUST_FISH_SIZE;

        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("SurvivorsAquaculture - Common settings")
                    .push("general");

            ADJUST_FISH_SIZE = builder
                    .comment("Make fish larger according to how many fillets it provides.")
                    .define("adjustFishSize", true);

            builder.pop();
        }
    }
}
