package com.sc.util.trino;

import com.google.common.collect.ImmutableSet;
import io.trino.spi.Plugin;

import java.util.Set;


/**
 * Author Ari
 * Date 2025/8/20
 * Desc
 */
public class MyUdfPlugin implements Plugin {
    @Override
    public Set<Class<?>> getFunctions() {
        return ImmutableSet.of(TrinoReverseStringFunctions.class);
    }
}
