package xyz.ximou.mousekeyshare.example;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.*;
import xyz.ximou.mousekeyshare.robot.MouseKeyRobot;
import xyz.ximou.mousekeyshare.systeminfo.SystemInfo;
import xyz.ximou.mousekeyshare.systeminfo.SystemTypeEnum;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsumeEvent implements NativeKeyListener, NativeMouseInputListener, NativeMouseWheelListener {

	Field f=null;

	//mac下当鼠标移动到左边界的时候，将鼠标移动到右边界，weight--，相当于扩展了一个无限大的屏幕，
	//用于计算鼠标的偏移量，并且不受制于屏幕的大小
	int xScreenWeight=0;
	int yScreenWeight=0;

	int lastXScreenWeight=0;
	int lastYScreenWeight=0;

	int lastX=0;
	int lastY=0;

	//Windows下当ConsumeEvent，将事件吃掉之后，mousemove事件只会获得每次的偏移量，鼠标在屏幕上不会移动，
	// 我们计划当鼠标到左边屏幕边界的时候就吃掉事件，所以通过变量来计算鼠标真正应该在的位置，并且验证
	//“鼠标是否回到了屏幕”，当鼠标应该回到屏幕的时候，我们便不再吃掉事件。
	int virtualX=0;
	int virtualY=0;
	short reserved=0x00;
	{
		try {
			f = NativeInputEvent.class.getDeclaredField("reserved");
			f.setAccessible(true);
			lastX=(int)MouseInfo.getPointerInfo().getLocation().getX();
			lastY=(int)MouseInfo.getPointerInfo().getLocation().getY();
		}
		catch (Exception ex) {
			System.out.print("[ !! ]\n");
			ex.printStackTrace();
		}
	}
	public ConsumeEvent() throws NativeHookException {
		// Create custom logger and level.
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.WARNING);

		GlobalScreen.setEventDispatcher(new VoidDispatchService());
		GlobalScreen.registerNativeHook();

		GlobalScreen.addNativeKeyListener(this);
		GlobalScreen.addNativeMouseListener(this);
		GlobalScreen.addNativeMouseMotionListener(this);
	}

	@Override
	public void nativeMouseClicked(NativeMouseEvent nativeMouseEvent) {
		setReserved(nativeMouseEvent);
	}

	@Override
	public void nativeMousePressed(NativeMouseEvent nativeMouseEvent) {
		setReserved(nativeMouseEvent);
		//System.out.println("mouse pressed"+nativeMouseEvent.getPoint());
	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent nativeMouseEvent) {
		setReserved(nativeMouseEvent);
	}

	@Override
	public void nativeMouseMoved(NativeMouseEvent nativeMouseEvent) {
		System.out.println("mouse movedw"+nativeMouseEvent.getPoint());
		int width=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int height=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		System.out.println(Toolkit.getDefaultToolkit().getScreenSize().getSize());
		if(SystemInfo.getSystemType()== SystemTypeEnum.MAC_OS){
			if(nativeMouseEvent.getX()==(width-1)){
				MouseKeyRobot.mouseMove(0,nativeMouseEvent.getY());
			}
			if(nativeMouseEvent.getX()==0){
				MouseKeyRobot.mouseMove(width-1,nativeMouseEvent.getY());
			}
			if(nativeMouseEvent.getY()==height-1){
				MouseKeyRobot.mouseMove(nativeMouseEvent.getX(),0);
			}
			if(nativeMouseEvent.getY()==0){
				MouseKeyRobot.mouseMove(nativeMouseEvent.getX(),height-1);
			}

			if(lastX-nativeMouseEvent.getX()==(width-1)){
				xScreenWeight++;
			}

			if(nativeMouseEvent.getX()-lastX==(width-1)){
				xScreenWeight--;
			}

			if(nativeMouseEvent.getY()-lastY==(height-1)){
				yScreenWeight++;
			}
			if(lastY-nativeMouseEvent.getY()==(height-1)){
				yScreenWeight--;
			}
			int diffx=nativeMouseEvent.getX()-lastX+(lastXScreenWeight-xScreenWeight)*width;
			int diffy=nativeMouseEvent.getY()-lastY+(lastYScreenWeight-yScreenWeight)*height;
			System.out.println("diffx:"+diffx);
			System.out.println("diffy:"+diffy);
			lastX=nativeMouseEvent.getX();
			lastY=nativeMouseEvent.getY();
			lastXScreenWeight=xScreenWeight;
			lastYScreenWeight=yScreenWeight;
			System.out.println("xScreenWeight:"+xScreenWeight+",yScreenWeight:"+yScreenWeight+";");
			if(xScreenWeight==0&&yScreenWeight==0){
				callEventToNext();
			}else{
				consumeEvent();
			}

		}else if(SystemInfo.getSystemType()== SystemTypeEnum.WINDOWS){

			if(this.reserved==0x01){
				//当鼠标处于丢弃事件的情况下，首先计算此时鼠标应该在的位置。
				int diffx=nativeMouseEvent.getX()-lastX;
				int diffy=nativeMouseEvent.getY()-lastY;
				System.out.println("diffx:"+diffx);
				System.out.println("diffy:"+diffy);
				virtualX+=diffx;
				virtualY+=diffy;
				System.out.println("virtualX:"+virtualX);
				System.out.println("virtualY:"+virtualY);
				if(virtualX>0){
					callEventToNext();
				}
			}else{
				//当鼠标到达左边界，并且未丢弃事件的情况下，记录此时的鼠标坐标,开始丢弃事件。
				System.out.println("nativeMouseEvent.getX()"+(nativeMouseEvent.getX()<=0));
				if(nativeMouseEvent.getX()<=0){
					virtualX=lastX=nativeMouseEvent.getX();
					virtualY=lastY=nativeMouseEvent.getY();
					consumeEvent();
				}
			}
		}else if(SystemInfo.getSystemType()== SystemTypeEnum.LINUX||SystemInfo.getSystemType()== SystemTypeEnum.OTHER){
			System.out.println("不支持Linux以及其他系统");
		}
		setReserved(nativeMouseEvent);

	}

	@Override
	public void nativeMouseDragged(NativeMouseEvent nativeMouseEvent) {
		setReserved(nativeMouseEvent);
	}

	@Override
	public void nativeMouseWheelMoved(NativeMouseWheelEvent nativeMouseWheelEvent) {
		setReserved(nativeMouseWheelEvent);
	}

	public void consumeEvent(){
		reserved=0x01;
	}
	public void callEventToNext(){
		reserved=0x00;
	}

	/**
	 * 确定事件是否继续向下传递
	 * @param e
	 */
	public void setReserved(NativeInputEvent e ) {
		try {
			f.setShort(e,reserved);
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
	}

	private class VoidDispatchService extends AbstractExecutorService {
		private boolean running = false;

		public VoidDispatchService() {
			running = true;
		}

		public void shutdown() {
			running = false;
		}

		public List<Runnable> shutdownNow() {
			running = false;
			return new ArrayList<Runnable>(0);
		}

		public boolean isShutdown() {
			return !running;
		}

		public boolean isTerminated() {
			return !running;
		}

		public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
			return true;
		}

		public void execute(Runnable r) {
			r.run();
		}
	}

	public void nativeKeyPressed(NativeKeyEvent e) {
		if (e.getKeyCode() == NativeKeyEvent.VC_B) {
			consumeEvent();
		}else
		if (e.getKeyCode() == NativeKeyEvent.VC_W) {
			callEventToNext();
		}
		System.out.println("key Pressedw`12	"+e.getKeyCode());
		setReserved(e);
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
	}

	public void nativeKeyTyped(NativeKeyEvent e) { /* Unimplemented */ }

	public static void main(String [] args) throws NativeHookException {
		new ConsumeEvent();
	}
}