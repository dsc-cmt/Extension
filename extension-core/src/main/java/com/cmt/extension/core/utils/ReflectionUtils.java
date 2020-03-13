package com.cmt.extension.core.utils;

import com.cmt.extension.core.annotation.SPI;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通过实现类找到它实现的SPI接口
 *
 * @author yonghuang
 */
public class ReflectionUtils {
    public static List<Class> getAllSpies(Class clz) {
        List<Class> interfaces = getAllInterfaces(clz);
        if (interfaces != null) {
            return interfaces.stream().filter((o) -> o.isAnnotationPresent(SPI.class))
                    .collect(Collectors.toList());
        }

        return null;
    }

    private static List<Class> getAllInterfaces(Class clz) {
        List<Class> interfaces = null;

        Class[] inters = clz.getInterfaces();
        if (inters != null && inters.length != 0) {
            interfaces = new ArrayList<>();
            for (Class c : inters) {
                interfaces.add(c);
                List<Class> supers = getAllInterfaces(c);
                if (supers != null) {
                    interfaces.addAll(supers);
                }
            }
        }

        Class superClz = clz.getSuperclass();
        if (superClz == null) {
            return interfaces;
        }


        List<Class> supers = getAllInterfaces(superClz);
        if (supers != null) {
            if (interfaces == null) {
                interfaces = new ArrayList<>();
            }
            interfaces.addAll(supers);
        }


        return interfaces;
    }
}
