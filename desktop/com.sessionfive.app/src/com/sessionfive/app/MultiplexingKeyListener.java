package com.sessionfive.app;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class MultiplexingKeyListener implements KeyListener {
	
	private List<KeyListener> listeners;
	
	public MultiplexingKeyListener() {
		listeners = new ArrayList<KeyListener>();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		for (KeyListener listener : listeners) {
			listener.keyPressed(e);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		for (KeyListener listener : listeners) {
			listener.keyReleased(e);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		for (KeyListener listener : listeners) {
			listener.keyTyped(e);
		}
	}
	
	public void addKeyListener(KeyListener listener) {
		listeners.add(listener);
	}
	
	public void removeKeyListener(KeyListener listener) {
		listeners.remove(listener);
	}

}
