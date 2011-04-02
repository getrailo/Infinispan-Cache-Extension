package railo.extension.io.cache.infinispan;

import java.io.Serializable;
import java.util.Date;

import railo.commons.io.cache.CacheEntry;
import railo.extension.io.cache.CacheUtil;
import railo.loader.engine.CFMLEngineFactory;
import railo.runtime.type.Struct;

public class InfinispanCacheEntry implements CacheEntry,Serializable {

	private String key;
	private Object value;
	private long created;
	private Long idleTime;
	private Long until;

	public InfinispanCacheEntry(String key, Object value, Long idleTime, Long until) {
		this.key=key;
		this.value=value;
		this.idleTime=idleTime;
		this.until=until;
		this.created=System.currentTimeMillis();
	}

	/**
	 * @see railo.commons.io.cache.CacheEntry#created()
	 */
	public Date created() {
		return CFMLEngineFactory.getInstance().getCreationUtil().createDate(created);
	}

	/**
	 * @see railo.commons.io.cache.CacheEntry#getKey()
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @see railo.commons.io.cache.CacheEntry#getValue()
	 */
	public Object getValue() {
		return value;
	}

	public int hitCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long idleTimeSpan() {
		if(idleTime==null)return Long.MIN_VALUE;
		return idleTime.longValue();
	}

	public Date lastHit() {
		// TODO Auto-generated method stub
		return null;
	}

	public Date lastModified() {
		// TODO Auto-generated method stub
		return null;
	}

	public long liveTimeSpan() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long size() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Struct getCustomInfo() {
		return CacheUtil.getInfo(this);
	}

}
