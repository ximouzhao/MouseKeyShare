package xyz.ximou.mousekeyshare.example;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Random;
 
import javax.swing.*;
 
public class MyJPanel extends JPanel implements MouseMotionListener{
 
	@Override
	public void paint(Graphics g) {
		Image image = new ImageIcon("snowflower.png").getImage();
		Random r = new Random();//利用随机数来控制屏保出现位置
		for(int i = 0; i < 30; i++){
			int x = r.nextInt(1366)-50;
			int y = r.nextInt(768) - 50;
			g.drawImage(image, x,  y, null);
		}
 
	}
	
	public void move(){
		new Thread(){//新建一个线程
			public void run(){
				while(true){
					repaint();//重画，实现画布移动效果
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
 
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
 
	@Override
	public void mouseMoved(MouseEvent e) {
		//重写，监听到此动作，执行以下语句退出程序
		System.exit(0);
	}
}