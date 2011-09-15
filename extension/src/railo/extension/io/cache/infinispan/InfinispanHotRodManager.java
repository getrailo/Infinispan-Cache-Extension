package railo.extension.io.cache.infinispan;

import java.util.Properties;

import org.infinispan.client.hotrod.RemoteCacheManager;

public class InfinispanHotRodManager extends RemoteCacheManager {

	private static RemoteCacheManager manager;
	
	public static RemoteCacheManager getInstance(Properties properties) {
		if(manager == null) {
			manager = new RemoteCacheManager(properties);
			manager.start();			
		}
		return manager;
	}
	
}
