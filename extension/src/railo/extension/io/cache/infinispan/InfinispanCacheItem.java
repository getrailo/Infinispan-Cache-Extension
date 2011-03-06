package railo.extension.io.cache.infinispan;

import java.net.InetSocketAddress;
import java.util.List;

import net.spy.memcached.MemcachedClient;

public class InfinispanCacheItem {
	
	private Object value;
	private InfinispanCache cache;
	private String key;
	
	public InfinispanCacheItem(InfinispanCache cache, String key, Object obj) {
		this.cache = cache;
		this.value = obj;
		this.key = key;
	}

	
	public List<InetSocketAddress> getAddresses(){
		return this.cache.getAddresses();
	}

	public MemcachedClient getMc() {
		return this.cache.getMC();
	}
	
	public Object getValue() {
		return value;
	}
	
	public String getKey(){
		return key;
	}

}
