/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */
package com.simonhu.common.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 * @author Mark sunlightcs@gmail.com
 */
@Component
public class RedisUtils {
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valueOperations;
    @Resource(name = "stringRedisTemplate")
    private HashOperations<String, String, Object> hashOperations;
    @Resource(name = "stringRedisTemplate")
    private ListOperations<String, Object> listOperations;
    @Resource(name = "stringRedisTemplate")
    private SetOperations<String, Object> setOperations;
    @Resource(name = "stringRedisTemplate")
    private ZSetOperations<String, Object> zSetOperations;
    /**
     * 默认过期时长，单位：秒
     */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    /**
     * 不设置过期时长
     */
    public final static long NOT_EXPIRE = -1;
    
    public void set(String key, Object value, long expire) {
        valueOperations.set(key, toJson(value));
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }
    /**
      * @Description:有效期
      * @Author:SimonHu
      * @Date: 2020/5/22 16:46
      * @param key
      * @param expire
      * @return void
      */
    public void expire(String key, long expire) {
        redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }
    
    public void set(String key, Object value) {
        set(key, value, DEFAULT_EXPIRE);
    }
    
    /**
     * redis中添加,如果key已经存在，返回false
     *
     * @param key
     * @param value
     */
    public boolean setnx(String key, String value) {
        return valueOperations.setIfAbsent(key, value);
    }
    
    public <T> T get(String key, Class<T> clazz, long expire) {
        String value = valueOperations.get(key);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : fromJson(value, clazz);
    }
    
    public <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, NOT_EXPIRE);
    }
    
    public String get(String key, long expire) {
        String value = valueOperations.get(key);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value;
    }
    
    public String get(String key) {
        return get(key, NOT_EXPIRE);
    }
    
    public void delete(String key) {
        redisTemplate.delete(key);
    }
    
    /**
     * Object转成JSON数据
     */
    private String toJson(Object object) {
        if (object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String) {
            return String.valueOf(object);
        }
        return JSON.toJSONString(object);
    }
    
    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }
}
