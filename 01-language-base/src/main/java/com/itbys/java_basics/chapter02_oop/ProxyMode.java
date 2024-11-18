package com.itbys.java_basics.chapter02_oop;

/**
 * Author xx
 * Date 2023/3/14
 * Desc
 */
public class ProxyMode {

    public static void main(String[] args) {

        Service service = new Service();
        ProxyServer proxyServer = new ProxyServer(service);
        proxyServer.browse();

    }
}


interface Network {

    void browse();
}


//被代理类
class Service implements Network {
    @Override
    public void browse() {
        System.out.println("真实的服务器网络");
    }
}


class ProxyServer implements Network {

    private Network network;

    public ProxyServer(Network network) {
        this.network = network;
    }

    public void check() {
        System.out.println("联网之前的检查");
    }

    @Override
    public void browse() {

        check();
        network.browse();

    }
}