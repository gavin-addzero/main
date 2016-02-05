package q.util;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.lang3.StringUtils;

import q.tool.Min;

public class Web {
	public static final JspWriter wrap(HttpServletRequest request, JspWriter out, String name) {
		@SuppressWarnings("resource")
		$JspWriter agent = new $JspWriter(out.getBufferSize(), out.isAutoFlush()).original(out);
		if (request.getAttribute(name) == null) request.setAttribute(name, new StringBuilder());
		agent.buffer((StringBuilder) request.getAttribute(name));
		return agent;
	}

	public static final JspWriter js(HttpServletRequest request, JspWriter out) {
		return wrap(request, out, "script");
	}

	public static final JspWriter css(HttpServletRequest request, JspWriter out) {
		return wrap(request, out, "style");
	}

	public static final JspWriter wrap(JspWriter out) {
		if (!(out instanceof $JspWriter)) return out;
		$JspWriter x = ($JspWriter) out;
		return x.original;
	}

	public static final JspWriter js(JspWriter out) {
		if (!(out instanceof $JspWriter)) return out;
		$JspWriter x = ($JspWriter) out;
		Matcher m = Pattern.compile("(?mis)<script[^>]*>(.*?)</script>").matcher(x.buffer);
		if (m.find()) {
			StringBuilder s = new StringBuilder(), b = new StringBuilder();
			String g;
			do {
				g = StringUtils.trimToNull(m.group(1));
				if (g == null) {
					if (b.length() > 0) {
						Min.js(b, s.append("<script>")).append("</script>");
						b.setLength(0);
					}
					s.append(m.group());
				} else {
					b.append(g);
				}
			} while (m.find());
			if (b.length() > 0) Min.js(b, s.append("<script>")).append("</script>");
			x.buffer.setLength(0);
			if (StringUtils.isNotBlank(s)) x.buffer.append(s);
		}
		return x.original;
	}

	public static final JspWriter css(JspWriter out) {
		if (!(out instanceof $JspWriter)) return out;
		$JspWriter x = ($JspWriter) out;
		Matcher m = Pattern.compile("(?mis)<style[^>]*>(.*?)</style>").matcher(x.buffer);
		if (m.find()) {
			StringBuilder s = new StringBuilder(), b = new StringBuilder();
			String g;
			do {
				g = StringUtils.trimToNull(m.group(1));
				if (g == null) {
					if (b.length() > 0) {
						Min.css(b, s.append("<style>")).append("</style>");
						b.setLength(0);
					}
					s.append(m.group());
				} else {
					b.append(g);
				}
			} while (m.find());
			if (b.length() > 0) Min.css(b, s.append("<style>")).append("</style>");
			x.buffer.setLength(0);
			if (StringUtils.isNotBlank(s)) x.buffer.append(s);
		}
		return x.original;
	}

	private static class $JspWriter extends JspWriter {
		private JspWriter original;
		private StringBuilder buffer;

		public $JspWriter original(JspWriter original) {
			this.original = original;
			return this;
		}

		public $JspWriter buffer(StringBuilder buffer) {
			this.buffer = buffer;
			return this;
		}

		protected $JspWriter(int bufferSize, boolean autoFlush) {
			super(bufferSize, autoFlush);
		}

		@Override
		public void clear() throws IOException {}

		@Override
		public void clearBuffer() throws IOException {}

		@Override
		public int getRemaining() {
			return 0;
		}

		@Override
		public void newLine() throws IOException {
			buffer.append('\n');
		}

		@Override
		public void close() throws IOException {}

		@Override
		public void flush() throws IOException {}

		@Override
		public void print(boolean b) throws IOException {
			buffer.append(b);
		}

		@Override
		public void print(char c) throws IOException {
			buffer.append(c);
		}

		@Override
		public void print(int i) throws IOException {
			buffer.append(i);
		}

		@Override
		public void print(long l) throws IOException {
			buffer.append(l);
		}

		@Override
		public void print(float f) throws IOException {
			buffer.append(f);
		}

		@Override
		public void print(double d) throws IOException {
			buffer.append(d);
		}

		@Override
		public void print(char[] s) throws IOException {
			buffer.append(s);
		}

		@Override
		public void print(String s) throws IOException {
			print(s.toCharArray());
		}

		@Override
		public void print(Object o) throws IOException {
			buffer.append(o);
		}

		@Override
		public void println() throws IOException {
			buffer.append('\n');
		}

		@Override
		public void println(boolean b) throws IOException {
			buffer.append(b).append('\n');
		}

		@Override
		public void println(char c) throws IOException {
			buffer.append(c).append('\n');
		}

		@Override
		public void println(int i) throws IOException {
			buffer.append(i).append('\n');
		}

		@Override
		public void println(long l) throws IOException {
			buffer.append(l).append('\n');
		}

		@Override
		public void println(float f) throws IOException {
			buffer.append(f).append('\n');
		}

		@Override
		public void println(double d) throws IOException {
			buffer.append(d).append('\n');
		}

		@Override
		public void println(char[] s) throws IOException {
			buffer.append(s).append('\n');
		}

		@Override
		public void println(String s) throws IOException {
			buffer.append(s).append('\n');
		}

		@Override
		public void println(Object o) throws IOException {
			buffer.append(o).append('\n');
		}

		@Override
		public void write(char[] cbuf, int off, int len) throws IOException {
			buffer.append(cbuf, off, len);
		}
	}
}
