package com.sc.util;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Author Ari
 * Date 2024/3/26
 * Desc generate random passwd
 */
public class MyPasswdUtil {

    @Test
    public void tets01() throws IOException {

        Random random = new Random();

        ArrayList<String> arrayList = MyFileUtil.readFile("input/datax.txt");
        for (String s : arrayList) {
            String[] split = s.split("\t");
//            System.out.println(s + "@" + random.nextInt(1000000));

//            System.out.println(s + ".*" );
            System.out.println("GRANT SELECT_PRIV,LOAD_PRIV,ALTER_PRIV,CREATE_PRIV,DROP_PRIV ON " + split[2] + " TO '" + split[0] + "'@'%';\n");

//            String[] split = s.split("@");
//            System.out.println("CREATE DATABASE "+split[0]+";");
//            System.out.println("CREATE USER '"+split[0]+"'@'%' IDENTIFIED BY '"+s+"';");
//            System.out.println("GRANT SELECT_PRIV,LOAD_PRIV,ALTER_PRIV,CREATE_PRIV,DROP_PRIV ON "+split[0]+".* TO '"+split[0]+"'@'%';");
        }

    }

}
