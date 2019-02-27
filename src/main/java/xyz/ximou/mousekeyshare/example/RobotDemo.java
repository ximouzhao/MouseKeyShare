package xyz.ximou.mousekeyshare.example;

import xyz.ximou.mousekeyshare.robot.MouseKeyRobot;

import java.awt.*;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 测试java的robot类是否能流畅的控制鼠标
 */
public class RobotDemo {

    public static  void main(String []argv) throws IOException {
        MouseMoveThread thread=new MouseMoveThread();
        thread.start();
        InputStreamReader isr=new InputStreamReader(System.in);
        int r=isr.read();
        thread.runFlag=false;
    }

    public static class  MouseMoveThread extends Thread{
        int x=300;
        int y=300;
        boolean runFlag=true;

        @Override
        public void run() {
            int width=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()-300;
            int height=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()-200;
            while(runFlag){
                ++x;
                while (runFlag){
                    //robot.mouseMove(x,++y);
                    MouseKeyRobot.mouseMove(x,++y);
                    //System.out.println("x="+x+";y="+y);
                    //System.out.println(MouseInfo.getPointerInfo().getLocation().toString());
                    if(height==y){
                        y=300;
                        break;
                    }
                    try {
                        Thread.sleep(8);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(width==x){
                    x=300;
                    //break;
                }
            }
        }
    }
}
