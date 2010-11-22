package railo.extension.io.cache.infinispan;

import org.infinispan.manager.DefaultCacheManager;

public final class CacheManagerFactory {
	
	private static DefaultCacheManager _dcm;
	
	public static DefaultCacheManager getDefaultCacheManager() {
		if(_dcm == null){
			_dcm = new DefaultCacheManager();
		}
		return _dcm;
	}
	
}
