package main;

import org.jboss.modules.Main;

/**
 * Runs Railo as a Java Application
 */
public class DebugJBossApplication {

	public static void main(String[] args) throws Exception {
		String jbossDir = "./build/jboss7.0.0.Beta3-SNAPSHOT"; 
		System.setProperty("java.net.preferIPv4Stack", "true");
		System.setProperty("org.jboss.boot.log.file", jbossDir + "/standalone/log/boot.log");
		System.setProperty("logging.configuration", "file:"+jbossDir+"/standalone/configuration/logging.properties");
		System.setProperty("jboss.server.home.url", "file://"+jbossDir);
		System.setProperty("jboss.home.dir", jbossDir);
		String argline="-mp "+jbossDir+"/modules -logmodule org.jboss.logmanager -jaxpmodule javax.xml.jaxp-provider org.jboss.as.standalone -Djboss.home.dir="+jbossDir;
		try {
			org.jboss.modules.Main.main(argline.split(" "));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

