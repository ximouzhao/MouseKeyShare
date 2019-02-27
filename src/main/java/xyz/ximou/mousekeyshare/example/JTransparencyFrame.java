package xyz.ximou.mousekeyshare.example;

import com.sun.awt.AWTUtilities;

import javax.swing.*;
import java.awt.*;

public class JTransparencyFrame extends JFrame {
    public JTransparencyFrame(){

        setUndecorated(true);
        AWTUtilities.setWindowOpacity(this,0.5f);
        setVisible(true);
    }
    public void center() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setSize(screenWidth,screenWidth);
        this.setLocation((screenWidth - this.getWidth()) / 2, (screenHeight - this.getHeight()) / 2);
    }

    public static void main(String[] args) {
        JTransparencyFrame customFrame=new JTransparencyFrame();

        customFrame.setTitle("Ttranslucency JFrame");
        customFrame.setSize(400, 300);
        //customFrame.setOpacity(0.5f);
        customFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        customFrame.center();
        customFrame.setAlwaysOnTop(true);
        customFrame.validate();
        //GraphicsEnvironment.getLocalGraphicsEnvironment()
         //       .getDefaultScreenDevice()
         //       .setFullScreenWindow(customFrame);
    }
}
