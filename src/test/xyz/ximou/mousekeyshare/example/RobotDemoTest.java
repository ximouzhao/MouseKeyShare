package xyz.ximou.mousekeyshare.example;

import org.junit.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class ScreenTest {
    @Test
    public void getScreenInfo(){
        int width=(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int height=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        System.out.println(width+";"+height);
    }

}