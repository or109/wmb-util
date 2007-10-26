package com.googlecode.wmbutil.cache;

import junit.framework.TestCase;

public class LookupTest extends TestCase {

	public void test() throws LookupCacheException {
		LookupDataSourceFactory.setDataSource(null);

		Lookup lookup = new Lookup("cache1");
		
		assertEquals("value1", lookup.lookupValue("key1"));
		assertEquals("value2", lookup.lookupValue("key2"));
		assertEquals("value3", lookup.lookupValue("key3"));
		assertNull(lookup.lookupValue("key4"));

		Lookup lookup2 = new Lookup("cache2");
		assertNull(lookup2.lookupValue("key3"));
		assertEquals("value4", lookup2.lookupValue("key4"));
		assertEquals("value5", lookup2.lookupValue("key5"));
	}
	
	public void testInitialLoadFail() throws LookupCacheException {
		MockDataSource ds = new MockDataSource();
		ds.setExceptionToThrow(new CacheRefreshException("mock"));
		LookupDataSourceFactory.setDataSource(ds);
		
		
		try  {
			// must not be able to load cache
			new Lookup("cache56");
		} catch(CacheRefreshException e) {
			// ok!
		}
	}
}
