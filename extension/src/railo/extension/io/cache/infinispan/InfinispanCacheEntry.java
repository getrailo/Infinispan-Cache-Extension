package railo.extension.io.cache.infinispan;

import java.net.SocketAddress;
import java.util.Date;
import java.util.Map;

import net.spy.memcached.MemcachedClient;

import railo.commons.io.cache.CacheEntry;
import railo.loader.engine.CFMLEngine;
import railo.loader.engine.CFMLEngineFactory;
import railo.runtime.exp.PageException;
import railo.runtime.type.Struct;
import railo.runtime.util.Cast;

public class InfinispanCacheEntry implements CacheEntry {
	
	private InfinispanCacheItem item;
	
	public InfinispanCacheEntry(InfinispanCacheItem item) {
		this.item = item;	
	}

	@Override
	public Date created() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Struct getCustomInfo() {
		CFMLEngine engine = CFMLEngineFactory.getInstance();
		Cast caster = engine.getCastUtil();		
		Struct res = null;
		try{
			res = caster.toStruct(this.item.getMc().getStats(this.item.getKey()).get(this.item.getAddresses().get(0)));
		}catch(PageException e){
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public String getKey() {
		return this.item.getKey();
	}

	@Override
	public Object getValue() {
		return this.item.getValue();
	}

	@Override
	public int hitCount() {
		int hits = 0;
		
		MemcachedClient mc = this.item.getMc();
		Map<SocketAddress,Map<String,String>> stats = mc.getStats(this.item.getKey());
		SocketAddress add = this.item.getAddresses().get(0);
	
		String hitsValue = stats.get(add).get("get_hits");
		
		if(hitsValue != null){
		    hits = Integer.parseInt(hitsValue);			
		}
		return hits;
	}

	@Override
	public long idleTimeSpan() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Date lastHit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date lastModified() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long liveTimeSpan() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long size() {
		// TODO Auto-generated method stub
		return 0;
	}

}
