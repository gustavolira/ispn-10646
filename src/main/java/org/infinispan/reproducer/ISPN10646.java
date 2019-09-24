package org.infinispan.reproducer;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.persistence.jdbc.configuration.JdbcStringBasedStoreConfigurationBuilder;

/**
 * Reproducer for https://issues.jboss.org/browse/ISPN-10646
 */
public class ISPN10646 {

   public static void main(String[] args) {

      GlobalConfigurationBuilder globalConfigurationBuilder = new GlobalConfigurationBuilder().defaultCacheName("default");
      ConfigurationBuilder builder = new ConfigurationBuilder();
      Configuration build = builder.persistence().addStore(JdbcStringBasedStoreConfigurationBuilder.class)
            .table()
            .dropOnExit(true)
            .createOnStart(true)
            .tableNamePrefix("ISPN_STRING_TABLE")
            .idColumnName("ID_COLUMN").idColumnType("VARCHAR(255)")
            .dataColumnName("DATA_COLUMN").dataColumnType("VARBINARY(1000)")
            .timestampColumnName("TIMESTAMP_COLUMN").timestampColumnType("BIGINT")
            .connectionPool()
            .connectionUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1")
            .username("sa")
            .password("sa")
            .driverClass("org.h2.Driver")
            .build();

      DefaultCacheManager cacheManager = new DefaultCacheManager(globalConfigurationBuilder.build(), build, true);
      Cache<String, String> cache = cacheManager.getCache();
      cache.put("k1", "v1");
      System.out.println(cache.get("k1"));

   }
}
