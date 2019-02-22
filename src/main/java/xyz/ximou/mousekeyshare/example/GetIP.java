package xyz.ximou.mousekeyshare.example;

import java.net.InetAddress;
public class GetIP {
 
    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
         InetAddress addr = InetAddress.getLocalHost();   
         String ip=addr.getHostAddress().toString(); //获取本机ip   
         String hostName=addr.getHostName().toString(); //获取本机计算机名称   
         System.out.println(ip);
         System.out.println(hostName);
    }
 
}
