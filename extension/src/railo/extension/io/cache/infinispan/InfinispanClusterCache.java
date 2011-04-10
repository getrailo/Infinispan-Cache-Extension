package railo.extension.io.cache.infinispan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.infinispan.config.Configuration;
import org.infinispan.config.GlobalConfiguration;
import org.infinispan.manager.CacheContainer;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.remoting.transport.jgroups.JGroupsTransport;

import railo.commons.io.cache.Cache;
import railo.commons.io.cache.CacheEntry;
import railo.commons.io.cache.CacheEntryFilter;
import railo.commons.io.cache.CacheKeyFilter;
import railo.commons.io.cache.exp.CacheException;
import railo.extension.io.cache.CacheUtil;
import railo.extension.io.cache.util.CacheKeyFilterAll;
import railo.loader.engine.CFMLEngineFactory;
import railo.runtime.config.Config;
import railo.runtime.type.Struct;
import railo.runtime.util.Cast;


public class InfinispanClusterCache implements Cache {

	org.infinispan.Cache<Object, Object> cache = null;
	private int hits;
	private int misses;

	public static void init(Config config, String[] cacheNames, Struct[] arguments) throws IOException {
	}

	/**
	 * @throws IOException
	 * @see railo.commons.io.cache.Cache#init(java.lang.String, railo.runtime.type.Struct)
	 * @deprecated use instead init(Config config,String cacheName,Struct arguments)
	 */
	public void init(String cacheName, Struct arguments) throws IOException {
		throw new IOException("do not use this init method, use instead init(Config config,String cacheName,Struct arguments)");
		// no longer used
	}

	public void init(Config config, String cacheName, Struct arguments) throws IOException {
		ClassLoader cl = config.getClassLoader();
		// debug
		/*
		Key[] keys = arguments.keys();
		for(int i=0;i<keys.length;i++){
			System.out.println(keys[i]+":"+arguments.get(keys[i],""));
		}
		*/
		/*
		Cast cast = CFMLEngineFactory.getInstance().getCastUtil();
		try {
			String[] servers;
			String strServers = cast.toString(arguments.get("servers"), null);
			// backward comaptibility
			if (strServers == null) {
				String host = cast.toString(arguments.get("host"));
				int port = cast.toIntValue(arguments.get("port"));
				servers = new String[] { host + ":" + port };
			} else {
				strServers = strServers.trim();
				servers = strServers.split("\\s+");
				for (int i = 0; i < servers.length; i++) {
					servers[i] = servers[i].trim();
				}
			}

			// settings
			int initConn = cast.toIntValue(arguments.get("initial_connections", 1), 1);
			if (initConn > 0)
				pool.setInitConn(initConn);

			int minConn = cast.toIntValue(arguments.get("min_spare_connections", 1), 1);
			if (minConn > 0)
				pool.setMinConn(minConn);

			int maxConn = cast.toIntValue(arguments.get("max_spare_connections", 32), 32);
			if (maxConn > 0)
				pool.setMaxConn(maxConn);

			int maxIdle = cast.toIntValue(arguments.get("max_idle_time", 5), 5);
			if (maxIdle > 0)
				pool.setMaxIdle(maxIdle * 1000L);

			int maxBusy = cast.toIntValue(arguments.get("max_busy_time", 30), 30);
			if (maxBusy > 0)
				pool.setMaxBusyTime(maxBusy * 1000L);

			int maintSleep = cast.toIntValue(arguments.get("maint_thread_sleep", 5), 5);
			if (maintSleep > 0)
				pool.setMaintSleep(maintSleep * 1000L);

			int socketTO = cast.toIntValue(arguments.get("socket_timeout", 3), 3);
			if (socketTO > 0)
				pool.setSocketTO(socketTO * 1000);

			int socketConnTO = cast.toIntValue(arguments.get("socket_connect_to", 3), 3);
			if (socketConnTO > 0)
				pool.setSocketConnectTO(socketConnTO * 1000);

			pool.setFailover(cast.toBooleanValue(arguments.get("failover", false), false));
			pool.setFailback(cast.toBooleanValue(arguments.get("failback", false), false));
			pool.setNagle(cast.toBooleanValue(arguments.get("nagle_alg", false), false));
			pool.setAliveCheck(cast.toBooleanValue(arguments.get("alive_check", false), false));

			int bufferSize = cast.toIntValue(arguments.get("buffer_size", false));
			if (bufferSize > 0)
				pool.setBufferSize(bufferSize * 1024 * 1024);

			// pool.setHashingAlg(SockIOPool.NEW_COMPAT_HASH);

			pool.setServers(servers);
			pool.initialize();
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
		*/
		
		//EmbeddedCacheManager manager = new DefaultCacheManager();
		cache = EmbeddedCacheInstance.getInstance(cl).getCache(cacheName);
	}

	/**
	 * @see railo.commons.io.cache.Cache#contains(java.lang.String)
	 */
	public boolean contains(String key) {
		return cache.containsKey(key);
	}

	/**
	 * @see railo.commons.io.cache.Cache#getCustomInfo()
	 */
	public Struct getCustomInfo() {
		Struct info = CacheUtil.getInfo(this);
		return info;
	}

	/**
	 * @see railo.commons.io.cache.Cache#getCacheEntry(java.lang.String)
	 */
	public CacheEntry getCacheEntry(String key) throws CacheException {
		Object value = cache.get(key);
		if (value == null) {
			misses++;
			throw new CacheException("there is no entry with key [" + key + "] in cache");
		}
		hits++;
		return new InfinispanCacheEntry(key, value, null, null);
	}

