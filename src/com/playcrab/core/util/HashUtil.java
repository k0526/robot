package com.playcrab.core.util;

public class HashUtil {
    public static int hash(Object k) {
        int h = 0;
        h ^= k.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12);
        int result = h ^ (h >>> 7) ^ (h >>> 4);
        return Math.abs(result);
    }
}
