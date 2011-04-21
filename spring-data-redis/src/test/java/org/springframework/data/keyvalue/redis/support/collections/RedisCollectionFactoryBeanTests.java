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
package org.springframework.data.keyvalue.redis.support.collections;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.data.keyvalue.redis.ConnectionFactoryTracker;
import org.springframework.data.keyvalue.redis.SettingsUtils;
import org.springframework.data.keyvalue.redis.connection.RedisConnection;
import org.springframework.data.keyvalue.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.keyvalue.redis.core.RedisCallback;
import org.springframework.data.keyvalue.redis.core.StringRedisTemplate;
import org.springframework.data.keyvalue.redis.support.collections.RedisCollectionFactoryBean.CollectionType;

/**
 * @author Costin Leau
 */
public class RedisCollectionFactoryBeanTests {

	protected ObjectFactory<String> factory = new StringObjectFactory();
	protected StringRedisTemplate template;
	protected RedisStore col;

	public RedisCollectionFactoryBeanTests() {
		JedisConnectionFactory jedisConnFactory = new JedisConnectionFactory();
		jedisConnFactory.setUsePool(true);

		jedisConnFactory.setPort(SettingsUtils.getPort());
		jedisConnFactory.setHostName(SettingsUtils.getHost());

		jedisConnFactory.afterPropertiesSet();

		this.template = new StringRedisTemplate(jedisConnFactory);
		ConnectionFactoryTracker.add(jedisConnFactory);
	}

	@AfterClass
	public static void cleanUp() {
		ConnectionFactoryTracker.cleanUp();
	}

	@After
	public void tearDown() throws Exception {
		// clean up the whole db
		template.execute(new RedisCallback<Object>() {

			@Override
			public Object doInRedis(RedisConnection connection) {
				connection.flushDb();
				return null;
			}
		});
	}

	private RedisStore createCollection(String key) {
		return createCollection(key, null);
	}

	private RedisStore createCollection(String key, CollectionType type) {
		RedisCollectionFactoryBean fb = new RedisCollectionFactoryBean();
		fb.setKey(key);
		fb.setTemplate(template);
		fb.setType(type);
		fb.afterPropertiesSet();

		return fb.getObject();
	}

	@Test
	public void testNone() throws Exception {
		RedisStore store = createCollection("nosrt", CollectionType.PROPERTIES);
		assertThat(store, instanceOf(RedisProperties.class));

		store = createCollection("nosrt", CollectionType.MAP);
		assertThat(store, instanceOf(DefaultRedisMap.class));

		store = createCollection("nosrt", CollectionType.SET);
		assertThat(store, instanceOf(DefaultRedisSet.class));

		store = createCollection("nosrt", CollectionType.LIST);
		assertThat(store, instanceOf(DefaultRedisList.class));

		store = createCollection("nosrt");
		assertThat(store, instanceOf(DefaultRedisList.class));
	}


	@Test
	public void testExistingCol() throws Exception {
		String key = "set";
		String val = "value";

		template.boundSetOps(key).add(val);
		RedisStore col = createCollection(key);
		assertThat(col, is(DefaultRedisSet.class));

		key = "map";
		template.boundHashOps(key).put(val, val);
		col = createCollection(key);
		assertThat(col, is(DefaultRedisMap.class));

		col = createCollection(key, CollectionType.PROPERTIES);
		assertThat(col, is(RedisProperties.class));

	}
}