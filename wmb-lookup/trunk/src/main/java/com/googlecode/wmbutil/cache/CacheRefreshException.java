package com.googlecode.wmbutil.cache;

public class CacheRefreshException extends LookupCacheException {

	private static final long serialVersionUID = 6367452043939466587L;

	public CacheRefreshException() {
		super();
	}

	public CacheRefreshException(String message, Throwable cause) {
		super(message, cause);
	}

	public CacheRefreshException(String message) {
		super(message);
	}

	public CacheRefreshException(Throwable cause) {
		super(cause);
	}

}
