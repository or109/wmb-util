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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class JdbcLookupDataSource implements LookupDataSource {

	private DataSource ds;
	
	public JdbcLookupDataSource(DataSource ds) {
		this.ds = ds;
	}
	

	public LookupData[] lookup(String cacheName) throws CacheRefreshException {
		Connection conn;
		try {
			conn = ds.getConnection();
		
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT lookup.name, lookup.value, lookup.ttl, lookup.ttd " +
					"FROM lookup, cache " +
					"WHERE cache.id = lookup.cache_id AND cache.name='" + cacheName + "';");
			
			List dataList = new ArrayList();
			while(rs.next()) {
				String key = rs.getString("name");
				String value = rs.getString("value");
				long ttl = rs.getLong("ttl");
				long ttd = rs.getLong("ttd");
				
				dataList.add(new LookupData(key, value, ttl, ttd));
			}
			
			return (LookupData[]) dataList.toArray(new LookupData[0]);
		} catch (SQLException e) {
			throw new CacheRefreshException("Failed to read from database", e);
		}
	}

}
