package com.googlecode.wmbutil.cache;

public class LookupData {

	private String key;
	private String value;
	private long ttl;
	private long ttd;
	
	public LookupData(String key, String value, long ttl, long ttd) {
		this.key = key;
		this.value = value;
		this.ttl = ttl;
		this.ttd = ttd;
	}
	
	public String getKey() {
		return key;
	}
	public String getValue() {
		return value;
	}
	public long getTtl() {
		return ttl;
	}
	public long getTtd() {
		return ttd;
	}
}
