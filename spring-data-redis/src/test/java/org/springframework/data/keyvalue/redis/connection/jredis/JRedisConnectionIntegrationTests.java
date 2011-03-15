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

package org.springframework.data.keyvalue.redis.connection.jredis;

import org.jredis.JRedis;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.data.keyvalue.redis.SettingsUtils;
import org.springframework.data.keyvalue.redis.connection.AbstractConnectionIntegrationTests;
import org.springframework.data.keyvalue.redis.connection.RedisConnectionFactory;

public class JRedisConnectionIntegrationTests extends AbstractConnectionIntegrationTests {

	JredisConnectionFactory factory;

	public JRedisConnectionIntegrationTests() {
		factory = new JredisConnectionFactory();
		factory.setPort(SettingsUtils.getPort());
		factory.setHostName(SettingsUtils.getHost());

		factory.setUsePool(true);
		factory.afterPropertiesSet();
	}

	@Override
	protected RedisConnectionFactory getConnectionFactory() {
		return factory;
	}

	@Test
	public void testRaw() throws Exception {
		JRedis jr = (JRedis) factory.getConnection().getNativeConnection();

		System.out.println(jr.dbsize());
		System.out.println(jr.exists("foobar"));
		jr.set("foobar", "barfoo");
		System.out.println(jr.get("foobar"));
	}

	@Ignore("JRedis has connecting issues with null")
	public void testNullSerialization() {
	}

	@Ignore("JRedis has connecting issues with null")
	public void testHashNullValue() {
	}

	@Ignore("JRedis has connecting issues with null")
	public void testHashNullKey() {
	}

	@Ignore("JRedis has connecting issues with null")
	public void testNullValue() {
	}

	@Ignore("JRedis has connecting issues with null")
	public void testNullKey() {
	}

	@Ignore("JRedis does not support pipelining")
	public void testNullCollections() {
	}
}
