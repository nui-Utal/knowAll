package com.example.newbst.utils;

import org.springframework.beans.BeanWrapperImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * created by Xu on 2023/8/19 14:03.
 */
public class IgnoreProperty {
    public static String[] getIgnorePropertyNames(Object source, Object target) {
        final BeanWrapperImpl src = new BeanWrapperImpl(source);
        final BeanWrapperImpl tar = new BeanWrapperImpl(target);
        java.beans.PropertyDescriptor[] srcPds = src.getPropertyDescriptors();
        java.beans.PropertyDescriptor[] tarPds = tar.getPropertyDescriptors();

        Set<String> ignoreName = new HashSet<>();
        ignoreName.addAll(Arrays.stream(srcPds)
                .filter(pd -> src.getPropertyValue(pd.getName()) == null)
                .map(java.beans.PropertyDescriptor::getName)
                .collect(Collectors.toSet()));

        ignoreName.addAll(Arrays.stream(tarPds)
                .filter(pd -> tar.getPropertyValue(pd.getName()) != null)
                .map(java.beans.PropertyDescriptor::getName)
                .collect(Collectors.toSet()));

        return ignoreName.toArray(new String[0]);
    }
}
