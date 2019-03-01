package xyz.ximou.mousekeyshare.nettransport.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ximou.mousekeyshare.nettransport.constant.UDPConstants;
import xyz.ximou.mousekeyshare.util.ByteUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

/**
 * 开放UDP端口，供其他客户端搜索
 */
public class UDPProvider {
    static Logger logger = LoggerFactory.getLogger(Object.class);
    private static class Provider extends Thread{
        private final byte[] sn;
        private final int port;
        private boolean done=false;
        private DatagramSocket ds=null;

        final byte[] buffer=new byte[1128];

        Provider(String sn,int port){
            super();
            this.sn=sn.getBytes();
            this.port=port;
        }
        @Override
        public void run(){
            super.run();
            logger.info("UDPProvider Started.");

            try{
                ds=new DatagramSocket(UDPConstants.PORT_SERVER);
                //接收消息的Packet
                DatagramPacket receivePack=new DatagramPacket(buffer,buffer.length);
                while (!done){
                    //接收
                    ds.receive(receivePack);

                    //打印接收到的信息与发送者的信息
                    //发送者的IP地址
                    String clientIP=receivePack.getAddress().getHostAddress();
                    int clientPort=receivePack.getPort();
                    int clientDataLen=receivePack.getLength();
                    byte[] clientData=receivePack.getData();

                    boolean isValid=clientDataLen>=(UDPConstants.HEADER.length+2+4)
                            && ByteUtils.startsWith(clientData,UDPConstants.HEADER);
                    if(!isValid){
                        //无效继续
                        continue;
                    }
                    //解析命令与回送端口
                    int index=UDPConstants.HEADER.length;
                    short cmd=(short)((clientData[index++]<<8)|
                            (clientData[index++]));
                    int responsePort=(((clientData[index++]<<24)|
                            (clientData[index++])<<16|
                            (clientData[index++]<<8)|
                            (clientData[index++])));
                    //判断合法性
                    if(cmd==1&&responsePort>0){
                        //构建一份回送数据
                        ByteBuffer byteBuffer=ByteBuffer.wrap(buffer);
                        byteBuffer.put(UDPConstants.HEADER);
                        byteBuffer.putShort((short)2);
                        byteBuffer.putInt(port);
                        byteBuffer.put(sn);

                        int len=byteBuffer.position();
                        //直接根据发送者构建一份回送信息
                        DatagramPacket responsePack=new DatagramPacket(buffer,
                                len,
                                receivePack.getAddress(),
                                responsePort);
                        ds.send(receivePack);
                        logger.info("ServerProvider response to:"+clientIP+"\tport:"+responsePack+"\tdataLen:"+len);

                    }else{
                        logger.info("ServerProvider receive cmd nonsupport;cmd:"+cmd+"\tport:"+port);
                    }
                }
            }catch (Exception ex){

            }finally {
                close();
            }
        }

        private void close(){
            if(ds!=null){
                ds.close();
                ds=null;
            }
        }
        //结束进程
        void exit(){
            done=true;
            close();
        }
    }
}
