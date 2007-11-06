/*
 * Copyright 2007 (C) Callista Enterprise.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *	http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.googlecode.wmbutil.cache;

import java.util.List;
import java.util.Map;

public class MockDataSource implements LookupDataSource {

	private CacheRefreshException exceptionToThrow;
	private Map data;
	
	public Map getData() {
		return data;
	}

	public void setData(Map data) {
		this.data = data;
	}

	public LookupData[] lookup(String cacheName) throws CacheRefreshException {
		if(exceptionToThrow != null) {
			throw exceptionToThrow;
		}
		
		List dataList = (List) data.get(cacheName);
		
		if(dataList == null) {
			return null;
		} else {
			return (LookupData[]) dataList.toArray(new LookupData[0]);
		}
	}

	public CacheRefreshException getExceptionToThrow() {
		return exceptionToThrow;
	}

	public void setExceptionToThrow(CacheRefreshException exceptionToThrow) {
		this.exceptionToThrow = exceptionToThrow;
	}

}
