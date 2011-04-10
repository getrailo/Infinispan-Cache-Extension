package railo.extension.io.cache.infinispan;

import java.util.Properties;

import org.infinispan.Cache;
import org.infinispan.config.Configuration;
import org.infinispan.config.GlobalConfiguration;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.remoting.transport.jgroups.JGroupsTransport;

public class EmbeddedCacheInstance {
	private static EmbeddedCacheInstance instance = null;
	private static EmbeddedCacheManager manager = null;
	private static ClassLoader configCl;

	protected EmbeddedCacheInstance() {
		// Exists only to defeat instantiation.
	}

	public synchronized static EmbeddedCacheInstance getInstance(ClassLoader cl) {
		if (instance == null) {
			instance = new EmbeddedCacheInstance();
			ClassLoader ocl = Thread.currentThread().getContextClassLoader();
			configCl = cl;
			Thread.currentThread().setContextClassLoader(cl);
			GlobalConfiguration gc = GlobalConfiguration.getClusteredDefault();
			gc.setClusterName("demoCluster");
			// gc.setClusterName("demoCluster");
			gc.setTransportClass(JGroupsTransport.class.getName());
			// Load the jgroups properties
			Properties p = new Properties();
			gc.setTransportProperties(p);
			gc.setAllowDuplicateDomains(true);
			Configuration c = new Configuration();
			// Distributed cache mode
			c.setCacheMode(Configuration.CacheMode.DIST_SYNC);
			c.setExposeJmxStatistics(true);

			// turn functionality which returns the previous value when setting
			c.setUnsafeUnreliableReturnValues(true);

			// data will be distributed over 3 nodes
			c.setNumOwners(3);
			c.setL1CacheEnabled(true);

			// Allow batching
			c.setInvocationBatchingEnabled(true);
			c.setL1Lifespan(6000000);
			manager = new DefaultCacheManager(gc, c, false);
		    Thread.currentThread().setContextClassLoader(ocl);
		}
		return instance;
	}
	
	public Cache<Object, Object> getCache(String cachename) {
		ClassLoader ocl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(configCl);
		Cache<Object, Object> cache = manager.getCache(cachename);
	    Thread.currentThread().setContextClassLoader(ocl);
	    return cache;
	}
	public Cache<Object, Object> getCache() {
		ClassLoader ocl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(configCl);
		Cache<Object, Object> cache = manager.getCache();
		Thread.currentThread().setContextClassLoader(ocl);
		return cache;
	}
}