	/**
	 * @see railo.commons.io.cache.Cache#getCacheEntry(java.lang.String, railo.commons.io.cache.CacheEntry)
	 */
	public CacheEntry getCacheEntry(String key, CacheEntry defaultValue) {
		Object value = cache.get(key);
		if (value == null) {
			misses++;
			return defaultValue;
		}
		hits++;
		return new InfinispanCacheEntry(key, value, null, null);
	}

	/**
	 * @see railo.commons.io.cache.Cache#getValue(java.lang.String)
	 */
	public Object getValue(String key) throws CacheException {
		Object value = cache.get(key);
		if (value == null) {
			misses++;
			throw new CacheException("there is no entry with key [" + key + "] in cache");
		}
		hits++;
		return value;
	}

	/**
	 * @see railo.commons.io.cache.Cache#getValue(java.lang.String, java.lang.Object)
	 */
	public Object getValue(String key, Object defaultValue) {
		Object value = cache.get(key);
		if (value == null) {
			misses++;
			return defaultValue;
		}
		hits++;
		return value;
	}

	/**
	 * @see railo.commons.io.cache.Cache#hitCount()
	 */
	public long hitCount() {
		return hits;
	}

	/**
	 * @see railo.commons.io.cache.Cache#missCount()
	 */
	public long missCount() {
		return misses;
	}

	/**
	 * @see railo.commons.io.cache.Cache#put(java.lang.String, java.lang.Object, java.lang.Long, java.lang.Long)
	 */
	public void put(String key, Object value, Long idleTime, Long until) {
		if (until == null) {
			cache.put(key, value);
		} else {
			cache.put(key, value, until.longValue() + System.currentTimeMillis(), TimeUnit.MILLISECONDS);
		}

	}

	/**
	 * @see railo.commons.io.cache.Cache#remove(java.lang.String)
	 */
	public boolean remove(String key) {
		if (cache.containsKey(key)) {
			cache.remove(key);
			return true;
		} else
			return false;

	}

	// not supported

	/**
	 * @see railo.commons.io.cache.Cache#remove(railo.commons.io.cache.CacheKeyFilter)
	 */
	public int remove(CacheKeyFilter filter) {
		if (CacheKeyFilterAll.equalTo(filter)) {
			cache.clear();
			return -1;
		}
		throw notSupported("remove:key filter");
	}

	public void clear() {
		cache.clear();
	}

	/**
	 * @see railo.commons.io.cache.Cache#remove(railo.commons.io.cache.CacheEntryFilter)
	 */
	public int remove(CacheEntryFilter filter) {
		throw notSupported("remove:entry filter");
	}

	/**
	 * @see railo.commons.io.cache.Cache#values()
	 */
	public List values() {
		throw notSupported("values");
	}

	/**
	 * @see railo.commons.io.cache.Cache#values(railo.commons.io.cache.CacheKeyFilter)
	 */
	public List values(CacheKeyFilter filter) {
		throw notSupported("entries:key filter");
	}

	/**
	 * @see railo.commons.io.cache.Cache#values(railo.commons.io.cache.CacheEntryFilter)
	 */
	public List values(CacheEntryFilter filter) {
		throw notSupported("values:entry filter");
	}

	/**
	 * @see railo.commons.io.cache.Cache#entries()
	 */
	public List entries() {
		List keys = keys();
		List list=new ArrayList();
		Iterator it = keys.iterator();
		String key;
		while(it.hasNext()){
			key=(String) it.next();
			list.add(cache.get(key));
		}
		return list;
	}

	/**
	 * @see railo.commons.io.cache.Cache#entries(railo.commons.io.cache.CacheKeyFilter)
	 */
	public List entries(CacheKeyFilter filter) {
		List keys = keys();
		List list=new ArrayList();
		Iterator it = keys.iterator();
		String key;
		while(it.hasNext()){
			key=(String) it.next();
			if(filter.accept(key))list.add(getQuiet(key,null));
		}
		return list;
	}

	/**
	 * @see railo.commons.io.cache.Cache#entries(railo.commons.io.cache.CacheEntryFilter)
	 */
	public List entries(CacheEntryFilter filter) {
		List keys = keys();
		List list=new ArrayList();
		Iterator it = keys.iterator();
		String key;
		CacheEntry entry;
		while(it.hasNext()){
			key=(String) it.next();
			entry=getQuiet(key,null);
			if(filter.accept(entry))list.add(entry);
		}
		return list;
	}

	/**
	 * @see railo.commons.io.cache.Cache#keys()
	 */
	public List keys() {
		List<Object> list = new ArrayList(cache.keySet());
		return list;
	}

	/**
	 * @see railo.commons.io.cache.Cache#keys(railo.commons.io.cache.CacheKeyFilter)
	 */
	public List keys(CacheKeyFilter filter) {
		List keys = keys();
		List list=new ArrayList();
		Iterator it = keys.iterator();
		String key;
		while(it.hasNext()){
			key=(String) it.next();
			if(filter.accept(key))list.add(key);
		}
		return list;
	}

	/**
	 * @see railo.commons.io.cache.Cache#keys(railo.commons.io.cache.CacheEntryFilter)
	 */
	public List keys(CacheEntryFilter filter) {
		throw notSupported("keys: entry filter");
	}

	private RuntimeException notSupported(String feature) {
		return new RuntimeException("this feature [" + feature + "] is not supported by infinispan");
	}


	public CacheEntry getQuiet(String key, CacheEntry defaultValue){
		try {
			return getCacheEntry(key);
		} catch (IOException e) {
			return defaultValue;
		}
	}
	
}
