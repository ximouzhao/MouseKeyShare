package xyz.ximou.mousekeyshare.example;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.jnativehook.mouse.NativeMouseListener;
import org.jnativehook.mouse.NativeMouseMotionListener;

import java.awt.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsumeEvent implements NativeKeyListener, NativeMouseInputListener {

	Field f=null;
	{
		try {
			f = NativeInputEvent.class.getDeclaredField("reserved");
			f.setAccessible(true);
		}
		catch (Exception ex) {
			System.out.print("[ !! ]\n");
			ex.printStackTrace();
		}
	}
	short reserved=0x00;
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
		System.out.println("mouse moved"+nativeMouseEvent.getPoint());
		setReserved(nativeMouseEvent);
	}

	@Override
	public void nativeMouseDragged(NativeMouseEvent nativeMouseEvent) {
		setReserved(nativeMouseEvent);
	}

	public void consumeEvent(NativeKeyEvent e){
		reserved=0x01;
	}
	public void callEventToNext(NativeKeyEvent e){
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
			consumeEvent(e);
		}else
		if (e.getKeyCode() == NativeKeyEvent.VC_W) {
			callEventToNext(e);
		}
		setReserved(e);
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
	}

	public void nativeKeyTyped(NativeKeyEvent e) { /* Unimplemented */ }

	public static void main(String [] args) throws NativeHookException {
		new ConsumeEvent();
	}
}