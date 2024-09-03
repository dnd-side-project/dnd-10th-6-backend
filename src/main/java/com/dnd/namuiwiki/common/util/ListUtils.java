package com.dnd.namuiwiki.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ListUtils {
    public static <T> List<T> convertList(Object obj) {
        if (obj.getClass().isArray()) {
            return Arrays.asList((T[]) obj);
        } else if (obj instanceof Collection) {
            return new ArrayList<>((Collection<T>) obj);
        } else {
            throw new ClassCastException("Object is not a List.");
        }

    }
}
