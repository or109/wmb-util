INSERT INTO cache (name) values('cache1');
INSERT INTO cache (name) values('cache2');

INSERT INTO lookup (cache_id, name, value, ttl, ttd) values(1, 'key1', 'value1', 1000, 2000);
INSERT INTO lookup (cache_id, name, value, ttl, ttd) values(1, 'key2', 'value2', 1000, 2000);
INSERT INTO lookup (cache_id, name, value, ttl, ttd) values(1, 'key3', 'value3', 1000, 2000);
INSERT INTO lookup (cache_id, name, value, ttl, ttd) values(2, 'key4', 'value4', 1000, 2000);
INSERT INTO lookup (cache_id, name, value, ttl, ttd) values(2, 'key5', 'value5', 1000, 2000);
