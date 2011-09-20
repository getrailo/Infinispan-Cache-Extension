package main;

import java.io.File;
import java.util.List;

/**
 * Runs Railo as a Java Application
 */
public class DebugAsJavaApplication_infinispan {

	public static void main(String[] args) throws Exception {
		System.setProperty("java.net.preferIPv4Stack", "true");
		File configFile = new File("./extension/src/main/debug.properties");
		System.out.println(configFile.exists());
		if(configFile.exists()) {
			DebugAsJavaApplication.main(new String[] { configFile.getAbsolutePath() });
		} else {
			System.out.println("Could not find config:" + configFile.getPath());
			System.exit(1);
		}
	}

}
