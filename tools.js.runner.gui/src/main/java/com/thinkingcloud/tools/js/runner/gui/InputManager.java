package com.thinkingcloud.tools.js.runner.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputManager extends KeyAdapter {
	private JConsole console;

	public InputManager(JConsole console) {
		this.console = console;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.isActionKey())
			console.moveCursor(e.getKeyChar());
		else
			console.write(e.getKeyChar());
	}
}
