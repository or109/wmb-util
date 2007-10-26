package com.googlecode.wmbutil.cache;

public interface LookupDataSource {

	// TODO returns null if cache is not known, should throw?
	LookupData[] lookup(String cache) throws CacheRefreshException;
	
}
