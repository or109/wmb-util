package com.googlecode.wmbutil.cache;

import java.util.HashMap;
import java.util.Map;

public class Cache {

	private static final Map caches = new HashMap();
	
	/**
	 * Gets or creates an existing cache
	 * @param cacheName
	 * @return
	 * @throws LookupCacheException 
	 */
	public static Cache getCache(String cacheName) throws LookupCacheException {
		synchronized (caches) {
			Cache cache = (Cache) caches.get(cacheName);
			if(cache == null) {
				cache = new Cache(cacheName);
				caches.put(cacheName, cache);
			}
			return cache;
			
		}
	}
	
	private final String cacheName;
	private LookupDataSource dataSource;
	
	private Map cachedData;
	private long cacheRefreshTime;
	
	// hide cstr
	private Cache(String cacheName) throws LookupCacheException {
		this.cacheName = cacheName;
		
		dataSource = LookupDataSourceFactory.getDataSource();
		
		refreshCache();
	}
	
	private final synchronized void refreshCache() throws CacheRefreshException {
		LookupData[] dataArray = dataSource.lookup(cacheName);
		
		Map oldCachedData = cachedData;
		
		if(cachedData != null) {
			cachedData.clear();
		} else {
			cachedData = new HashMap();
		}
		
		try {
			for (int i = 0; i < dataArray.length; i++) {
				LookupData data = dataArray[i];
				
				cachedData.put(data.getKey(), data);
			}
			
			cacheRefreshTime = System.currentTimeMillis();
		} catch(Exception e) {
			cachedData = oldCachedData;
			throw new CacheRefreshException("Failed to refresh cache", e);
		}
	}
	
	private boolean dataHasPassedTTL(LookupData data) {
		return data.getTtl() +  cacheRefreshTime > System.currentTimeMillis();
	}

	private boolean dataHasPassedTTD(LookupData data) {
		return data.getTtd() +  cacheRefreshTime > System.currentTimeMillis();
	}
	
	// TODO revisit concurrency
	public synchronized String lookupValue(String key) throws LookupCacheException {
		if(cachedData == null) {
			// initial init failed, try refresh
			refreshCache();
			
			if(cachedData == null) {
				// failed refresh, throw exception
				throw new StaleCacheException("Failed to init lookup cache");
			}
		}
		
		LookupData data = (LookupData) cachedData.get(key);
		
		if(data == null) {
			// data not found in cache
			return null;
		} else {
			if(dataHasPassedTTL(data)) {
				// old data, try refresh
				try {
					refreshCache();
					
					// get new value
					data = (LookupData) cachedData.get(key);
					return data.getValue();
					
				} catch(CacheRefreshException e) {
					// refresh failed, so if we can use old value
					if(dataHasPassedTTD(data)) {
						throw new StaleCacheException("Cache could not be updated and value has died");
					} else {
						// return old, but still alive data
						return data.getValue();
					}
				}
			} else {
				return data.getValue();
			}
		}
	}
}
