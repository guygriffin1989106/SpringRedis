/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.data.redis.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * CacheManager implementation for Redis.
 * By default saves the keys by appending a prefix (which acts as a namespace). For performance reasons, the current implementation
 * uses a set for the keys in each cache.
 * 
 * @author Costin Leau
 */
public class RedisCacheManager implements CacheManager {

	// fast lookup by name map
	private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();
	private final Collection<String> names = Collections.unmodifiableSet(caches.keySet());
	private final RedisTemplate template;

	private boolean usePrefix;
	private RedisCachePrefix cachePrefix = new DefaultRedisCachePrefix();

    // 0 - never expire
    private long defaultExpireTime = 0;
    private Map<String, Long>expires = new HashMap<String, Long>();

	public RedisCacheManager(RedisTemplate template) {
		this.template = template;
	}

	public Cache getCache(String name) {
		Cache c = caches.get(name);
		if (c == null) {
            long expire = (expires.containsKey(name)) ? expires.get(name) : defaultExpireTime;
			c = new RedisCache(name, (usePrefix ? cachePrefix.prefix(name) : null), template, expire);
			caches.put(name, c);
		}

		return c;
	}

	public Collection<String> getCacheNames() {
		return names;
	}

    public void setUsePrefix(boolean usePrefix) {
        this.usePrefix = usePrefix;
    }

    /**
	 * Sets the cachePrefix.
	 *
	 * @param cachePrefix the cachePrefix to set
	 */
	public void setCachePrefix(RedisCachePrefix cachePrefix) {
		this.cachePrefix = cachePrefix;
	}

    /**
     * Set default expire time.
     *
     * @param defaultExpireTime time in ms
     */
    public void setDefaultExpireTime(long defaultExpireTime) {
        this.defaultExpireTime = defaultExpireTime;
    }

    /**
     * Set expire time for caches
     *
     * @param expires time in ms
     */
    public void setExpires(Map<String, Long> expires) {
        this.expires = expires;
    }
}