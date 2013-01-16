package info.thinkingcloud.jersey.main.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.Perl5Pattern;

public class PagerObject {
	private String[] lines;

	private int lineCount = 30;

	private int current = 0;

	public PagerObject(String text) {
		String wrapped = WordUtils.wrap(text, 80);
		this.lines = wrapped.split("\n");
	}

	public int current() {
		return current;
	}

	public String grep(String regex) throws MalformedPatternException {
		StringBuilder sb = new StringBuilder();
		Perl5Compiler compiler = new Perl5Compiler();
		Perl5Pattern p = (Perl5Pattern) compiler.compile(regex);
		Perl5Matcher matcher = new Perl5Matcher();
		for (String line : lines) {
			if (matcher.matches(line, p) || line.contains(regex))
				sb.append(line).append("\n");
		}
		return sb.toString();
	}

	public String page(int page) {
		current = page;
		StringBuilder sb = new StringBuilder();
		int p = page;
		if (p < 0)
			p = 0;
		if (p > pages())
			p = pages() - 1;
		for (int i = p * lineCount; i <= (p + 1) * lineCount; i++) {
			if (i > lines())
				break;

			sb.append(lines[i]).append("\n");
		}
		return sb.toString();
	}

	public String prev() {
		if (current == 0)
			current = 1;
		return page(current - 1);
	}

	public String next() {
		if (current == pages()) {
			current = pages() - 1;
		}
		return page(current + 1);
	}

	public int pages() {
		return lines() % lineCount == 0 ? lines() / lineCount : lines() / lineCount + 1;
	}

	public int lines() {
		return lines.length;
	}

	public String dump() {
		return StringUtils.join(lines, "\n");
	}

	@Override
	public String toString() {
		return page(current);
	}
}
