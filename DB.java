package gavin;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;

public class DB {
	private static final Map<String, BoneCPDataSource> map = new HashMap<>();

	public static final void init(File dir) {
		if (dir == null || dir.listFiles() == null) return;
		for (File file : dir.listFiles()) {
			try {
				Properties props = new Properties();
				try (FileInputStream input = new FileInputStream(file)) {
					props.load(input);
				}
				if (props.containsKey("driverClass")) {
					Class.forName(props.getProperty("driverClass"));
				}
				BoneCPConfig cfg = new BoneCPConfig(props);
				BoneCPDataSource src = new BoneCPDataSource(cfg);
				map.put(file.getName(), src);
			} catch (Throwable e) {}
		}
	}

	public static final void destroy() {
		for (BoneCPDataSource src : map.values()) {
			src.close();
		}
	}

	public static final void keep() {
		for (BoneCPDataSource src : map.values()) {
			try (Connection conn = src.getConnection()) {
				try (Statement stat = conn.createStatement()) {
					try (ResultSet rs = stat.executeQuery("SELECT 0")) {
						rs.next();
					}
				}
			} catch (Throwable e) {}
		}
	}

	public static final DataSource src(String name) {
		return map.get(name);
	}

	public static final Connection conn(String name) {
		try {
			return src(name).getConnection();
		} catch (Throwable e) {
			return null;
		}
	}
}
