package com.itbys.java_basics.chapter05_used_classes._01_string;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class _04_srtingBuffer_Builder {

    public static void main(String[] args) {

        StringBuilder aa = new StringBuilder("aa");

        //
        StringBuilder bb = aa.append("bb");

        //
        StringBuilder delete = bb.delete(2, 4);

        //
        StringBuilder cc = bb.replace(2, 4, "cc");

        //
        StringBuilder insert = bb.insert(0, "11");

        //
        StringBuilder reverse = insert.reverse();


        System.out.println(reverse);



    }

}
