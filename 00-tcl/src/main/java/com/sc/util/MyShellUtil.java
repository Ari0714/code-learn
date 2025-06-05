package com.sc.util;

import java.util.*;

import com.jcraft.jsch.*;

/**
 * Author Ari
 * Date 2024/8/23
 * Desc
 */
public class MyShellUtil {

    private static Session session;
    private static Channel channel;
    private static ChannelExec channelExec;
    private static ChannelSftp sftp = null;

    public static void main(String[] args) {

//        getShell();

        //10.126.124.73,10.126.124.74	22	hdfs	hdfs@123
//        for (int i = 0; i < 10; i++) {
            String hdfs = verifyValidIp("10.126.124.73,10.126.124.74", 22, "hdfs", "hdfs@123");
        System.out.println(hdfs);

//        }

        try {
            Channel hdfs1 = init(hdfs, 22, "hdfs", "hdfs@123");
            System.out.println(hdfs1);
        } catch (JSchException e) {
            e.printStackTrace();
        }

//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 2; j++) {
//                if (j == 1) {
//                    break;
//                }
//                System.out.println(i + "," + j);
//            }
//        }


    }

    public static  Channel init(String ip, Integer port, String username, String password) throws JSchException {
        JSch jsch = new JSch();
        jsch.getSession(username, ip, port);
        session = jsch.getSession(username, ip, port);
        session.setPassword(password);
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        session.setConfig(sshConfig);
        session.connect(60 * 1000);
//        LOGGER.info("Session connected!");
        // 打开执行shell指令的通道
        channel = session.openChannel("exec");
        channelExec = (ChannelExec) channel;
        Channel channel = session.openChannel("sftp");
        channel.connect();
        sftp = (ChannelSftp) channel;
        return sftp;
    }

    public static void getShell() {

        String host = "10.126.124.735";
        String user = "root";
        String pwd = "hadoop@1010";
        int port = 22;

        Session session = null;
        Channel channel = null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);

            Properties prop = new Properties();
            //File file = new File(SystemUtils.getUserHome() + "/.ssh/id_rsa");
            //String knownHosts = SystemUtils.getUserHome() + "/.ssh/known_hosts".replace('/', File.separatorChar);
            //jsch.setKnownHosts(knownHosts)
            //jsch.addIdentity(file.getPath())
//            prop.put("PreferredAuthentications", "publickey");
            prop.put("PreferredAuthentications", "password");
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);
            session.setPort(port);
            session.setPassword(pwd);
            session.connect();

            boolean connected = session.isConnected();
            if (connected) {
                System.out.println("connection success");
            } else {
                System.out.println("connection fail");
            }
            System.out.println(session);

//            session.setConfig("StrictHostKeyChecking", "no");
//            session.connect(30000);   // making a connection with timeout.
//            channel = session.openChannel("shell");
//            channel.setInputStream(System.in);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            channel.setOutputStream(baos);
//            channel.connect(3 * 1000);
//            System.out.println(baos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("finally...");
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }

    }


    public static String verifyValidIp(String ips, int port, String user, String pwd) {

        String validIp = null;
        Session session = null;

        List<String> splitIps = Arrays.asList(ips.split(","));
        Collections.shuffle(splitIps);

        // retry 3 times
        for (int i = 0; i < 3; i++) {
            for (String ip : splitIps) {
                try {
                    JSch jsch = new JSch();
                    session = jsch.getSession(user, ip, port);

                    Properties prop = new Properties();
                    prop.put("PreferredAuthentications", "password");
                    prop.put("StrictHostKeyChecking", "no");
                    session.setConfig(prop);
                    session.setPort(port);
                    session.setPassword(pwd);
                    session.connect();

                    boolean connected = session.isConnected();
                    if (connected) {
//                        log.error("[success] ssh connection success:{}", ip);
                        validIp = ip;
                        break;
                    }
                    System.out.println(session);

                } catch (Exception e) {
//                    log.error("[error] ssh connection fail:{}", ip);
                    e.printStackTrace();
                } finally {
                    if (session != null) {
                        session.disconnect();
                    }
                }
            }

            if (validIp != null) {
                break;
            } else {
//                log.error("ssh connection fail：retry.....");
                try {
                    Thread.sleep(60 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        return validIp;
    }


}
