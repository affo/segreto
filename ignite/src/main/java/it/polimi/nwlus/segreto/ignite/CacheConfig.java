package it.polimi.nwlus.segreto.ignite;

import org.apache.ignite.configuration.CacheConfiguration;

import javax.cache.configuration.FactoryBuilder;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.util.concurrent.TimeUnit;

public class CacheConfig {
    public static CacheConfiguration<String, String> tupleCache(Integer windowSize) {
        CacheConfiguration<String, String> cfg = new CacheConfiguration<>("tuples");

        cfg.setIndexedTypes(String.class, String.class);

        // Sliding window of `windowSize` seconds.
        cfg.setExpiryPolicyFactory(FactoryBuilder.factoryOf(
                new CreatedExpiryPolicy(new Duration(TimeUnit.SECONDS, windowSize))));

        return cfg;
    }
}
