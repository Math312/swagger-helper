package com.sharedaka.utils;

import java.util.HashMap;
import java.util.Map;

public class BasicTypeUtil {

    private static final Map<String, String> BASIC_TYPE_MAP = new HashMap<>();

    private static final Map<String, String> BASIC_TYPE_MAP2 = new HashMap<>();

    static {
        BASIC_TYPE_MAP.put("byte", "java.lang.Byte");
        BASIC_TYPE_MAP.put("char", "java.lang.Character");
        BASIC_TYPE_MAP.put("double", "java.lang.Double");
        BASIC_TYPE_MAP.put("float", "java.lang.Float");
        BASIC_TYPE_MAP.put("int", "java.lang.Integer");
        BASIC_TYPE_MAP.put("long", "java.lang.Long");
        BASIC_TYPE_MAP.put("short", "java.lang.Short");
        BASIC_TYPE_MAP.put("boolean", "java.lang.Boolean");
        BASIC_TYPE_MAP.put("string", "java.lang.String");

        BASIC_TYPE_MAP2.put("java.lang.Byte", "byte");
        BASIC_TYPE_MAP2.put("java.lang.Character", "char");
        BASIC_TYPE_MAP2.put("java.lang.Double", "double");
        BASIC_TYPE_MAP2.put("java.lang.Float", "float");
        BASIC_TYPE_MAP2.put("java.lang.Integer", "int");
        BASIC_TYPE_MAP2.put("java.lang.Long", "long");
        BASIC_TYPE_MAP2.put("java.lang.Short", "short");
        BASIC_TYPE_MAP2.put("java.lang.Boolean", "boolean");
        BASIC_TYPE_MAP2.put("java.lang.String", "string");
        BASIC_TYPE_MAP2.put("byte", "byte");
        BASIC_TYPE_MAP2.put("char", "char");
        BASIC_TYPE_MAP2.put("double", "double");
        BASIC_TYPE_MAP2.put("float", "float");
        BASIC_TYPE_MAP2.put("int", "int");
        BASIC_TYPE_MAP2.put("long", "long");
        BASIC_TYPE_MAP2.put("short", "short");
        BASIC_TYPE_MAP2.put("boolean", "boolean");
        BASIC_TYPE_MAP2.put("string", "string");
    }

    public static String getDataTypeByCanonicalName(String canonicalName) {
        return BASIC_TYPE_MAP2.getOrDefault(canonicalName, null);
    }

}