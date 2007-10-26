package com.googlecode.wmbutil.cache;

public class Lookup {

	private Cache cache;
	
	public Lookup(String cacheName) throws LookupCacheException {
		this.cache = Cache.getCache(cacheName);
	}


	public String lookupValue(String key) throws LookupCacheException {
		return cache.lookupValue(key);
	}
}
