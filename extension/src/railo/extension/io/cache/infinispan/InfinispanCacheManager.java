package railo.extension.io.cache.infinispan;

import java.io.IOException;
import java.util.Properties;

import org.infinispan.config.Configuration;
import org.infinispan.config.GlobalConfiguration;
import org.infinispan.manager.DefaultCacheManager;

public class InfinispanCacheManager extends DefaultCacheManager {

	private static DefaultCacheManager manager;
	
	public static DefaultCacheManager getInstance(Properties properties) {
		if(manager == null) {

/*
			GlobalConfiguration globalConfig = new GlobalConfiguration().fluent()
			  .transport()
			  .transportClass(org.infinispan.remoting.transport.jgroups.JGroupsTransport.class)
			    .clusterName("qa-cluster")
//			    .addProperty("configurationFile", "jgroups-tcp.xml")
			    .addProperty("configurationFile", "jgroups-udp.xml")
			    .machineId("qa-machine").rackId("qa-rack")
			  .build();
			
			Configuration config = new Configuration().fluent()
			  .clustering()
			    .mode(Configuration.CacheMode.DIST_SYNC)
			    .sync()
			    .l1().lifespan(25000L)
			    .hash().numOwners(3)
			  .build();
*/
			try {
				manager = new DefaultCacheManager(properties.getProperty("infinispan.config.file").toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return manager;
	}
	
}
