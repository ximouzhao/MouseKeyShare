package xyz.ximou.mousekeyshare.example.swing;

import javax.swing.JFrame;
 
import com.sun.awt.AWTUtilities;//实现窗口透明很重要的一个包


public class MyJFrame extends JFrame {
	public MyJFrame() {
		this.setSize(1366, 768);//设置大小，即当前分辨率，可以手动设置，也可以自动获取
		this.setUndecorated(true);//禁用或启动用此JFrame装饰 
		this.setLocationRelativeTo(null);//默认窗口居中对齐
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置用户在此窗体上发起 "close" 时默认执行的操作
		MyJPanel p = new MyJPanel();
		AWTUtilities.setWindowOpaque(this, false);//flase设置透明反之不透明
		this.add(p);
		p.setBackground(null); //将背景色设置为空
		p.setOpaque(false);//将背景设置为透明
		this.setVisible(true);
		p.move();//调用用线程实现的画布移动功能
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.addMouseMotionListener(p);//注册鼠标监听器
	}

	public static void main(String[] args) {
		new MyJFrame();
	}
}