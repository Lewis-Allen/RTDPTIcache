package com.lewisallen.rtdptiCache.caches;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// TODO: possible solution to remove duplication in CodesCache ...
public abstract class CodesCache<T> {

    private Map<String, T> cache = new ConcurrentHashMap<>();
}
