package q.util;

import it.sauronsoftware.cron4j.Scheduler;

import java.io.File;

public class Crontab {
	private static Scheduler scheduler = null;

	public static final void init(File dir) {
		if (dir == null || dir.listFiles() == null) return;
		scheduler = new Scheduler();
		scheduler.setDaemon(true);
		for (File file : dir.listFiles()) {
			scheduler.scheduleFile(file);
		}
		scheduler.start();
	}

	public static final void destroy() {
		if (scheduler != null) scheduler.stop();
	}
}
