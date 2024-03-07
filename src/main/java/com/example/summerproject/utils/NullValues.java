package com.example.summerproject.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public class NullValues {
public static String[] getNullPropertyNames(Object source) {
    final BeanWrapper src = new BeanWrapperImpl(source);
    PropertyDescriptor[] pds = src.getPropertyDescriptors();

    Set<String> emptyNames = new HashSet<>();
    for (PropertyDescriptor pd : pds) {
        Object srcValue = src.getPropertyValue(pd.getName());
        if (isNullOrEmpty(srcValue)) {
            emptyNames.add(pd.getName());
        }
    }
    return emptyNames.toArray(new String[0]);
}
    private static boolean isNullOrEmpty(Object value) {
        if (value == null) {
            return true;
        } else if (value instanceof String) {
            return ((String) value).isEmpty();
        } else if (value instanceof Integer) {
            return ((Integer) value) == 0;
        }else if (value instanceof Double) {
            return ((Double) value) == 0.0;
        } else {
            return false;
        }
    }

}
