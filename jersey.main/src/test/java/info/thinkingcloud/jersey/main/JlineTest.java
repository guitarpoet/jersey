package info.thinkingcloud.jersey.main;

import java.io.IOException;

import jline.console.ConsoleReader;
import jline.console.completer.FileNameCompleter;

public class JlineTest {
	public static void main(String[] args) throws IOException {
		ConsoleReader reader = new ConsoleReader();
		reader.addCompleter(new FileNameCompleter());
		reader.setBellEnabled(true);
		String s = null;
		while (!(s = reader.readLine(System.getProperty("user.name") + ">")).equals("exit")) {
			System.out.println(s);
		}
	}
}