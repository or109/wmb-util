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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;

public class LookupDataSourceFactory {

	private static LookupDataSource dataSource;
	
	public static synchronized LookupDataSource getDataSource() throws CacheRefreshException {
		if(dataSource == null) {
			try {
				Properties config = new Properties();
				config.load(new FileInputStream("lookup-connection.properties"));
				
				BasicDataSource ds = new BasicDataSource();
				ds.setDriverClassName(config.getProperty("lookup.class"));
				ds.setUrl(config.getProperty("lookup.url"));
				ds.setUsername(config.getProperty("lookup.username"));
				ds.setPassword(config.getProperty("lookup.password"));
				ds.setDefaultReadOnly(false);
				
				dataSource = new JdbcLookupDataSource(ds);
			} catch (FileNotFoundException e) {
				throw new CacheRefreshException("Could not find lookup-connection.properties file", e);
			} catch (IOException e) {
				throw new CacheRefreshException("Found, but could not read from lookup-connection.properties file", e);
			} catch (RuntimeException e) {
				throw new CacheRefreshException("Could not create data source", e);
			}
		}
		
		return dataSource;
	}
	
	protected static void setDataSource(LookupDataSource newDataSource) {
		dataSource = newDataSource;
	}
}
