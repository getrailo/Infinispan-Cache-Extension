package main;

import java.io.File;
import java.util.List;

/**
 * Runs Railo as a Java Application
 */
public class DebugJavaApplication {

	public static void main(String[] args) throws Exception {
		System.setProperty("java.net.preferIPv4Stack", "true");
		DebugAsJavaApplication jab = new DebugAsJavaApplication();
		DebugAsJavaApplication.main(new String[] { new File("./extension/src/main/debug.properties").getAbsolutePath() });
	}

}
