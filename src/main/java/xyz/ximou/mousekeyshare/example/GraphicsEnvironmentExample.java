package xyz.ximou.mousekeyshare.example;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class GraphicsEnvironmentExample {
  public static void main(String[] argv) throws Exception {

    java.awt.GraphicsEnvironment ge = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice[] gs = ge.getScreenDevices();

    for (int i = 0; i < gs.length; i++) {
      DisplayMode dm = gs[i].getDisplayMode();

       int refreshRate = dm.getRefreshRate();
      if (refreshRate == DisplayMode.REFRESH_RATE_UNKNOWN) {
        System.out.println("Unknown rate"); 
      }

      int bitDepth = dm.getBitDepth();
      int numColors = (int) Math.pow(2, bitDepth);
        System.out.println(numColors);
    }
  }
}