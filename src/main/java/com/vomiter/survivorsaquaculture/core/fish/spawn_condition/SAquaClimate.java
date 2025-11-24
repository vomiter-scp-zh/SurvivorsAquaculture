package com.vomiter.survivorsaquaculture.core.fish.spawn_condition;

import com.vomiter.survivorsaquaculture.data.SAquaEntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class SAquaClimate {

    public static final class Range {
        public final int minT, maxT;   // °C
        public final int minR, maxR;   // mm

        public Range(int minT, int maxT, int minR, int maxR) {
            this.minT = minT; this.maxT = maxT; this.minR = minR; this.maxR = maxR;
        }

        public boolean test(int tempC, int rainfallMm) {
            return tempC >= minT && tempC <= maxT && rainfallMm >= minR && rainfallMm <= maxR;
        }

        public static Range of(int minT, int maxT, int minR, int maxR) {
            return new Range(minT, maxT, minR, maxR);
        }
    }

    public static final Range ANY = Range.of(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);

    public static final Map<TagKey<EntityType<?>>, Range> RANGES;
    static {
        Map<TagKey<EntityType<?>>, Range> m = new HashMap<>();
        //MIN TEMP, MAX TEMP, MIN RAINFALL, MAX RAIN FALL
        m.put(SAquaEntityTypeTags.ARCTIC_OCEAN_FISH,   Range.of(Integer.MIN_VALUE, 0,   75, Integer.MAX_VALUE));
        m.put(SAquaEntityTypeTags.SALT_WATER_FISH,     Range.of( 0, Integer.MAX_VALUE,   0, Integer.MAX_VALUE));

        m.put(SAquaEntityTypeTags.FRESH_WATER_FISH,    Range.of(0, 20,   0, Integer.MAX_VALUE));
        m.put(SAquaEntityTypeTags.ARID_FISH,           Range.of( 13, Integer.MAX_VALUE,  -1, 300));

        m.put(SAquaEntityTypeTags.JUNGLE_FISH,         Range.of( 15, 30, 300, Integer.MAX_VALUE));
        m.put(SAquaEntityTypeTags.SWAMP_FISH,          Range.of( 12, 30, 300, Integer.MAX_VALUE));

        m.put(SAquaEntityTypeTags.MUSHROOM_ISLAND_FISH,Range.of(  8, 24, 150, 450));
        m.put(SAquaEntityTypeTags.TWILIGHT_FOREST_FISH,Range.of(  0, 21, 190, 500));
        m.put(SAquaEntityTypeTags.ANYWHERE_FISH,       ANY);
        RANGES = Collections.unmodifiableMap(m);
    }

    public static boolean canSpawn(TagKey<EntityType<?>> tag, int tempC, int rainfallMm) {
        Range r = RANGES.getOrDefault(tag, ANY);
        return r.test(tempC, rainfallMm);
    }
}
