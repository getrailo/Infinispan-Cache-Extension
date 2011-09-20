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
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.VersionedValue;

import railo.commons.io.cache.Cache;
import railo.commons.io.cache.CacheEntry;
import railo.commons.io.cache.CacheEntryFilter;
import railo.commons.io.cache.CacheKeyFilter;
import railo.commons.io.cache.exp.CacheException;
import railo.extension.io.cache.CacheUtil;
import railo.extension.io.cache.util.CacheKeyFilterAll;
import railo.loader.engine.CFMLEngineFactory;
import railo.runtime.config.Config;
import railo.runtime.exp.PageException;
import railo.runtime.type.Struct;
import railo.runtime.util.Cast;

public class InfinispanHotRodClientCache implements Cache {

	private int hits;
	private int misses;
	private ClassLoader classLoader;
	private String cacheName;
	private RemoteCacheManager manager;

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
		Cast caster = CFMLEngineFactory.getInstance().getCastUtil();
		this.classLoader = config.getClassLoader();
		this.cacheName = cacheName;
		setClassLoader();
		Iterator propNames = arguments.keyIterator();
		Properties properties = new Properties();
		try {
			while (propNames.hasNext()) {
				String propName = caster.toString(propNames.next());
				properties.setProperty(propName, caster.toString(arguments.get(propName)));
			}
		} catch (PageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		manager = InfinispanHotRodManager.getInstance(properties);
		/*
				infinispan.client.hotrod.request_balancing_strategy, default = org.infinispan.client.hotrod.impl.transport.tcp.RoundRobinBalancingStrategy. For replicated (vs distributed) Hot Rod server clusters, the client balances requests to the servers according to this strategy.
				infinispan.client.hotrod.server_list, default = 127.0.0.1:11311. This is the initial list of Hot Rod servers to connect to, specified in the following format: host1:port1;host2:port2... At least one host:port must be specified.
				infinispan.client.hotrod.force_return_values, default = false. Whether or not to implicitly Flag.FORCE_RETURN_VALUE for all calls.
				infinispan.client.hotrod.tcp_no_delay, default = true. Affects TCP NODELAY on the TCP stack.
				infinispan.client.hotrod.ping_on_startup, default = true. If true, a ping request is sent to a back end server in order to fetch cluster's topology.
				infinispan.client.hotrod.transport_factory, default = org.infinispan.client.hotrod.impl.transport.tcp.TcpTransportFactory - controls which transport to use. Currently only the TcpTransport is supported.
				infinispan.client.hotrod.marshaller, default = org.infinispan.marshall.jboss.GenericJBossMarshaller. Allows you to specify a custom Marshaller implementation to serialize and deserialize user objects.
				infinispan.client.hotrod.async_executor_factory, default = org.infinispan.client.hotrod.impl.async.DefaultAsyncExecutorFactory. Allows you to specify a custom asynchroous executor for async calls.
				infinispan.client.hotrod.default_executor_factory.pool_size, default = 10. If the default executor is used, this configures the number of threads to initialize the executor with.
				infinispan.client.hotrod.default_executor_factory.queue_size, default = 100000. If the default executor is used, this configures the queue size to initialize the executor with.
				infinispan.client.hotrod.hash_function_impl.1, default = org.infinispan.client.hotrod.impl.consistenthash.ConsistentHashV1. This specifies the version of the hash function and consistent hash algorithm in use, and is closely tied with the HotRod server version used.
				infinispan.client.hotrod.key_size_estimate, default = 64. This hint allows sizing of byte buffers when serializing and deserializing keys, to minimize array resizing.
				infinispan.client.hotrod.value_size_estimate, default = 512. This hint allows sizing of byte buffers when serializing and deserializing values, to minimize array resizing.
		*/

	}

	private void setClassLoader() {
		if (classLoader != Thread.currentThread().getContextClassLoader())
			Thread.currentThread().setContextClassLoader(classLoader);
	}

	private RemoteCache<Object, Object> getCache() {
		setClassLoader();
		return manager.getCache(cacheName);
	}

	/**
	 * @see railo.commons.io.cache.Cache#contains(java.lang.String)
	 */
	public boolean contains(String key) {
		return getCache().containsKey(key);
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
		Object value = getCache().get(key);
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
		Object value = getCache().get(key);
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
		Object value = getCache().get(key);
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
		Object value = getCache().get(key);
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
			getCache().put(key, value);
		} else {
			getCache().put(key, value, until.longValue() + System.currentTimeMillis(), TimeUnit.MILLISECONDS);
		}

	}

	/**
	 * @see railo.commons.io.cache.Cache#remove(java.lang.String)
	 */
	public boolean remove(String key) {
		if (getCache().containsKey(key)) {
			VersionedValue ve = getCache().getVersioned(key);
			getCache().removeWithVersion(key, ve.getVersion());
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
			getCache().clear();
			return -1;
		}
		throw notSupported("remove:key filter");
	}

	public void clear() {
		getCache().clear();
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
		List list = new ArrayList();
		Iterator it = keys.iterator();
		String key;
		while (it.hasNext()) {
			key = (String) it.next();
			list.add(getCache().get(key));
		}
		return list;
	}

	/**
	 * @see railo.commons.io.cache.Cache#entries(railo.commons.io.cache.CacheKeyFilter)
	 */
	public List entries(CacheKeyFilter filter) {
		List keys = keys();
		List list = new ArrayList();
		Iterator it = keys.iterator();
		String key;
		while (it.hasNext()) {
			key = (String) it.next();
			if (filter.accept(key))
				list.add(getQuiet(key, null));
		}
		return list;
	}

	/**
	 * @see railo.commons.io.cache.Cache#entries(railo.commons.io.cache.CacheEntryFilter)
	 */
	public List entries(CacheEntryFilter filter) {
		List keys = keys();
		List list = new ArrayList();
		Iterator it = keys.iterator();
		String key;
		CacheEntry entry;
		while (it.hasNext()) {
			key = (String) it.next();
			entry = getQuiet(key, null);
			if (filter.accept(entry))
				list.add(entry);
		}
		return list;
	}

	/**
	 * @see railo.commons.io.cache.Cache#keys()
	 */
	public List keys() {
		List<Object> list = new ArrayList(getCache().keySet());
		return list;
	}

	/**
	 * @see railo.commons.io.cache.Cache#keys(railo.commons.io.cache.CacheKeyFilter)
	 */
	public List keys(CacheKeyFilter filter) {
		List keys = keys();
		List list = new ArrayList();
		Iterator it = keys.iterator();
		String key;
		while (it.hasNext()) {
			key = (String) it.next();
			if (filter.accept(key))
				list.add(key);
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

	public CacheEntry getQuiet(String key, CacheEntry defaultValue) {
		try {
			return getCacheEntry(key);
		} catch (IOException e) {
			return defaultValue;
		}
	}

}
