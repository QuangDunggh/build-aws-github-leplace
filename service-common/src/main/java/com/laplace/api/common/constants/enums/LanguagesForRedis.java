package com.laplace.api.common.constants.enums;

import java.util.function.Supplier;
import java.util.stream.Stream;

public enum LanguagesForRedis {

    ENGLISH("en"),
    JAPANESE("jp");

    private String value;

    LanguagesForRedis(String value) {
        this.value = value;
    }

    private static Supplier<Stream<LanguagesForRedis>>
    languagesForRedisSupplier() {
        return () -> Stream.of(LanguagesForRedis.values());
    }

    public static LanguagesForRedis forName(String languagesForRedis) {
        return languagesForRedisSupplier().get()
                .filter(condition -> condition.name().equals(languagesForRedis))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Cannot create enum from " + languagesForRedis + " value!"));
    }

    public static Stream<LanguagesForRedis> stream() {
        return Stream.of(LanguagesForRedis.values());
    }

    public String getValue() {
        return value;
    }
}
