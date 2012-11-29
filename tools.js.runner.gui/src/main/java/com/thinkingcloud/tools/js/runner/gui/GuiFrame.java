package com.thinkingcloud.tools.js.runner.gui;

import javax.annotation.PostConstruct;
import javax.swing.JFrame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("guiFrame")
public class GuiFrame extends JFrame {

	private static final long serialVersionUID = 4531563910793413366L;

	@Autowired
	private JConsole console;

	@Value("${gui.title}")
	private String defaultTitle;

	@PostConstruct
	public void init() {
		getContentPane().add(console);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		addKeyListener(new InputManager(console));
		setVisible(true);
	}
}
