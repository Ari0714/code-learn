package com.example.springboot.controller;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Properties;


/**
 * Author xx
 * Date 2024/1/4
 * Desc
 */
@Repository
public class RemoteShell {

    public static void execShell(String command) {

        String host = "hdp";
        String user = "root";
        String password = "111111";


        try {
            JSch jsch = new JSch();

            Session session = jsch.getSession(user, host, 22);

            session.setPassword(password);

            Properties config = new Properties();

            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);

            session.connect();

            Channel channel = session.openChannel("exec");

            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream((InputStream) null);
            InputStream in = channel.getInputStream();
            channel.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
            channel.disconnect();
            session.disconnect();
        } catch (JSchException var11) {
            var11.printStackTrace();
        } catch (IOException var12) {
            var12.printStackTrace();

        }


    }

}
