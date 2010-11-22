package railo.extension.io.cache.infinispan;

import org.infinispan.manager.EmbeddedCacheManager;
import railo.commons.io.cache.Cache;
import railo.commons.io.cache.CacheEntry;
import railo.commons.io.cache.CacheEntryFilter;
import railo.commons.io.cache.CacheKeyFilter;
import railo.runtime.type.Struct;

import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: andrea
 * Date: Nov 21, 2010
 * Time: 3:39:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class InfinispanCache implements Cache{

    private EmbeddedCacheManager cm = CacheManagerFactory.getDefaultCacheManager();;

    @Override
    public void init(String s, Struct struct) throws IOException {
    }

    @Override
    public CacheEntry getCacheEntry(String s) throws IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object getValue(String s) throws IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CacheEntry getCacheEntry(String s, CacheEntry cacheEntry) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object getValue(String s, Object o) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void put(String s, Object o, Long aLong, Long aLong1) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean contains(String s) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean remove(String s) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int remove(CacheKeyFilter cacheKeyFilter) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int remove(CacheEntryFilter cacheEntryFilter) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List keys() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List keys(CacheKeyFilter cacheKeyFilter) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List keys(CacheEntryFilter cacheEntryFilter) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List values() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List values(CacheKeyFilter cacheKeyFilter) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List values(CacheEntryFilter cacheEntryFilter) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List entries() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List entries(CacheKeyFilter cacheKeyFilter) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List entries(CacheEntryFilter cacheEntryFilter) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long hitCount() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long missCount() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Struct getCustomInfo() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

