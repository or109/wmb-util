package com.googlecode.wmbutil.cache;

public class StaleCacheException extends LookupCacheException {

	private static final long serialVersionUID = -5497620953666125357L;

	public StaleCacheException() {
		super();
	}

	public StaleCacheException(String message, Throwable cause) {
		super(message, cause);
	}

	public StaleCacheException(String message) {
		super(message);
	}

	public StaleCacheException(Throwable cause) {
		super(cause);
	}

}
