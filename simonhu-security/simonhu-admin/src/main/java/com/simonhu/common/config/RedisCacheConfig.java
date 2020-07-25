package com.simonhu.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: SimonHu
 * @Date: 2020/7/15 13:17
 * @Description:给@Cacheable配置过期时间
 */
@Configuration
public class RedisCacheConfig {
    @Value("${simonhu.globalSessionTimeout}")
    private long globalSessionTimeout;
    @Autowired
    ResourceLoader resourceLoader;
    
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.builder(redisConnectionFactory)
                //默认的缓存配置(没有配置键的key均使用此配置)
                .cacheDefaults(getDefaultCacheConfiguration())
                .withInitialCacheConfigurations(getCacheConfigurations())
                //在spring事务正常提交时才缓存数据
                .transactionAware()
                .build();
    }
    
    private Map<String, RedisCacheConfiguration> getCacheConfigurations() {
        Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>();
        //缓存键,且3600*10秒后过期,3600*10秒后再次调用方法时需要重新缓存
        configurationMap.put("AllMenuList", this.getDefaultCacheConfiguration(globalSessionTimeout * 10));
        configurationMap.put("Menus", this.getDefaultCacheConfiguration(globalSessionTimeout * 10));
        configurationMap.put("NotButtonList", this.getDefaultCacheConfiguration(globalSessionTimeout * 10));
        configurationMap.put("UserMenuList", this.getDefaultCacheConfiguration(globalSessionTimeout * 10));
        configurationMap.put("AllpermUser", this.getDefaultCacheConfiguration(globalSessionTimeout * 10));
        configurationMap.put("ListParentId", this.getDefaultCacheConfiguration(globalSessionTimeout * 10));
        return configurationMap;
    }
    
    /**
     * 获取redis的缓存配置(针对于键)
     *
     * @param seconds 键过期时间
     * @return
     */
    private RedisCacheConfiguration getDefaultCacheConfiguration(long seconds) {
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration.entryTtl(Duration.ofSeconds(seconds));
        return redisCacheConfiguration;
    }
    
    /**
     * 获取Redis缓存配置,此处获取的为默认配置
     * 如对键值序列化方式,是否缓存null值,是否使用前缀等有特殊要求
     * 可另行调用 RedisCacheConfiguration 的构造方法
     *
     * @return
     */
    private RedisCacheConfiguration getDefaultCacheConfiguration() {
        // 注意此构造函数为 spring-data-redis-2.1.9 及以上拥有,经试验 已知spring-data-redis-2.0.9及以下版本没有此构造函数
        // 但观察源码可得核心不过是在值序列化器(valueSerializationPair)的构造中注入 ClassLoader 即可
        return RedisCacheConfiguration.defaultCacheConfig(resourceLoader.getClassLoader());
    }
}
