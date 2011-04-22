package railo.extension.io.cache.infinispan;

import java.util.Properties;

import main.ClassLoaderUtils;

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
			System.out.println(ClassLoaderUtils.showClassLoaderHierarchy(ocl.getParent()));
			System.out.println(ClassLoaderUtils.showClassLoaderHierarchy(ocl));
			System.out.println(ClassLoaderUtils.showClassLoaderHierarchy(cl.getParent()));
			System.out.println(ClassLoaderUtils.showClassLoaderHierarchy(cl));
			System.out.println("----------------------");
			Thread.currentThread().setContextClassLoader(cl);
			GlobalConfiguration gc = GlobalConfiguration.getClusteredDefault();
			gc.setClusterName("demoCluster");

			// gc.setClusterName("demoCluster");
			gc.setTransportClass(JGroupsTransport.class.getName());
			// Load the jgroups properties
			Properties p = new Properties();
			p.setProperty("configurationFile", "jgroups-udp.xml");
			gc.setTransportProperties(p);
			gc.setAllowDuplicateDomains(false);
			Configuration c = new Configuration();
			// Distributed cache mode
			c.setCacheMode(Configuration.CacheMode.DIST_SYNC);
			c.setExposeJmxStatistics(true);
			c.setUseLazyDeserialization(true);

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

//	Albuquerque, NM to Boston, MA
//	Sunday, May 1, 2011
//	Depart Albuquerque, NM (ABQ) 		1:50 PM
//	Arrive in Boston Logan, MA (BOS) 	11:25 PM
//	
//	Boston, MA to Albuquerque, NM
//	Wednesday, May 4, 2011
//	Depart Boston Logan, MA (BOS)		1:45 PM
//	Arrive in Albuquerque, NM (ABQ) 	6:45 PM

	public Cache<Object, Object> getCache(String cachename) {
		ClassLoader ocl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(configCl);
		Cache cache = manager.getCache(cachename);
		if (!cache.getStatus().allowInvocations()) {
			cache.start();
		}
		Cache cache2 = new ClassLoaderAwareCache(cache.getAdvancedCache(), Thread.currentThread().getContextClassLoader()); 
		Thread.currentThread().setContextClassLoader(ocl);
		return cache2;
		//return cache;
	}

}