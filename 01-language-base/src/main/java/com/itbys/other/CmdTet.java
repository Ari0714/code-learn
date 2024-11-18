package com.itbys.other;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc 调度shell脚本
 */
public class CmdTet {

    public static void main(String[] args) throws IOException {

        Process exec = Runtime.getRuntime().exec("jps");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));

        String flag;
        while ((flag = bufferedReader.readLine()) != null){
            System.out.println(flag);
        }

        bufferedReader.close();

    }

}
