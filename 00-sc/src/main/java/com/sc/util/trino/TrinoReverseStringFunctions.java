//package com.sc.util.trino;
//
//
//import io.trino.spi.function.*;
//import io.trino.spi.type.StandardTypes;
//import io.trino.spi.type.Type;
//import io.airlift.slice.Slice;
//import io.airlift.slice.Slices;
//
//import java.util.Date;
//
///**
// * Author Ari
// * Date 2025/8/20
// * Desc
// */
//public class TrinoReverseStringFunctions {
//
//    private TrinoReverseStringFunctions() {
//    }
//
//    static Type type;
//
//    @ScalarFunction("reverse_string")
//    @SqlType(StandardTypes.BIGINT)
//    @SqlNullable
//    public static Long length(@SqlType(StandardTypes.VARCHAR) Slice input) {
//        if (input == null) return null;
//        return (long) input.length();
//    }
//
//
//    @ScalarFunction("reverse_string2")
//    @SqlType(StandardTypes.BIGINT)
//    @SqlNullable
//    public static Long reverseInt(@SqlType(StandardTypes.DATE) long date) {
//        return (long)date;
//    }
//
//
//}
