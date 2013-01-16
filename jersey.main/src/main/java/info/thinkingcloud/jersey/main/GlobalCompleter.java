package info.thinkingcloud.jersey.main;

import info.thinkingcloud.jersey.core.NewGlobal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import jline.console.completer.Completer;

@Service
public class GlobalCompleter implements Completer {

	@Autowired
	private NewGlobal global;

	@Override
	public int complete(String buffer, int cursor, List<CharSequence> candidates) {
		for (Object obj : global.getAllIds()) {
			String name = String.valueOf(obj);
			if (name.startsWith(buffer)) {
				candidates.add(name);
			}
		}
		return 0;
	}
}
