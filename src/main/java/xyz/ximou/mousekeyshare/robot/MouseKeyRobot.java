package xyz.ximou.mousekeyshare.robot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
public class MouseKeyRobot {
    private static final Logger log= LoggerFactory.getLogger(MouseKeyRobot.class);
    static Robot robot;
    static long lastTime;
    static{
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        lastTime=System.currentTimeMillis();
    }
    static int screenWidth=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    static int screenHeight=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    public static void mouseMove(int x,int y){
        //按照120的刷新率来计算，每帧0.00833333333秒,调用过于频繁会导致系统死机
        long current=System.currentTimeMillis();
        long difference=current-lastTime;
        if(difference<=8){
            System.out.println("调用移动鼠标时间间隔过短，间隔为"+difference+"毫秒，舍弃本次操作");
            log.warn("调用移动鼠标时间间隔过短，间隔为"+difference+"毫秒，舍弃本次操作");
            return ;
        }else{
            lastTime=current;
        };
        if(screenWidth>x){
            log.warn("横坐标x超出范围："+x+",屏幕最大横坐标x："+screenWidth);
        }
        if(screenHeight>y){
            log.warn("纵坐标y超出范围："+y+",屏幕最大纵坐标y："+screenHeight);
        }
        robot.mouseMove(x,y);
    }
}
