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
