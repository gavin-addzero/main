package q.util;

import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class Str {
	public static final CharSequence replace(CharSequence input, String regex, String replacement) {
		Matcher matcher = Pattern.compile(regex).matcher(input);
		if (matcher.find()) {
			StringBuffer buffer = new StringBuffer(input.length() >>> 1);
			do {
				matcher.appendReplacement(buffer, replacement);
			} while (matcher.find());
			return matcher.appendTail(buffer);
		}
		return input;
	}

	private static final byte[] uuid = new byte[10];
	private static long last = -1;

	private static final byte[] uuid() {
		long time = System.currentTimeMillis();
		if (time - last > 60000) {
			ThreadLocalRandom.current().nextBytes(uuid);
			uuid[6] &= 0x0f;
			uuid[8] |= 0x80;
			last = time;
		}
		byte[] bytes = new byte[16];
		bytes[0] = (byte) ((time >>> 8) & 0xff);
		bytes[1] = (byte) (time & 0xff);
		time = System.nanoTime();
		bytes[2] = (byte) (time & 0xff);
		bytes[3] = (byte) ((time >>> 8) & 0xff);
		bytes[4] = (byte) (ThreadLocalRandom.current().nextInt() & 0xff);
		bytes[5] = (byte) (ThreadLocalRandom.current().nextInt() & 0xff);
		System.arraycopy(uuid, 0, bytes, 6, uuid.length);
		return bytes;
	}

	public static final String uid() {
		return Hex.encodeHexString(uuid());
	}

	public static final String uid64() {
		return Base64.encodeBase64URLSafeString(uuid());
	}

	public static final char[] rand(int len) {
		return rand(new char[len]);
	}

	public static final char[] rand(char[] c) {
		for (int i = 0, j, l = c.length; i < l; i++) {
			c[i] = (char) ((j = ThreadLocalRandom.current().nextInt(62)) + (j > 35 ? 61 : j > 9 ? 55 : 48));
		}
		return c;
	}

	public static void main(String[] args) {
		TreeSet<String> set = new TreeSet<>();
		String tmp;
		for (int i = 0; i < 10; i++) {
			if (set.contains(tmp = uid())) {
				System.err.println(tmp);
				System.exit(0);
			}
			set.add(tmp);
			System.out.println(tmp);
		}
		for (int i = 0; i < 10; i++) {
			if (set.contains(tmp = uid64())) {
				System.err.println(tmp);
				System.exit(0);
			}
			set.add(tmp);
			System.out.println(tmp);
		}
	}
}
