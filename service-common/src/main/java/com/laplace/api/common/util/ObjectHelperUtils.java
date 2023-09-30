package com.laplace.api.common.util;

import org.springframework.util.ObjectUtils;
import java.util.List;

public final class ObjectHelperUtils {

    private ObjectHelperUtils() {
    }

    public static boolean isAllEmpty(List<Object> objectList) {
        return ObjectUtils.isEmpty(objectList) || objectList.stream().allMatch(ObjectUtils::isEmpty);
    }

    public static boolean isNoneEmpty(List<Object> objectList) {
        return !ObjectUtils.isEmpty(objectList) && objectList.stream().noneMatch(ObjectUtils::isEmpty);
    }
}
