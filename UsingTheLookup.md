First, setup your LookupConfiguration.

## Understanding time-outs ##
The Lookup cache values in memory. This has two purposes, first to speed up lookup. Second, to provide robustness in case the database failure. However, since values might be updated, they can not be cached forever. The following configuration values control the cache:
  * TTL: time to live. The number of seconds before the cache will refresh the value from the database
  * TTD: time to die. If the cache fails to refresh due to a database problem after TTL has passed, the value can still be used to provide additional robustness. The TTD value controls for how long a old value can still be used.

Set TTL and TTD to 0 to disable caching.

## Adding values to the database ##
  * Insert a component into the component table
  * Using the ID generated in the previous step, add the lookup values needed to the lookup table

## Using the lookup API ##
In your Java code, you create a lookup instance using:
```
Lookup lookup = new Lookup("MyComponent");
```

It is recommended to do this once per Java compute node. There is no need to cache the Lookup instance as it is cheap to create.

After creating, you can lookup values using:
```
String myValue = lookup.lookupValue("MyKey");
```

If the key does not exist in the database, null will be returned. Otherwise, your configured value will be returned.