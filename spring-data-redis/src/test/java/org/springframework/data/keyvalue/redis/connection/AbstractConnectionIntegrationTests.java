/*
 * Copyright 2010-2011 the original author or authors.
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

package org.springframework.data.keyvalue.redis.connection;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.keyvalue.redis.Address;
import org.springframework.data.keyvalue.redis.Person;
import org.springframework.data.keyvalue.redis.core.StringRedisTemplate;
import org.springframework.data.keyvalue.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.keyvalue.redis.serializer.RedisSerializer;
import org.springframework.data.keyvalue.redis.serializer.StringRedisSerializer;

public abstract class AbstractConnectionIntegrationTests {

	protected StringRedisConnection connection;
	protected RedisSerializer serializer = new JdkSerializationRedisSerializer();
	protected RedisSerializer stringSerializer = new StringRedisSerializer();

	private static final String listName = "test-list";
	private static final byte[] EMPTY_ARRAY = new byte[0];

	protected abstract RedisConnectionFactory getConnectionFactory();

	private static Set<RedisConnectionFactory> connFactories = new LinkedHashSet<RedisConnectionFactory>();

	@Before
	public void setUp() {
		connection = new DefaultStringRedisConnection(getConnectionFactory().getConnection());
		connFactories.add(getConnectionFactory());

	}

	@AfterClass
	public static void cleanUp() {
		if (connFactories != null) {
			for (RedisConnectionFactory connectionFactory : connFactories) {
				try {
					((DisposableBean) connectionFactory).destroy();
				} catch (Exception ex) {
					System.err.println("Cannot clean factory " + connectionFactory + ex);
				}
			}
		}
	}


	@After
	public void tearDown() {
		connection.close();
		connection = null;
	}

	@Test
	public void testLPush() throws Exception {
		Long index = connection.lPush(listName.getBytes(), "bar".getBytes());
		if (index != null) {
			assertEquals((Long) (index + 1), connection.lPush(listName.getBytes(), "bar".getBytes()));
		}
	}

	@Test
	public void testSetAndGet() {
		connection.set("foo".getBytes(), "blahblah".getBytes());
		assertEquals("blahblah", new String(connection.get("foo".getBytes())));
	}

	private boolean isJredis() {
		return connection.getClass().getSimpleName().startsWith("Jredis");
	}


	@Test
	public void testByteValue() {
		String value = UUID.randomUUID().toString();
		Person person = new Person(value, value, 1, new Address(value, 2));
		String key = getClass() + ":byteValue";
		byte[] rawKey = stringSerializer.serialize(key);

		connection.set(rawKey, serializer.serialize(person));
		byte[] rawValue = connection.get(rawKey);
		assertNotNull(rawValue);
		assertEquals(person, serializer.deserialize(rawValue));
	}

	@Test
	public void testPingPong() throws Exception {
		assertEquals("PONG", connection.ping());
	}

	@Test
	public void testInfo() throws Exception {
		Properties info = connection.info();
		assertNotNull(info);
		assertTrue("at least 5 settings should be present", info.size() >= 5);
		String version = info.getProperty("redis_version");
		assertNotNull(version);
		System.out.println(info);
	}

	@Test
	public void testNullKey() throws Exception {
		connection.decr(EMPTY_ARRAY);
		try {
			connection.decr((String) null);
		} catch (Exception ex) {
			// excepted
		}
	}

	@Test
	public void testNullValue() throws Exception {
		byte[] key = UUID.randomUUID().toString().getBytes();
		connection.append(key, EMPTY_ARRAY);
		try {
			connection.append(key, null);
		} catch (DataAccessException ex) {
			// expected
		}
	}

	@Test
	public void testHashNullKey() throws Exception {
		byte[] key = UUID.randomUUID().toString().getBytes();
		connection.hExists(key, EMPTY_ARRAY);
		try {
			connection.hExists(key, null);
		} catch (DataAccessException ex) {
			// expected
		}
	}

	@Test
	public void testHashNullValue() throws Exception {
		byte[] key = UUID.randomUUID().toString().getBytes();
		byte[] field = "random".getBytes();

		connection.hSet(key, field, EMPTY_ARRAY);
		try {
			connection.hSet(key, field, null);
		} catch (DataAccessException ex) {
			// expected
		}
	}

	@Test
	public void testNullSerialization() throws Exception {
		String[] keys = new String[] { "~", "[" };
		List<String> mGet = connection.mGet(keys);
		assertEquals(2, mGet.size());
		assertNull(mGet.get(0));
		assertNull(mGet.get(1));

		StringRedisTemplate stringTemplate = new StringRedisTemplate(getConnectionFactory());
		List<String> multiGet = stringTemplate.opsForValue().multiGet(Arrays.asList(keys));
		assertEquals(2, multiGet.size());
		assertNull(multiGet.get(0));
		assertNull(multiGet.get(1));
	}

	@Test
	public void testNullCollections() throws Exception {
		connection.openPipeline();
		assertNull(connection.keys("~*"));
		assertNull(connection.hKeys("~"));
		connection.closePipeline();
	}
}