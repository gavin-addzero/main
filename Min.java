package q.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CharSequenceReader;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import q.comm.AppendableWriter;

import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

public class Min {

	public static <T extends Appendable> T js(CharSequence input, T output) {
		js(new CharSequenceReader(input), new AppendableWriter(output));
		return output;
	}

	public static <T extends Appendable> T css(CharSequence input, T output) {
		css(new CharSequenceReader(input), new AppendableWriter(output));
		return output;
	}

	public static <T extends Writer> T js(Reader in, T out) {
		try {
			JavaScriptCompressor compressor = new JavaScriptCompressor(in, REPORTER);
			compressor.compress(out, -1, true, false, false, false);
		} catch (Throwable e) {}
		return out;
	}

	public static <T extends Writer> T css(Reader in, T out) {
		try {
			CssCompressor compressor = new CssCompressor(in);
			compressor.compress(out, -1);
		} catch (Throwable e) {}
		return out;
	}

	private static final ErrorReporter REPORTER = new ErrorReporter() {
		public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {}

		public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {}

		public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
			return null;
		}
	};

	private File to = null;

	public void setTo(File dst) {
		this.to = dst;
	}

	private ArrayList<Src> srcs = new ArrayList<>();

	public Src createSrc() {
		Src src = new Src();
		srcs.add(src);
		return src;
	}

	public void execute() throws IOException {
		if (to == null || srcs.size() == 0) return;
		if (to.exists()) {
			boolean go = false;
			long lastModified = to.lastModified();
			for (Src src : srcs) {
				if (src.from == null) continue;
				if (src.from.lastModified() > lastModified) {
					go = true;
					break;
				}
			}
			if (!go) return;
		}
		File tmp = File.createTempFile("min.", ".tmp");
		try (FileOutputStream output = new FileOutputStream(tmp)) {
			for (Src src : srcs) {
				if (src.from == null) continue;
				try (FileInputStream input = new FileInputStream(src.from)) {
					IOUtils.copy(input, output);
				}
				output.flush();
			}
		}
		if (!to.getParentFile().exists()) to.getParentFile().mkdirs();
		try (FileReader reader = new FileReader(tmp)) {
			try (FileWriter writer = new FileWriter(to)) {
				if (this.to.getName().endsWith(".js")) {
					js(reader, writer);
				} else {
					css(reader, writer);
				}
			}
		}
		System.out.println(this.to);
		tmp.delete();
	}

	public static final class Src {
		private File from;

		public void setFrom(File from) {
			this.from = from;
		}
	}
}
